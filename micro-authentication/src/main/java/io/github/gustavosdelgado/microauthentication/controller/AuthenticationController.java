package io.github.gustavosdelgado.microauthentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import io.github.gustavosdelgado.microauthentication.domain.user.JwtData;
import io.github.gustavosdelgado.microauthentication.domain.user.User;
import io.github.gustavosdelgado.microauthentication.service.AuthTokenService;
import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private AuthTokenService tokenService;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtData> authenticate(@RequestBody @Valid AuthenticationRequest request) {

        final Logger logger = LoggerFactory.getLogger(getClass());

        try {
            var token = new UsernamePasswordAuthenticationToken(request.login(), request.password());
            var authentication = manager.authenticate(token);
    
            var jwt = tokenService.gerarToken((User) authentication.getPrincipal());
    
            return ResponseEntity.ok(new JwtData(jwt));
        } catch (Exception e) {
            logger.error("Authentication failure: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
