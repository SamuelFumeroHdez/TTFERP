package com.ttf.tallertornofumeroerp.service;

import com.ttf.tallertornofumeroerp.model.User;

import java.util.List;

public interface IUserService {

    List<User> retrieveAllUsers();
    User retrieveUser(String userName);
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(String userName);
}
