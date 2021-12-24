package xyz.dps0340.kpucountdown.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.dps0340.kpucountdown.Entity.OrderEntity;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, OrderRepositoryCustom {

}