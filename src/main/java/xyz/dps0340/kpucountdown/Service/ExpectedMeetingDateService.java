package xyz.dps0340.kpucountdown.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.dps0340.kpucountdown.DTO.ExpectedMeetingDateDTO;
import xyz.dps0340.kpucountdown.Entity.ExpectedMeetingDateEntity;
import xyz.dps0340.kpucountdown.Repository.ExpectedMeetingDateRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpectedMeetingDateService {
    @Autowired
    private ExpectedMeetingDateRepository repository;

    public ExpectedMeetingDateDTO getDate() {
        List<ExpectedMeetingDateDTO> dates = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(e -> {
            ExpectedMeetingDateDTO dto = new ExpectedMeetingDateDTO(e);
            dates.add(dto);
        });
        int length = dates.size();
        if (length == 0) {
            ExpectedMeetingDateDTO calculated = calculateExpectedMeetingDate();
            ExpectedMeetingDateEntity entity = new ExpectedMeetingDateEntity();
            entity.setDate(calculated.getDate());

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

    public ExpectedMeetingDateDTO calculateExpectedMeetingDate() {
        // Machine learning with MultiLayerRegressionModel TODO

        return null;
    }
}
