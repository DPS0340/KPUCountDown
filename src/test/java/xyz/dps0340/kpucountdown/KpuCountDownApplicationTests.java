package xyz.dps0340.kpucountdown;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.dps0340.kpucountdown.Entity.OrderEntity;
import xyz.dps0340.kpucountdown.Entity.OrderItemEntity;
import xyz.dps0340.kpucountdown.Repository.OrderItemRepository;
import xyz.dps0340.kpucountdown.Repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class KpuCountDownApplicationTests {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    void testFindAll() {
        List<OrderEntity> orders = Stream.generate(() -> {
            List<OrderItemEntity> relatedItems = Stream.generate(OrderItemEntity::new).limit(5).collect(Collectors.toList());
            orderItemRepository.saveAll(relatedItems);

            OrderEntity order = new OrderEntity();
            order.setOrderItems(relatedItems);

            return order;
        }).limit(5).collect(Collectors.toList());

        orderRepository.saveAll(orders);

        System.out.println("–––QUERIES START---");
        orders = orderRepository.getAll();
        orders.stream().forEach(e -> System.out.println(e.toString()));
        System.out.println("–––QUERIES END---");
    }

}
