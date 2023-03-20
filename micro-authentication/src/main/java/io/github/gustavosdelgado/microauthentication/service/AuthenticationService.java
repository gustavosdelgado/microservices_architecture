package io.github.gustavosdelgado.microauthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.library.domain.user.User;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private AuthTokenService tokenService;

    public String generateToken(AuthenticationRequest request) {
        var token = createUsernamePasswordAuthToken(request);
        return generateJwt(token);
    }

    protected UsernamePasswordAuthenticationToken createUsernamePasswordAuthToken(AuthenticationRequest request) {
        return new UsernamePasswordAuthenticationToken(request.login(), request.password());
    }

    protected String generateJwt(UsernamePasswordAuthenticationToken token) {
        Authentication auth = manager.authenticate(token);
        return tokenService.gerarToken((User) auth.getPrincipal());
    }

}
