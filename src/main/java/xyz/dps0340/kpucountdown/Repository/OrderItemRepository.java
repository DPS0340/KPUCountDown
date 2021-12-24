package xyz.dps0340.kpucountdown.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.dps0340.kpucountdown.Entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

}
