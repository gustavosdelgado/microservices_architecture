package io.github.gustavosdelgado.microauthentication.domain.user;

import javax.validation.constraints.NotEmpty;

import io.github.gustavosdelgado.library.domain.user.Role;

public record AuthenticationRequest(@NotEmpty String login, @NotEmpty String password, Role role) {

}
