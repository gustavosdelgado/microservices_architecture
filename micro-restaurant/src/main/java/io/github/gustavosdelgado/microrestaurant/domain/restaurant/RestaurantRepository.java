package io.github.gustavosdelgado.microrestaurant.domain.restaurant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findbyIdAndUser(Long id, String user);

}
