package io.github.gustavosdelgado.microauthentication.service;

import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.library.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.library.service.AuthTokenService;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private AuthTokenService tokenService;

    public String generateToken(AuthenticationRequest request) throws BadRequestException {
        try {
            var token = createUsernamePasswordAuthToken(request);
            return generateJwt(token);
        } catch (NoDataFoundException | AuthenticationException e) {
            throw new BadRequestException(e);
        }
    }

    protected UsernamePasswordAuthenticationToken createUsernamePasswordAuthToken(AuthenticationRequest request) {
        return new UsernamePasswordAuthenticationToken(request.login(), request.password());
    }

    protected String generateJwt(UsernamePasswordAuthenticationToken token) throws NoDataFoundException {
        Authentication auth = manager.authenticate(token);
        return tokenService.generateToken(auth.getPrincipal());
    }

}
