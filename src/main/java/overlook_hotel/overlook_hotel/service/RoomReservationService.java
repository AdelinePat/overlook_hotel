package overlook_hotel.overlook_hotel.service;

import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.*;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.repository.*;

import java.time.LocalDate;

@Service
public class RoomReservationService {
    private final RoomReservationRepository roomReservationRepository;
    private final RoomLinkReservationRepository roomLinkReservationRepository;
    private final RoomReservationBonusRepository roomReservationBonusRepository;
    private final RoomBonusRepository roomBonusRepository;
    private final RoomRepository roomRepository;
    private final ClientRepository clientRepository;

    public RoomReservationService(RoomReservationRepository roomReservationRepository,
                                  RoomLinkReservationRepository roomLinkReservationRepository,
                                  RoomReservationBonusRepository roomReservationBonusRepository,
                                  RoomBonusRepository roomBonusRepository,
                                  RoomRepository roomRepository,
                                  ClientRepository clientRepository) {

        this.roomReservationRepository = roomReservationRepository;
        this.roomLinkReservationRepository = roomLinkReservationRepository;
        this.roomReservationBonusRepository = roomReservationBonusRepository;
        this.roomBonusRepository = roomBonusRepository;
        this.roomRepository = roomRepository;
        this.clientRepository = clientRepository;
    }

    public void saveReservation(RoomReservationFields fields, Long clientId) {


        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

        RoomReservation reservation = new RoomReservation();
//        reservation.setId(clientId);
        reservation.setClient(client);
        reservation.setCreationDate(LocalDate.now());
        reservation.setStartDate(fields.getStartDate());
        reservation.setEndDate(fields.getEndDate());
        reservation.setStatus(true); // assume confirmed
        reservation.setTotalPrice(fields.getTotalPriceWithAdditional());

        reservation = roomReservationRepository.save(reservation);

        // link room
        Room roomEntity = roomRepository.findById(fields.getIdRoom())
                .orElseThrow(() -> new RuntimeException("Room not found: " + fields.getIdRoom()));
        RoomLinkReservation link = new RoomLinkReservation();
        link.setRoomReservation(reservation);
        link.setRoom(roomEntity);
        roomLinkReservationRepository.save(link);

        // link additional bonuses
        // link additional bonuses
        if (fields.getAdditionalBonuses() != null) {
            for (RoomBonusEnum bonusEnum : fields.getAdditionalBonuses()) {
                RoomBonus bonus = roomBonusRepository.findByType(bonusEnum)
                        .orElseThrow(() -> new RuntimeException("Bonus not found for type: " + bonusEnum.name()));

                RoomReservationBonus rrBonus = new RoomReservationBonus();
                rrBonus.setRoomReservation(reservation); // link to reservation
                rrBonus.setRoom(roomEntity);             // link to room
                rrBonus.setBonus(bonus);                 // link to bonus

                roomReservationBonusRepository.save(rrBonus);
            }
        }
    }
}
