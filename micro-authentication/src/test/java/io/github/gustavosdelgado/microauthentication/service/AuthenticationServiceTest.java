package io.github.gustavosdelgado.microauthentication.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import io.github.gustavosdelgado.library.service.AuthTokenService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager mockManager;

    @Mock
    private AuthTokenService mockTokenService;

    @Spy
    @InjectMocks
    private AuthenticationService service;

    // @Test
    // void testGenerateToken() {
    // AuthenticationRequest request = new AuthenticationRequest("login",
    // "password");
    // UsernamePasswordAuthenticationToken passwordToken = new
    // UsernamePasswordAuthenticationToken(request, request);
    // doReturn(passwordToken).when(service).createUsernamePasswordAuthToken(request);
    // doReturn("jwt").when(service).generateJwt(passwordToken);
    // assertEquals("jwt", service.generateToken(request), "not expected return");
    // }

}
