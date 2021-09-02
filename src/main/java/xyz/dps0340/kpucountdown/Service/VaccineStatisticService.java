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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VaccineStatisticService {
    @Autowired
    VaccineStatisticRepository vaccineStatisticRepository;


    public VaccineStatisticDTO getTodayStat() {
        LocalDateTime today = LocalDate.now().atTime(0, 0, 0, 0);
        VaccineStatisticEntity vaccineStatisticEntity = vaccineStatisticRepository.findByDate(today).get();

        return new VaccineStatisticDTO(vaccineStatisticEntity);
    }

    public List<VaccineStatisticDTO> getStats() {
        List<VaccineStatisticDTO> dtos = new ArrayList<>();
        vaccineStatisticRepository
                .findAllByOrderByDateAsc()
                .iterator()
                .forEachRemaining(e -> {
                    VaccineStatisticDTO dto = new VaccineStatisticDTO(e);
                    dtos.add(dto);
                });
        return dtos;
    }

    public List<VaccineStatisticEntity> crawlStatsFromServer() {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .toFormatter();
        LocalDateTime initialDate = LocalDateTime.parse("2018-01-01 00:00:00", dateTimeFormatter);

        return crawlStatsFromServer(initialDate);
    }

    public VaccineStatisticEntity crawlTodayStatFromServer() {
        LocalDateTime today = LocalDate.now().atTime(0, 0, 0, 0);
        List<VaccineStatisticEntity> todayStats = crawlStatsFromServer(today);
        if(todayStats.size() != 1) {
            throw new IllegalStateException(String.format("Today's stat size must be 1, but it actually %d", todayStats.size()));
        }
        return todayStats.get(0);
    }

    public List<VaccineStatisticEntity> crawlStatsFromServer(LocalDateTime after) {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                                            .appendPattern("yyyy-MM-dd HH:mm:ss")
                                            .toFormatter();
        String dateString = after.format(dateTimeFormatter);

        RestTemplate restTemplate = new RestTemplate();

        String uriString = UriComponentsBuilder
                .fromUriString(GlobalVariable.OPENAPI_ENDPOINT)
                .queryParam("page", 1)
                .queryParam("perPage", 100000)
                .queryParam("cond[baseDate::GTE]", dateString)
                .queryParam("cond[sido::EQ]", "전국")
                .encode()
                .build()
                .toUriString() + String.format("&serviceKey=%s", GlobalVariable.VACCINE_OPENAPI_SERVICE_KEY);

        URI uri = URI.create(uriString);

        VaccineRequestDTO vaccineRequestDTO = restTemplate.getForObject(uri, VaccineRequestDTO.class);
        List<VaccineRequestDataDTO> vaccineRequestData = vaccineRequestDTO.getData();

        List<VaccineStatisticEntity> entities = vaccineRequestData.stream().map(dto -> {
            VaccineStatisticEntity vaccineStatisticEntity = new VaccineStatisticEntity();

            LocalDateTime foundDateTime = LocalDateTime.parse(dto.getBaseDate(), dateTimeFormatter);
            vaccineStatisticEntity.setDate(foundDateTime);
            vaccineStatisticEntity.setTotalFirstCnt(dto.getTotalFirstCnt());
            vaccineStatisticEntity.setTotalSecondCnt(dto.getTotalSecondCnt());
            vaccineStatisticEntity.setExpectedMeetingDate(after.withYear(2099)); // expectedMeetingDate Ridge등 알고리즘으로 변경 TODO

            return vaccineStatisticEntity;
        })
        .collect(Collectors.toList());

        return entities;
    }
}
