package com.example.MPMT.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.MPMT.service.UsersService;
import com.example.MPMT.model.Users;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<Users> getAllUsers() {
        return usersService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Users> getUsersById(@PathVariable Long id) {
        return usersService.getUsersById(id);
    }

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users savedUser = usersService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

}
