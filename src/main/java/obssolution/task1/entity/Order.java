package obssolution.task1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import obssolution.task1.annotations.OrderNoId;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Data
public class Order implements Serializable {

    @Id
    @Column(name = "order_no")
    @OrderNoId
    private String orderNo;

    @Column(name = "qty")
    private int quantity;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "item_id_snapshot")
    private Long itemIdSnapshot;

}
