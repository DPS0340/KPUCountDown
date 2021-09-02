package xyz.dps0340.kpucountdown.Scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.dps0340.kpucountdown.DTO.ExpectedMeetingDateDTO;
import xyz.dps0340.kpucountdown.Entity.ExpectedMeetingDateEntity;
import xyz.dps0340.kpucountdown.Repository.ExpectedMeetingDateRepository;
import xyz.dps0340.kpucountdown.Service.ExpectService;

@Component
public class ExpectedMeetingDateScheduler {
    @Autowired
    private ExpectService service;
    @Autowired
    private ExpectedMeetingDateRepository repository;

    @Scheduled(cron = "0 0 12 * * 0")
    public void cacheExpectedMeetingDate() {
        ExpectedMeetingDateDTO dto = service.calculateExpectedMeetingDate();
        ExpectedMeetingDateEntity entity = new ExpectedMeetingDateEntity(dto);

        if(repository.count() > 0) {
            repository.deleteAll();
        }

        // Service와의 Race Condition 방지
        if(repository.count() == 0) {
            repository.save(entity);
        }
    }
}
