package xyz.dps0340.kpucountdown.DTO;

import lombok.Getter;
import lombok.Setter;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;
import xyz.dps0340.kpucountdown.GlobalVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VaccineStatisticDTO {
    private LocalDateTime date;
    private Long totalFirstCnt;
    private Long totalSecondCnt;
    private Double firstRatio;
    private Double secondRatio;

    public VaccineStatisticDTO(VaccineStatisticEntity entity) {
        this.date = entity.getDate();
        this.totalFirstCnt = entity.getTotalFirstCnt();
        this.totalSecondCnt = entity.getTotalSecondCnt();
        this.firstRatio = 100.0 * this.totalFirstCnt / GlobalVariable.NATIONAL_POPULATION;
        this.secondRatio = 100.0 * this.totalSecondCnt / GlobalVariable.NATIONAL_POPULATION;
    }
}
