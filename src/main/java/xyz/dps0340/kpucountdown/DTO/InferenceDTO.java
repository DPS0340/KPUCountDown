package xyz.dps0340.kpucountdown.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InferenceDTO {
    private int idx;
    private int count;
    private List<Double> expectations;
}
