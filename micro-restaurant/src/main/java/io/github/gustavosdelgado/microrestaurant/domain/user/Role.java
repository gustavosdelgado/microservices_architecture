package io.github.gustavosdelgado.microrestaurant.domain.user;

public enum Role {

    ROLE_RESTAURANT, ROLE_CONSUMER;

    public String getAuthority() {
        return name();
    }

}
