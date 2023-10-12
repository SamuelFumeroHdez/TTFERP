package com.ttf.tallertornofumeroerp.controller;

import com.ttf.tallertornofumeroerp.exception.user.UserNotFoundException;
import com.ttf.tallertornofumeroerp.model.User;
import com.ttf.tallertornofumeroerp.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(
            value = "/",
            produces = "application/json")
    public ResponseEntity<List<User>> retrieveAllUsers(){
        List<User> userList = userService.retrieveAllUsers();
        return userList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(userList);
    }

    @GetMapping(
            value = "/{user}",
            produces = "application/json")
    public ResponseEntity<User> retrieveUser(@PathVariable("user") String user){
        User userFound = userService.retrieveUser(user);
        return userFound == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(userFound);
    }

    @PostMapping(
            value = "/create",
            produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User userCreated = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping(value = "/{user}")
    public ResponseEntity<User> updateUser(@PathVariable("user") String userName, @RequestBody User user){
        User userDB = userService.updateUser(user);
        return userDB == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @DeleteMapping(value = "/{user}")
    public void deleteUser(@PathVariable("user") String userName) throws UserNotFoundException {
        userService.deleteUser(userName);
    }
}
