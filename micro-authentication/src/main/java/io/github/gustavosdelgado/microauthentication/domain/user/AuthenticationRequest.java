package io.github.gustavosdelgado.microauthentication.domain.user;

import javax.validation.constraints.NotEmpty;

public record AuthenticationRequest(@NotEmpty String login, @NotEmpty String password, Role role) {

}
