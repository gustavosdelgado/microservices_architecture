package io.github.gustavosdelgado.microauthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody @Valid AuthenticationRequest request) {

        var token = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        var authentication = manager.authenticate(token);

        return ResponseEntity.ok().build();
    }
}
