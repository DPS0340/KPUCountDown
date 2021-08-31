package xyz.dps0340.kpucountdown.DTO;

import lombok.Getter;
import lombok.Setter;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VaccineStatisticDTO {
    private LocalDateTime date;
    private Long totalFirstCnt;
    private Long totalSecondCnt;
    private LocalDateTime expectedMeetingDate;

    public VaccineStatisticDTO(VaccineStatisticEntity entity) {
        this.date = entity.getDate();
        this.totalFirstCnt = entity.getTotalFirstCnt();
        this.totalSecondCnt = entity.getTotalSecondCnt();
        this.expectedMeetingDate = entity.getExpectedMeetingDate();
    }
}
