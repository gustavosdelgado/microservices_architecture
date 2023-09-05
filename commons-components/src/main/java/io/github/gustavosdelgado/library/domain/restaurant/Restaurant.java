package io.github.gustavosdelgado.library.domain.restaurant;

import java.util.List;

import javax.persistence.*;

import io.github.gustavosdelgado.library.domain.order.Order;
import io.github.gustavosdelgado.library.domain.user.User;
import lombok.*;

@Table(name = "restaurant")
@Entity(name = "Restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Restaurant {

    public Restaurant(String name, User user) {
        this.name = name;
        this.user = user;
    }

    @Id
    @Column(name = "restaurant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToOne(optional = false)
    private User user;

    @OneToMany(mappedBy = "restaurant")
    @Column(nullable = true)
    private List<Order> orders;

}
