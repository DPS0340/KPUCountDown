package xyz.dps0340.kpucountdown.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.dps0340.kpucountdown.DTO.VaccineStatisticDTO;
import xyz.dps0340.kpucountdown.Service.VaccineStatisticService;

import java.util.List;

@RestController
public class VaccineStatisticController {
    @Autowired
    private VaccineStatisticService service;

    @GetMapping("/stat")
    public List<VaccineStatisticDTO> getVaccineStats() {
        return service.getStats();
    }

    @GetMapping("/stat/today")
    public VaccineStatisticDTO getTodayVaccineStat() {
        return service.getTodayStat();
    }

    @GetMapping(
            value = "/graph",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getVaccineStatsGraph() {
        return service.getVaccineStatsGraph();
    }

}
