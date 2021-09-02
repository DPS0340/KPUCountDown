package xyz.dps0340.kpucountdown.Scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;
import xyz.dps0340.kpucountdown.Repository.VaccineStatisticRepository;
import xyz.dps0340.kpucountdown.Service.VaccineStatisticService;

import java.util.List;

@Component
public class VaccineStatisticScheduler {
    @Autowired
    private VaccineStatisticService service;
    @Autowired
    private VaccineStatisticRepository repository;
    @Autowired
    private ExpectedMeetingDateScheduler expectedMeetingDateScheduler;

    @Scheduled(cron = "0 0 11 * * 0")
    public void crawlTodayStatistic() {
        VaccineStatisticEntity entity = service.crawlTodayStatFromServer();
        if(repository.findByDate(entity.getDate()).isPresent()) {
            return;
        }
        repository.save(entity);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doJobs() {
        crawlHistoryStatistic();
        expectedMeetingDateScheduler.cacheExpectedMeetingDate();
    }


    public void crawlHistoryStatistic() {
        List<VaccineStatisticEntity> entities = service.crawlStatsFromServer();
        entities
            .stream()
            .filter(entity -> repository.findByDate(entity.getDate()).isEmpty())
            .forEach(repository::save);
    }
}
