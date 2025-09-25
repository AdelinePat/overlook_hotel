package overlook_hotel.overlook_hotel.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import overlook_hotel.overlook_hotel.model.enumList.EventType;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFilterFields {
    private Long placeTypeId;
    private Integer minCapacity;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private EventType eventType;
}
