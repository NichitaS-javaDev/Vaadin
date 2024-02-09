package org.example.service;

import org.example.entity.User;
import org.example.repo.IUserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final IUserRepo userRepo;

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }
}
