package io.github.gustavosdelgado.microauthentication.controller;

import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import io.github.gustavosdelgado.microauthentication.domain.user.JwtData;
import io.github.gustavosdelgado.microauthentication.service.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    AuthenticationService mockService;

    @InjectMocks
    private AuthenticationController controller;

    @AfterEach
    void teardown() {
        verifyNoMoreInteractions(mockService);
    }

    @Test
    void givenValidRequestWhenAuthenticateThenSucceeds() throws BadRequestException {
        AuthenticationRequest request = new AuthenticationRequest("login", "password", null);
        when(mockService.generateToken(request))
                .thenReturn("jwt");

        ResponseEntity<JwtData> response = controller.authenticate(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).token()).isEqualTo("jwt");

        verify(mockService).generateToken(request);
    }

    @Test
    void givenInvalidRequestWhenAuthenticateThenThrowsBadRequestException() throws BadRequestException {
        AuthenticationRequest request = new AuthenticationRequest("login", "password", null);
        when(mockService.generateToken(request)).thenThrow(BadRequestException.class);

        ResponseEntity<JwtData> response = controller.authenticate(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(mockService).generateToken(request);
    }
    @Test
    void givenInvalidRequestWhenAuthenticateThenThrowsException() throws BadRequestException {
        AuthenticationRequest request = new AuthenticationRequest("login", "password", null);
        when(mockService.generateToken(request)).thenThrow(RuntimeException.class);

        ResponseEntity<JwtData> response = controller.authenticate(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        verify(mockService).generateToken(request);
    }

}