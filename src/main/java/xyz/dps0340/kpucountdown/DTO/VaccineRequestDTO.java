package xyz.dps0340.kpucountdown.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccineRequestDTO {

    List<VaccineRequestDataDTO> data;
}
