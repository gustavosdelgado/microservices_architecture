package io.github.gustavosdelgado.microauthentication.domain.user;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(@NotNull String login, @NotNull String password) {

}
