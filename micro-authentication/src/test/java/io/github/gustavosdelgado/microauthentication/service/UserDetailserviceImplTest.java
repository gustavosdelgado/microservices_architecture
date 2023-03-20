package io.github.gustavosdelgado.microauthentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.gustavosdelgado.microauthentication.domain.user.User;
import io.github.gustavosdelgado.microauthentication.domain.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailserviceImplTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserDetailserviceImpl service;

    @Test
    void givenValidUser_whenFindByLogin_thenSucceed() {
        User user = new User();
        var validUsername = "validUser";
        when(mockUserRepository.findByLogin(validUsername)).thenReturn(user);

        assertEquals(user, service.loadUserByUsername(validUsername), "not expected user");

        verify(mockUserRepository).findByLogin(validUsername);

        verifyNoMoreInteractions(mockUserRepository);
    }

}
