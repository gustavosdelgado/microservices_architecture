package io.github.gustavosdelgado.microauthentication.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import io.github.gustavosdelgado.microauthentication.domain.user.User;
import io.github.gustavosdelgado.microauthentication.domain.user.UserRepository;
import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody @Valid AuthenticationRequest request) {

        try {
            var token = new UsernamePasswordAuthenticationToken(request.login(), request.password());
            var authentication = manager.authenticate(token);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("Falha ao autenticar", e);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUser() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

}
