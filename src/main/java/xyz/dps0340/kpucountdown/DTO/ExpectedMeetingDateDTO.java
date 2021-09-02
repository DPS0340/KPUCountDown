package xyz.dps0340.kpucountdown.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.dps0340.kpucountdown.Entity.ExpectedMeetingDateEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedMeetingDateDTO {

    private LocalDateTime date;

    public ExpectedMeetingDateDTO(ExpectedMeetingDateEntity entity) {
        this.date = entity.getDate();
    }

}
