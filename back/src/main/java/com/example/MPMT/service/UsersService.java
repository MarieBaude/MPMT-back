package com.example.MPMT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.MPMT.repository.UsersRepository;
import com.example.MPMT.model.Users;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUsersById(Long id) {
        return usersRepository.findById(id);
    }

    public Users saveUser(Users user) {
        return usersRepository.save(user);
    }

}
