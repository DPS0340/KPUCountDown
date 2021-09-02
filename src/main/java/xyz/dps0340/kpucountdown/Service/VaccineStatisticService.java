package xyz.dps0340.kpucountdown.Service;


import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
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

import java.io.IOException;
import java.net.URI;
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
    private VaccineStatisticRepository repository;


    public VaccineStatisticDTO getTodayStat() {
        LocalDateTime today = LocalDate.now().atTime(0, 0, 0, 0);
        VaccineStatisticEntity entity = repository.findByDate(today).get();

        return new VaccineStatisticDTO(entity);
    }

    public List<VaccineStatisticDTO> getStats() {
        List<VaccineStatisticDTO> dtos = new ArrayList<>();
        repository
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

        VaccineRequestDTO requestDTO = restTemplate.getForObject(uri, VaccineRequestDTO.class);
        List<VaccineRequestDataDTO> data = requestDTO.getData();

        List<VaccineStatisticEntity> entities = data.stream().map(dto -> {
            VaccineStatisticEntity entity = new VaccineStatisticEntity();

            LocalDateTime foundDateTime = LocalDateTime.parse(dto.getBaseDate(), dateTimeFormatter);
            entity.setDate(foundDateTime);
            entity.setTotalFirstCnt(dto.getTotalFirstCnt());
            entity.setTotalSecondCnt(dto.getTotalSecondCnt());

            return entity;
        })
        .collect(Collectors.toList());

        return entities;
    }

    public byte[] getVaccineStatsGraph() {
        List<VaccineStatisticDTO> vaccineStats = getStats();
        List<Double> firstRatios = vaccineStats
                .stream()
                .map(VaccineStatisticDTO::getFirstRatio)
                .collect(Collectors.toList());
        List<Double> secondRatios = vaccineStats
                .stream()
                .map(VaccineStatisticDTO::getSecondRatio)
                .collect(Collectors.toList());

        final XYChart chart = new XYChartBuilder()
                .width(1200).height(800).title("Area Chart").xAxisTitle("Day After").yAxisTitle("Ratio").build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

        if(firstRatios.size() > 0) {
            chart.addSeries("First Vaccinated", firstRatios.stream().mapToDouble(Double::doubleValue).toArray());
        }
        if(secondRatios.size() > 0) {
            chart.addSeries("Second Vaccinated", secondRatios.stream().mapToDouble(Double::doubleValue).toArray());
        }

        byte[] result = null;

        try {
            result = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException ignored) {

        }

        return result;
    }
}
