package xyz.dps0340.kpucountdown.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.dps0340.kpucountdown.DTO.ExpectedMeetingDateDTO;
import xyz.dps0340.kpucountdown.Service.ExpectService;
import xyz.dps0340.kpucountdown.Service.VaccineStatisticService;

@RestController
public class ExpectController {
    @Autowired
    private ExpectService service;
    @Autowired
    private VaccineStatisticService vaccineStatisticService;

    @GetMapping("/expect")
    public ExpectedMeetingDateDTO getExpectedMeetingDate() {
        return service.getDate();
    }

    @GetMapping(
            value = "/graph/expect",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getExpectedMeetingDateGraph() {
        return service.getExpectedGraph();
    }
}
