package io.github.gustavosdelgado.microauthentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager mockManager;

    @Mock
    private AuthTokenService mockTokenService;

    @Spy
    @InjectMocks
    private AuthenticationService service;

    @Test
    void testGenerateToken() {
        AuthenticationRequest request = new AuthenticationRequest("login", "password");
        UsernamePasswordAuthenticationToken passwordToken = new UsernamePasswordAuthenticationToken(request, request);
        doReturn(passwordToken).when(service).createUsernamePasswordAuthToken(request);
        doReturn("jwt").when(service).generateJwt(passwordToken);
        assertEquals("jwt", service.generateToken(request));
    }

}
