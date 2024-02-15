package org.example.service;

import org.example.entity.UserRole;
import org.example.repo.IUserRoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    private final IUserRoleRepo userRoleRepo;

    public UserRoleService(IUserRoleRepo userRoleRepo) {
        this.userRoleRepo = userRoleRepo;
    }

    public List<UserRole> findAll(){
        return userRoleRepo.findAll();
    }
}
