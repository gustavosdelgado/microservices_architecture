package io.github.gustavosdelgado.microauthentication.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.library.domain.user.User;
import io.github.gustavosdelgado.library.domain.user.UserRepository;
import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;

@Service
public class UserDetailserviceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }

    public void create(AuthenticationRequest request) throws BadRequestException {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        User user = new User(request.login(), bcrypt.encode(request.password()), request.role());
        try {
            user = repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e);

        }

        rabbitTemplate.convertAndSend("user.exchange", "", user);
    }

}
