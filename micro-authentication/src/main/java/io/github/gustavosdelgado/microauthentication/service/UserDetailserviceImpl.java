package io.github.gustavosdelgado.microauthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import io.github.gustavosdelgado.microauthentication.domain.user.User;
import io.github.gustavosdelgado.microauthentication.domain.user.UserRepository;
import io.github.gustavosdelgado.microauthentication.exception.BadRequestException;

@Service
public class UserDetailserviceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }

    public void create(AuthenticationRequest request) throws BadRequestException {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        User user = new User(request.login(), bcrypt.encode(request.password()), request.role());
        try {
            repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e);

        }
    }

}
