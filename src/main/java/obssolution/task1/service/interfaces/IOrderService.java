package obssolution.task1.service.interfaces;

import obssolution.task1.entity.Order;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.OrderRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {

    public Order getByOrderNo(String orderNo) throws BusinessException;

    public Page<Order> findAll(Pageable pageable) ;

    public Order save(OrderRequestDto order) throws BusinessException;

    public Order update(String id, OrderRequestDto order) throws BusinessException;

    public void delete(String orderNo) throws BusinessException;
}
