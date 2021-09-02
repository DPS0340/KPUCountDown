package xyz.dps0340.kpucountdown.Repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dps0340.kpucountdown.Entity.ExpectedMeetingDateEntity;
import xyz.dps0340.kpucountdown.Entity.VaccineStatisticEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpectedMeetingDateRepository extends CrudRepository<ExpectedMeetingDateEntity, Long> { }
