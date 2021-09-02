package xyz.dps0340.kpucountdown.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.dps0340.kpucountdown.DTO.VaccineStatisticDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineStatisticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private Long totalFirstCnt;
    private Long totalSecondCnt;

    public VaccineStatisticEntity(VaccineStatisticDTO dto) {
        this.date = dto.getDate();
        this.totalFirstCnt = dto.getTotalFirstCnt();
        this.totalSecondCnt = dto.getTotalSecondCnt();
    }

}
