package xyz.dps0340.kpucountdown.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.dps0340.kpucountdown.Entity.OrderEntity;
import xyz.dps0340.kpucountdown.Entity.QOrderEntity;
import xyz.dps0340.kpucountdown.Entity.QOrderItemEntity;

import java.util.List;

public class OrderRepositoryImpl implements OrderRepositoryCustom {
    @Autowired
    public JPAQueryFactory jpaQueryFactory;

    public List<OrderEntity> getAll() {
        return jpaQueryFactory
                .selectFrom(QOrderEntity.orderEntity)
                .innerJoin(QOrderEntity.orderEntity.orderItems, QOrderItemEntity.orderItemEntity)
                .fetchJoin()
                .fetch();
    }
}
