package com.ttf.tallertornofumeroerp.service.impl;

import com.ttf.tallertornofumeroerp.exception.user.UserNotFoundException;
import com.ttf.tallertornofumeroerp.model.User;
import com.ttf.tallertornofumeroerp.repository.IUserRepository;
import com.ttf.tallertornofumeroerp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User retrieveUser(String user) {
        return userRepository.findById(user).orElse(null);
    }

    @Override
    public User createUser(User user) {
        User userDB = retrieveUser(user.getUserName());
        user.setCreationDate(new Date());
        return userDB == null ? userRepository.save(user) : userDB;
    }

    @Override
    public User updateUser(User user) {
        User userDB = retrieveUser(user.getUserName());
        if(userDB == null){
            return null;
        }
        userDB.setUserName(user.getUserName());
        userDB.setPassword(user.getPassword());
        userDB.setName(user.getName());
        userDB.setEmail(user.getEmail());
        userDB.setUserStatus(user.getUserStatus());
        userDB.setUpdateDate(new Date());
        return userRepository.save(userDB);
    }

    @Override
    public void deleteUser(String userName) {
        User userDB = retrieveUser(userName);
        if(userDB == null){
            throw new UserNotFoundException("Can't delete an non-existent user");
        }
        userRepository.delete(userDB);
    }
}
