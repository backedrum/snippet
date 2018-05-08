package com.backedrum.service;

import com.backedrum.model.User;
import com.backedrum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Named;

@Service
@Named("userService")
public class UserServiceImpl {

    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public User findUser(String username) {
        return repository.findUser(username);
    }
}
