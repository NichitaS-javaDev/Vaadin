package org.example.service;

import org.example.entity.Status;
import org.example.repo.IStatusRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueStatusService {
    private final IStatusRepo statusRepo;

    public IssueStatusService(IStatusRepo statusRepo) {
        this.statusRepo = statusRepo;
    }

    public List<Status> findAll(){
        return statusRepo.findAll();
    }
}
