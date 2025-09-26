package overlook_hotel.overlook_hotel.service;

import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.*;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.repository.*;
import overlook_hotel.overlook_hotel.model.RoomReservationCart;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    public RoomReservation saveCartAsReservation(RoomReservationCart cart, Long clientId) {
        if (cart.getRooms().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Pick start and end date from the first room (or calculate a general start/end)
        LocalDate startDate = cart.getRooms().get(0).getStartDate();
        LocalDate endDate = cart.getRooms().get(0).getEndDate();

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalStateException("Client not found"));

        RoomReservation reservation = new RoomReservation();
        reservation.setClient(client);
        reservation.setCreationDate(LocalDate.now());
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setStatus(true); // confirmed
        reservation.setTotalPrice(BigDecimal.ZERO); // will calculate below

        reservation = roomReservationRepository.save(reservation); // save to generate ID

        BigDecimal totalCartPrice = BigDecimal.ZERO;

        for (RoomReservationFields fields : cart.getRooms()) {
            // 1️⃣ Link room
            Room room = roomService.findById(fields.getIdRoom());
            RoomLinkReservation link = new RoomLinkReservation();
            link.setRoomReservation(reservation);
            link.setRoom(room);

            reservation.getRoomLinks().add(link);

            // 2️⃣ Add additional bonuses
            if (fields.getAdditionalBonuses() != null) {
                for (RoomBonusEnum bonusEnum : fields.getAdditionalBonuses()) {
                    RoomBonus bonus = roomBonusService.findBonusByType(bonusEnum);
                    if (bonus != null) {
                        RoomReservationBonus resBonus = new RoomReservationBonus();
                        resBonus.setRoomReservation(reservation);
                        resBonus.setRoom(room);
                        resBonus.setBonus(bonus);
                        reservation.getAdditionalBonuses().add(resBonus);
                    }
                }
            }

            // 3️⃣ Calculate price
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

        // Save again to persist room links and bonuses
        return roomReservationRepository.save(reservation);
    }



//    public void saveReservation(RoomReservationFields fields, Long clientId) {
//
//
//        Client client = clientRepository.findById(clientId)
//                .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));
//
//        RoomReservation reservation = new RoomReservation();
////        reservation.setId(clientId);
//        reservation.setClient(client);
//        reservation.setCreationDate(LocalDate.now());
//        reservation.setStartDate(fields.getStartDate());
//        reservation.setEndDate(fields.getEndDate());
//        reservation.setStatus(true); // assume confirmed
//        reservation.setTotalPrice(fields.getTotalPriceWithAdditional());
//
//        reservation = roomReservationRepository.save(reservation);
//
//        // link room
//        Room roomEntity = roomRepository.findById(fields.getIdRoom())
//                .orElseThrow(() -> new RuntimeException("Room not found: " + fields.getIdRoom()));
//        RoomLinkReservation link = new RoomLinkReservation();
//        link.setRoomReservation(reservation);
//        link.setRoom(roomEntity);
//        roomLinkReservationRepository.save(link);
//
//        // link additional bonuses
//        // link additional bonuses
//        if (fields.getAdditionalBonuses() != null) {
//            for (RoomBonusEnum bonusEnum : fields.getAdditionalBonuses()) {
//                RoomBonus bonus = roomBonusRepository.findByType(bonusEnum)
//                        .orElseThrow(() -> new RuntimeException("Bonus not found for type: " + bonusEnum.name()));
//
//                RoomReservationBonus rrBonus = new RoomReservationBonus();
//                rrBonus.setRoomReservation(reservation); // link to reservation
//                rrBonus.setRoom(roomEntity);             // link to room
//                rrBonus.setBonus(bonus);                 // link to bonus
//
//                roomReservationBonusRepository.save(rrBonus);
//            }
//        }
//    }
}
