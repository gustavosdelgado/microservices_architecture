package io.github.gustavosdelgado.microauthentication.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.github.gustavosdelgado.microauthentication.domain.user.Role;
import io.github.gustavosdelgado.microauthentication.domain.user.User;

@ExtendWith(MockitoExtension.class)
public class AuthTokenServiceTest {

    @InjectMocks
    private AuthTokenService service;

    @Test
    void givenValidUserWhenGenerateTokenThenSucceed() {
        User user = new User(1L, "login", "password", Role.ROLE_CONSUMER);
        ReflectionTestUtils.setField(service, "secret", "secret");
        ReflectionTestUtils.setField(service, "expirationInMinutes", 15);
        assertNotNull(service.gerarToken(user), "Unexpected null return");
    }

    @Test
    void givenNullUserWhenGenerateTokenThenFail() {
        ReflectionTestUtils.setField(service, "secret", "secret");
        assertThrows(RuntimeException.class,
                () -> service.gerarToken(null), "Expected Exception not thrown");
    }
}
