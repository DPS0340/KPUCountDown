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
    private VaccineStatisticService vaccineStatisticService;
    @Autowired
    private VaccineStatisticRepository vaccineStatisticRepository;

    @Scheduled(cron = "0 0 11 * * 0")
    @EventListener(ApplicationReadyEvent.class)
    public void crawlTodayStatistic() {
        VaccineStatisticEntity vaccineStatisticEntity = vaccineStatisticService.crawlTodayStatFromServer();
        if(vaccineStatisticRepository.findByDate(vaccineStatisticEntity.getDate()).isPresent()) {
            return;
        }
        vaccineStatisticRepository.save(vaccineStatisticEntity);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void crawlHistoryStatistic() {
        List<VaccineStatisticEntity> vaccineStatisticEntities = vaccineStatisticService.crawlStatsFromServer();
        vaccineStatisticEntities
                .stream()
                .filter(entity -> vaccineStatisticRepository.findByDate(entity.getDate()).isEmpty())
                .forEach(vaccineStatisticRepository::save);
    }
}
