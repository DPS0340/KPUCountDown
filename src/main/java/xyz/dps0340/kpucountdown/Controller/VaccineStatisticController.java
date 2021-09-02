package xyz.dps0340.kpucountdown.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;
import xyz.dps0340.kpucountdown.Repository.VaccineStatisticRepository;
import xyz.dps0340.kpucountdown.DTO.VaccineStatisticDTO;
import xyz.dps0340.kpucountdown.Service.VaccineStatisticService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class VaccineStatisticController {
    @Autowired
    private VaccineStatisticService vaccineStatisticService;

    @GetMapping("/stat")
    public List<VaccineStatisticDTO> getVaccineStats() {
        return vaccineStatisticService.getStats();
    }

    @GetMapping("/stat/today")
    public VaccineStatisticDTO getTodayVaccineStat() {
        return vaccineStatisticService.getTodayStat();
    }

    @GetMapping(
            value = "/graph",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getVaccineStatsGraph() {
        return vaccineStatisticService.getVaccineStatsGraph();
    }
}
