package org.example.service;

import jakarta.validation.Valid;
import org.example.entity.User;
import org.example.repo.IUserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final IUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public List<User> fetchPage(int page, int limit) {
        return userRepo.findAll(PageRequest.of(page, limit)).getContent();
    }

    public void save(@Valid User user){
        if (Objects.isNull(user.getId())){
            String encryptedPass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPass);
            userRepo.save(user);
        } else {
            userRepo.save(user);
        }
    }

    public void delete(User user){
        userRepo.delete(user);
    }
}
