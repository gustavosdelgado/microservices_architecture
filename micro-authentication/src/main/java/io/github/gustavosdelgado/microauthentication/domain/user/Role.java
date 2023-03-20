package io.github.gustavosdelgado.microauthentication.domain.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_RESTAURANT, ROLE_CONSUMER;

    @Override
    public String getAuthority() {
        return name();
    }

}
