package com.example.MPMT.service;

import com.example.MPMT.model.Users;
import com.example.MPMT.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsersRepository usersRepository;

    public AuthService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<Users> authenticate(String email, String password) {
        Optional<Users> user = usersRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}