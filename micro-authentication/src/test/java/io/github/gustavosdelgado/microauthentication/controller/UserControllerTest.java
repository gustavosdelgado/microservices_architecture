package io.github.gustavosdelgado.microauthentication.controller;

import io.github.gustavosdelgado.library.domain.user.Role;
import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import io.github.gustavosdelgado.microauthentication.domain.user.JwtData;
import io.github.gustavosdelgado.microauthentication.service.AuthenticationService;
import io.github.gustavosdelgado.microauthentication.service.UserDetailserviceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.Valid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserDetailserviceImpl mockUserService;

    @Mock
    private AuthenticationService mockAuthenticationService;

    @InjectMocks
    private UserController controller;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockUserService, mockAuthenticationService);
    }

    @Test
    void givenValidRequestWhenCreateUserThenSucceeds() throws BadRequestException {
        AuthenticationRequest request = new AuthenticationRequest("login", "password", Role.ROLE_CONSUMER);
        when(mockAuthenticationService.generateToken(request)).thenReturn("jwt");
        ResponseEntity<JwtData> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(mockUserService).create(request);
        verify(mockAuthenticationService).generateToken(request);
    }

    @Test
    void givenInvalidRequestWhenCreateUserThenThrowsBadRequestException() throws BadRequestException {
        AuthenticationRequest request = new AuthenticationRequest("login", "password", Role.ROLE_CONSUMER);
        when(mockAuthenticationService.generateToken(request)).thenThrow(BadRequestException.class);
        ResponseEntity<JwtData> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(mockUserService).create(request);
    }

    @Test
    void givenInvalidRequestWhenCreateUserThenThrowsException() throws BadRequestException {
        AuthenticationRequest request = new AuthenticationRequest("login", "password", Role.ROLE_CONSUMER);
        when(mockAuthenticationService.generateToken(request)).thenThrow(RuntimeException.class);
        ResponseEntity<JwtData> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        verify(mockUserService).create(request);
    }

}