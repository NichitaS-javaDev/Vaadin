package org.example.service;

import org.example.entity.ConnectionType;
import org.example.repo.IConnectionTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionTypeService {
    private final IConnectionTypeRepo connectionTypeRepo;

    @Autowired
    public ConnectionTypeService(IConnectionTypeRepo connectionTypeRepo) {
        this.connectionTypeRepo = connectionTypeRepo;
    }

    public List<ConnectionType> findAll(){
        return connectionTypeRepo.findAll();
    }
}
