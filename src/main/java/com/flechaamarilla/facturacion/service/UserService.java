package com.flechaamarilla.facturacion.service;

import com.flechaamarilla.facturacion.model.User;
import com.flechaamarilla.facturacion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Optional<User> getUser(String rfc){
        return userRepository.findById(rfc);
    }

    public void postUser(User usuario){
        userRepository.save(usuario);
    }
    public void deleteUser(String rfc){
        userRepository.deleteById(rfc);
    }


}
