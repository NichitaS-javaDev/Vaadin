package org.example.service;

import org.example.entity.IssueType;
import org.example.repo.IIssueTypeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueTypeService {
    private final IIssueTypeRepo typeRepo;

    public IssueTypeService(IIssueTypeRepo typeRepo) {
        this.typeRepo = typeRepo;
    }

    public List<IssueType> findAll(){
        return typeRepo.findAll();
    }
}
