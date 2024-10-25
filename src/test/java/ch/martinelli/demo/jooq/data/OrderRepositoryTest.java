package ch.martinelli.demo.jooq.data;

import ch.martinelli.demo.jooq.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestConfiguration.class)
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findOrders() {
        List<PurchaseOrder> orders = orderRepository.findOrders("%", "%", 0, 100);

        assertThat(orders).hasSize(100);
    }
}