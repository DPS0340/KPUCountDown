package xyz.dps0340.kpucountdown.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccineRequestDataDTO {
    private LocalDate baseDate;
    private long totalFirstCnt;
    private long totalSecondCnt;
}
