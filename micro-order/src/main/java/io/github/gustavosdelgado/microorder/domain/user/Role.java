package io.github.gustavosdelgado.microorder.domain.user;

public enum Role {

    ROLE_RESTAURANT, ROLE_CONSUMER;

    public String getAuthority() {
        return name();
    }

}
