package io.github.gustavosdelgado.microorder.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.github.gustavosdelgado.controller.EntityDomain;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "pedido")
@Entity(name = "Pedido")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Order implements EntityDomain {

    public Order(Long orderId, Long restaurantId) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long restaurantId;

    @Override
    public OrderWebResponse adaptToWebResponse() {
        return new OrderWebResponse(id, orderId, restaurantId);
    }

}
