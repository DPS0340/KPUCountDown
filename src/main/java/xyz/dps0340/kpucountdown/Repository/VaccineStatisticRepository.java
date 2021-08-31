package xyz.dps0340.kpucountdown.Repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface VaccineStatisticRepository extends CrudRepository<VaccineStatisticEntity, Long> {
    Optional<VaccineStatisticEntity> findByDate(LocalDateTime date);

}
