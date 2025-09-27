package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.ReservationHistoryDTO;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.*;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.repository.*;
import overlook_hotel.overlook_hotel.model.RoomReservationCart;
import overlook_hotel.overlook_hotel.specification.RoomReservationSpecification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomReservationService {
    private final RoomReservationRepository roomReservationRepository;
    private final RoomLinkReservationRepository roomLinkReservationRepository;
    private final RoomReservationBonusRepository roomReservationBonusRepository;
    private final RoomBonusRepository roomBonusRepository;
    private final RoomRepository roomRepository;
    private final ClientRepository clientRepository;
    private final RoomService roomService;
    private final RoomBonusService roomBonusService;

    public RoomReservationService(RoomReservationRepository roomReservationRepository,
                                  RoomLinkReservationRepository roomLinkReservationRepository,
                                  RoomReservationBonusRepository roomReservationBonusRepository,
                                  RoomBonusRepository roomBonusRepository,
                                  RoomRepository roomRepository,
                                  ClientRepository clientRepository,
                                  RoomService roomService, RoomBonusService roomBonusService) {

        this.roomReservationRepository = roomReservationRepository;
        this.roomLinkReservationRepository = roomLinkReservationRepository;
        this.roomReservationBonusRepository = roomReservationBonusRepository;
        this.roomBonusRepository = roomBonusRepository;
        this.roomRepository = roomRepository;
        this.clientRepository = clientRepository;
        this.roomService = roomService;
        this.roomBonusService = roomBonusService;
    }

    private void setRoomReservation(RoomReservationCart cart, Long clientId, RoomReservation reservation) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalStateException("Client not found"));

        LocalDate startDate = cart.getRooms().get(0).getStartDate();
        LocalDate endDate = cart.getRooms().get(0).getEndDate();

        reservation.setClient(client);
        reservation.setCreationDate(LocalDate.now());
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setStatus(true); // confirmed
        reservation.setTotalPrice(BigDecimal.ZERO);
    }

    private void setDefaultRoomBonus(RoomReservation reservation, Room room, RoomBonus bonus) {
        if (bonus != null) {
            RoomReservationBonus resBonus = new RoomReservationBonus();
            resBonus.setRoomReservation(reservation);
            resBonus.setRoom(room);
            resBonus.setBonus(bonus);
            reservation.getAdditionalBonuses().add(resBonus);
        }
    }

    public RoomReservation saveCartAsReservation(RoomReservationCart cart, Long clientId) {
        if (cart.getRooms().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }


        RoomReservation reservation = new RoomReservation();
        this.setRoomReservation(cart, clientId, reservation);
        reservation = roomReservationRepository.save(reservation);


        BigDecimal totalCartPrice = BigDecimal.ZERO;

        for (RoomReservationFields fields : cart.getRooms()) {
            Room room = roomService.findById(fields.getIdRoom());
            RoomLinkReservation link = new RoomLinkReservation();
            link.setRoomReservation(reservation);
            link.setRoom(room);

            reservation.getRoomLinks().add(link);

            if (fields.getAdditionalBonuses() != null) {
                for (RoomBonusEnum bonusEnum : fields.getAdditionalBonuses()) {
                    RoomBonus bonus = roomBonusService.findBonusByType(bonusEnum);
                    this.setDefaultRoomBonus(reservation, room, bonus);
                }
            }

            BigDecimal basePrice = room.getNightPrice();

            BigDecimal baseBonusTotal = room.getBonuses().stream()
                    .map(RoomBonus::getDailyPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal additionalBonusTotal = BigDecimal.ZERO;
            if (fields.getAdditionalBonuses() != null) {
                for (RoomBonusEnum bonusEnum : fields.getAdditionalBonuses()) {
                    RoomBonus bonusEntity = roomBonusService.findBonusByType(bonusEnum);
                    if (bonusEntity != null) additionalBonusTotal = additionalBonusTotal.add(bonusEntity.getDailyPrice());
                }
            }

            long nights = ChronoUnit.DAYS.between(fields.getStartDate(), fields.getEndDate());
            if (nights < 1) nights = 1;

            BigDecimal nightlyTotal = basePrice.add(baseBonusTotal).add(additionalBonusTotal);
            BigDecimal totalPrice = nightlyTotal.multiply(BigDecimal.valueOf(nights));

            totalCartPrice = totalCartPrice.add(totalPrice);
        }

        reservation.setTotalPrice(totalCartPrice);

        return roomReservationRepository.save(reservation);
    }

    public List<RoomReservation> getAllReservationFromClient(Long clientId) {

        Specification<RoomReservation> spec =
                (root, query, cb) -> cb.conjunction();

        if (clientId != null) {
            spec = spec.and(RoomReservationSpecification.byClientId(clientId));
        }
        List<RoomReservation> reservations = roomReservationRepository.findAll(spec);
        reservations.forEach(roomReservation -> roomReservation.setTotalPrice(calculateReservationTotal(roomReservation)));

        return reservations;
    }

    public List<ReservationHistoryDTO> getAllReservationHistoryFromClient(Long clientId) {
        List<RoomReservation> reservations = this.getAllReservationFromClient(clientId);
        List<ReservationHistoryDTO> dtoList = new ArrayList<>();

        for (RoomReservation reservation : reservations) {

            int numberOfNights = (int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
            if (numberOfNights < 1) numberOfNights = 1;

            int numberOfRooms = reservation.getRoomLinks().size();

            BigDecimal totalPrice = reservation.getTotalPrice();

            ReservationHistoryDTO dto = new ReservationHistoryDTO(
                    reservation.getId(),
                    reservation.getCreationDate(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    numberOfNights,
                    numberOfRooms,
                    reservation.getPaymentDate(),
                    totalPrice,
                    null
            );

            dtoList.add(dto);
        }
        dtoList.sort((r1, r2) -> r2.getStartDate().compareTo(r1.getStartDate()));

        return dtoList;
    }

    private BigDecimal calculateReservationTotal(RoomReservation reservation) {
        return reservation.getRoomLinks().stream()
                .map(link -> calculateRoomTotal(link.getRoom(), reservation))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal total = BigDecimal.ZERO;
//
//        for (RoomLinkReservation link : reservation.getRoomLinks()) {
//            Room room = link.getRoom();
//
//            BigDecimal basePrice = room.getNightPrice();
//            BigDecimal baseBonusTotal = room.getBonuses().stream()
//                    .map(RoomBonus::getDailyPrice)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            BigDecimal additionalBonusTotal = reservation.getAdditionalBonuses().stream()
//                    .filter(b -> b.getRoom().getId().equals(room.getId()))
//                    .map(rb -> rb.getBonus().getDailyPrice())
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            long nights = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
//            if (nights < 1) nights = 1;
//
//            total = total.add(
//                    (basePrice.add(baseBonusTotal).add(additionalBonusTotal))
//                            .multiply(BigDecimal.valueOf(nights))
//            );
//        }
//
//        return total;
    }


    public void setReservationWithRooms(ReservationHistoryDTO dto, RoomReservation reservation) {
        List<RoomReservationFields> rooms = reservation.getRoomLinks().stream().map(link -> {

            RoomReservationFields r = new RoomReservationFields();

            Room room = link.getRoom();

            r.setIdRoom(room.getId());
            r.setDescription(room.getDescription());
            r.setCapacity(room.getCapacity());
            r.setStanding(room.getStanding());
            r.setStandingString(room.getStanding().toString());
            r.setStartDate(reservation.getStartDate());
            r.setEndDate(reservation.getEndDate());
            r.setTotalPriceWithAdditional(calculateRoomTotal(room, reservation)); // reuse your previous calculation

            r.setBonuses(room.getBonuses().stream().map(RoomBonus::getType).toList());

            r.setAdditionalBonuses(reservation.getAdditionalBonuses().stream()
                    .filter(b -> b.getRoom().getId().equals(room.getId()))
                    .map(rb -> rb.getBonus().getType())
                    .toList());

            return r;
        }).toList();

        dto.setRooms(rooms);
    }

    private BigDecimal calculateRoomTotal(Room room, RoomReservation reservation) {
        BigDecimal basePrice = room.getNightPrice();

        BigDecimal defaultBonusTotal = room.getBonuses().stream()
                .map(RoomBonus::getDailyPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal additionalBonusTotal = reservation.getAdditionalBonuses().stream()
                .filter(b -> b.getRoom().getId().equals(room.getId()))
                .map(rb -> rb.getBonus().getDailyPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long nights = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        if (nights < 1) nights = 1;

        return (basePrice.add(defaultBonusTotal).add(additionalBonusTotal))
                .multiply(BigDecimal.valueOf(nights));
    }



}
