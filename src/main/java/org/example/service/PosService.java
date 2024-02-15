package org.example.service;

import jakarta.validation.Valid;
import org.example.entity.POS;
import org.example.repo.IPosRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosService {
    private final IPosRepo posRepo;

    public PosService(IPosRepo posRepo) {
        this.posRepo = posRepo;
    }

    public List<POS> findAll(){
        return posRepo.findAll();
    }

    public List<POS> fetchPage(int page, int limit) {
        return posRepo.findAll(PageRequest.of(page, limit)).getContent();
    }

    public void delete(POS pos) {
        posRepo.delete(pos);
    }

    public POS save(@Valid POS pos) {
        return posRepo.save(pos);
    }
}
