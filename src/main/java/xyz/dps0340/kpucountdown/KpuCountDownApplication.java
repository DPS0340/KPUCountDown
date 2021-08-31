package xyz.dps0340.kpucountdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.dps0340.kpucountdown.Scheduler.VaccineStatisticScheduler;

@SpringBootApplication
public class KpuCountDownApplication {

    public static void main(String[] args) {
        SpringApplication.run(KpuCountDownApplication.class, args);
    }

}
