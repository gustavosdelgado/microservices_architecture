package io.github.gustavosdelgado.microauthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.microauthentication.domain.user.User;
import io.github.gustavosdelgado.microauthentication.domain.user.UserRepository;

@Service
public class UserDetailserviceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }

}
