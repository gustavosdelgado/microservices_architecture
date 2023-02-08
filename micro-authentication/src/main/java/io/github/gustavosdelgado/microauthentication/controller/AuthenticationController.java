package io.github.gustavosdelgado.microauthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import io.github.gustavosdelgado.microauthentication.domain.user.JwtData;
import io.github.gustavosdelgado.microauthentication.domain.user.User;
import io.github.gustavosdelgado.microauthentication.service.TokenService;
import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtData> authenticate(@RequestBody @Valid AuthenticationRequest request) {

        var token = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        var authentication = manager.authenticate(token);

        var jwt = tokenService.gerarToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new JwtData(jwt));
    }

}