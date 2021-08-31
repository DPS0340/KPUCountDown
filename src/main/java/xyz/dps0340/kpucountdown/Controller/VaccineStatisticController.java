package xyz.dps0340.kpucountdown.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;
import xyz.dps0340.kpucountdown.Repository.VaccineStatisticRepository;
import xyz.dps0340.kpucountdown.DTO.VaccineStatisticDTO;
import xyz.dps0340.kpucountdown.Service.VaccineStatisticService;

import java.time.LocalDate;

@RestController
public class VaccineStatisticController {
    @Autowired
    VaccineStatisticService vaccineStatisticService;

    @GetMapping("/stat")
    public VaccineStatisticDTO getVaccineStats() {
        return vaccineStatisticService.getTodayStats();
    }
}
