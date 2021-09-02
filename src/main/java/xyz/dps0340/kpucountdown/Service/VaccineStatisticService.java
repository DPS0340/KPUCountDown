package xyz.dps0340.kpucountdown.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.dps0340.kpucountdown.DTO.VaccineRequestDTO;
import xyz.dps0340.kpucountdown.DTO.VaccineRequestDataDTO;
import xyz.dps0340.kpucountdown.DTO.VaccineStatisticDTO;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;
import xyz.dps0340.kpucountdown.Repository.VaccineStatisticRepository;
import xyz.dps0340.kpucountdown.GlobalVariable;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Service
public class VaccineStatisticService {
    @Autowired
    VaccineStatisticRepository vaccineStatisticRepository;


    public VaccineStatisticDTO getTodayStats() {
        LocalDateTime today = LocalDate.now().atTime(0, 0, 0, 0);
        VaccineStatisticEntity vaccineStatisticEntity = vaccineStatisticRepository.findByDate(today).get();

        return new VaccineStatisticDTO(vaccineStatisticEntity);
    }

    public VaccineStatisticEntity crawlTodayStatsFromServer() {
        LocalDateTime today = LocalDate.now().atTime(0, 0, 0, 0);
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                                            .appendPattern("yyyy-MM-dd HH:mm:ss")
                                            .toFormatter();
        String todayString = today.format(dateTimeFormatter);

        RestTemplate restTemplate = new RestTemplate();

        String uriString = UriComponentsBuilder
                .fromUriString(GlobalVariable.OPENAPI_ENDPOINT)
                .queryParam("page", 1)
                .queryParam("perPage", 1)
                .queryParam("cond[baseDate::EQ]", todayString)
                .queryParam("cond[sido::EQ]", "전국")
                .encode()
                .build()
                .toUriString() + String.format("&serviceKey=%s", GlobalVariable.VACCINE_OPENAPI_SERVICE_KEY);

        System.out.println(uriString);
        URI uri = URI.create(uriString);

        VaccineRequestDTO vaccineRequestDTO = restTemplate.getForObject(uri, VaccineRequestDTO.class);
        VaccineRequestDataDTO vaccineRequestDataDTO = vaccineRequestDTO.getData().get(0);

        VaccineStatisticEntity vaccineStatisticEntity = new VaccineStatisticEntity();
        vaccineStatisticEntity.setDate(today);
        vaccineStatisticEntity.setTotalFirstCnt(vaccineRequestDataDTO.getTotalFirstCnt());
        vaccineStatisticEntity.setTotalSecondCnt(vaccineRequestDataDTO.getTotalSecondCnt());
        vaccineStatisticEntity.setExpectedMeetingDate(today.withYear(2099)); // expectedMeetingDate 선형회귀 등 알고리즘으로 변경 TODO

        return vaccineStatisticEntity;
    }
}
