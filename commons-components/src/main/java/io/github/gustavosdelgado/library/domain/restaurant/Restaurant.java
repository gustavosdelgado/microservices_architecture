package io.github.gustavosdelgado.library.domain.restaurant;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

    public Restaurant(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany
    @Column(nullable = false)
    private List<User> users;

    @OneToMany
    @Column(nullable = false)
    private List<Order> orders;

}
