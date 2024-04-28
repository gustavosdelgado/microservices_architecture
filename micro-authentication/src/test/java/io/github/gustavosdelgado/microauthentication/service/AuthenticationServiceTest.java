package io.github.gustavosdelgado.microauthentication.service;

import io.github.gustavosdelgado.library.domain.user.Role;
import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.library.exception.NoDataFoundException;
import io.github.gustavosdelgado.library.service.AuthTokenService;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager mockManager;

    @Mock
    private AuthTokenService mockTokenService;

    @Spy
    @InjectMocks
    private AuthenticationService service;

    @Test
    void givenValidRequestWhenGenerateTokenThenSucceeds() throws BadRequestException, NoDataFoundException {
        AuthenticationRequest request = new AuthenticationRequest("login",
                "password", Role.ROLE_CONSUMER);
        UsernamePasswordAuthenticationToken passwordToken = new UsernamePasswordAuthenticationToken(request, request);
        doReturn(passwordToken).when(service).createUsernamePasswordAuthToken(request);
        doReturn("jwt").when(service).generateJwt(passwordToken);
        assertEquals("jwt", service.generateToken(request), "not expected return");
    }

    @Test
    void givenInvalidRequestWhenGenerateTokenThenThrowsAuthenticationException() {
        AuthenticationRequest request = new AuthenticationRequest("login",
                "password", Role.ROLE_CONSUMER);
        doThrow(InternalAuthenticationServiceException.class).when(service).createUsernamePasswordAuthToken(request);
        assertThrows(BadRequestException.class, () -> service.generateToken(request));
    }
    @Test
    void givenInvalidRequestWhenGenerateTokenThenThrowsNoDataFoundException() throws NoDataFoundException {
        AuthenticationRequest request = new AuthenticationRequest("login",
                "password", Role.ROLE_CONSUMER);
        UsernamePasswordAuthenticationToken passwordToken = new UsernamePasswordAuthenticationToken(request, request);
        Authentication auth = new TestingAuthenticationToken(passwordToken.getPrincipal(), passwordToken.getCredentials());
        when(mockManager.authenticate(any())).thenReturn(auth);
        doThrow(NoDataFoundException.class).when(mockTokenService).generateToken(any());
        assertThrows(BadRequestException.class, () -> service.generateToken(request));
    }

}
