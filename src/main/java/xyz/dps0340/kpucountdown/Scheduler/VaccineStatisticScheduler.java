package xyz.dps0340.kpucountdown.Scheduler;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;
import xyz.dps0340.kpucountdown.Repository.VaccineStatisticRepository;
import xyz.dps0340.kpucountdown.Service.VaccineStatisticService;

@Component
public class VaccineStatisticScheduler {
    @Autowired
    VaccineStatisticService vaccineStatisticService;
    @Autowired
    VaccineStatisticRepository vaccineStatisticRepository;

    @Scheduled(cron = "0 0 11 * * 0")
    @EventListener(ApplicationReadyEvent.class)
    public void crawlStatistic() {
        VaccineStatisticEntity vaccineStatisticEntity = vaccineStatisticService.crawlTodayStatsFromServer();
        if(vaccineStatisticRepository.findByDate(vaccineStatisticEntity.getDate()).isPresent()) {
            return;
        }
        vaccineStatisticRepository.save(vaccineStatisticEntity);
    }
}
