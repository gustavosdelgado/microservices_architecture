package io.github.gustavosdelgado.microauthentication.controller;

import javax.validation.Valid;

import io.github.gustavosdelgado.library.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import io.github.gustavosdelgado.microauthentication.domain.user.JwtData;
import io.github.gustavosdelgado.microauthentication.service.AuthenticationService;

@RestController
public class AuthenticationController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtData> authenticate(@RequestBody @Valid AuthenticationRequest request) {

        try {
            String jwt = authService.generateToken(request);
    
            return ResponseEntity.ok(new JwtData(jwt));
        } catch (BadRequestException e) {
            logger.error("Authentication failure: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Authentication failure: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
