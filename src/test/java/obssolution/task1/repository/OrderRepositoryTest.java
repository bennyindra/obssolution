package obssolution.task1.repository;

import obssolution.task1.entity.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderRepositoryTest {
    private final OrderRepository orderRepository;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderRepositoryTest.class);

    OrderRepositoryTest(@Autowired OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Test
    @org.junit.jupiter.api.Order(0)
    void shouldGenerateOrderNoWithPrefixO() {
        // Given
        Order order1 = new Order();
        order1.setQuantity(10);

        Order order2 = new Order();
        order2.setQuantity(5);

        // When
        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);

        // Then
        assertThat(savedOrder1.getOrderNo()).isEqualTo("O1");
        assertThat(savedOrder2.getOrderNo()).isEqualTo("O2");

        log.info("Generated ID 1: {}", savedOrder1.getOrderNo());
        log.info("Generated ID 1: {}", savedOrder2.getOrderNo());

    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void shouldFindOrderByOrderNo() {
        assertThatNoException().isThrownBy(() -> orderRepository.findByOrderNo("01"));
    }
}