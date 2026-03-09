package obssolution.task1.repository;

import obssolution.task1.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByOrderNo(String orderNo);
    Page<Order> findAllByOrOrderNo(String orderNo, Pageable pageable);
    void deleteByOrderNo(String orderNo);
}
