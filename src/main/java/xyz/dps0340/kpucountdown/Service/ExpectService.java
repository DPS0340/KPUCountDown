package xyz.dps0340.kpucountdown.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.dps0340.kpucountdown.DTO.InferenceDTO;
import xyz.dps0340.kpucountdown.DTO.ExpectedMeetingDateDTO;
import xyz.dps0340.kpucountdown.DTO.VaccineStatisticDTO;
import xyz.dps0340.kpucountdown.Entity.ExpectedMeetingDateEntity;
import xyz.dps0340.kpucountdown.Model.MultiLayerRegressionModel;
import xyz.dps0340.kpucountdown.Repository.ExpectedMeetingDateRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpectService {
    @Autowired
    private ExpectedMeetingDateRepository repository;
    @Autowired
    private VaccineStatisticService vaccineStatisticService;

    private final MultiLayerRegressionModel multiLayerRegressionModel = new MultiLayerRegressionModel();

    public ExpectedMeetingDateDTO getDate() {
        List<ExpectedMeetingDateDTO> dates = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(e -> {
            ExpectedMeetingDateDTO dto = new ExpectedMeetingDateDTO(e);
            dates.add(dto);
        });
        int length = dates.size();
        if (length == 0) {
            ExpectedMeetingDateDTO calculated = calculateExpectedMeetingDate();
            ExpectedMeetingDateEntity entity = new ExpectedMeetingDateEntity(calculated);

            // Scheduler와의 Race Condition 방지
            length = (int) repository.count();
            if(length == 0) {
                repository.save(entity);
            }

            dates.add(calculated);
        } else if(length > 1) {
            throw new IllegalStateException(
                    String.format(
                            "ExpectedMeetingDateEntity must only one exists, but it actually %d exist",
                            length));
        }
        ExpectedMeetingDateDTO result = dates.get(0);
        return result;
    }

    private InferenceDTO inference() {
        long count = vaccineStatisticService.getStatsCount();

        int idx = (int) count;
        double ratio = 0.0;
        List<Double> expectations = new ArrayList<>();

        while(ratio < 70.0) {
            idx += 1;

            ArrayList<Integer> x = new ArrayList<>();
            x.add(idx);

            List<Double> y = multiLayerRegressionModel.inference(x);

            if(y.size() != 1) {
                throw new IllegalStateException(String.format("Expectation's list size must be 1, but it actually %d", y.size()));
            }

            ratio = y.get(0);
            expectations.add(ratio);
        }

        return new InferenceDTO(idx, (int) count, expectations);
    }

    public ExpectedMeetingDateDTO calculateExpectedMeetingDate() {

        if(!multiLayerRegressionModel.isInitialized()) {
            multiLayerRegressionModel.initializeNeuralNetwork();
        }
        if(!multiLayerRegressionModel.isTrained()) {
            List<Double> firstRatios = vaccineStatisticService.getStats().stream().map(VaccineStatisticDTO::getFirstRatio).collect(Collectors.toList());
            multiLayerRegressionModel.train(firstRatios, MultiLayerRegressionModel.DEFAULT_BATCH_SIZE);
        }

        InferenceDTO inferenceDTO = inference();

        int idx = inferenceDTO.getIdx();
        int count = inferenceDTO.getCount();

        LocalDateTime today = vaccineStatisticService.getTodayStat().getDate();
        LocalDateTime expectedDate = today.plusDays(idx - count);

        ExpectedMeetingDateDTO result = new ExpectedMeetingDateDTO(expectedDate);

        return result;
    }

    public byte[] getExpectedGraph() {
        List<Double> firstRatios = vaccineStatisticService
                .getStats()
                .stream()
                .map(VaccineStatisticDTO::getFirstRatio)
                .collect(Collectors.toList());

        InferenceDTO inferenceDTO = inference();
        List<Double> expectations = inferenceDTO.getExpectations();
        firstRatios.addAll(expectations);

        List<List<Double>> lists = new ArrayList<>();
        lists.add(firstRatios);

        List<String> labels = new ArrayList<>();
        labels.add("First Vaccinated (including inference)");

        return vaccineStatisticService.drawGraph(lists, labels);
    }
}
