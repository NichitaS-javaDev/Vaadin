package org.example.service;

import org.example.entity.City;
import org.example.repo.ICityRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    private final ICityRepo cityRepo;

    public CityService(ICityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    public List<City> findAll(){
        return cityRepo.findAll();
    }
}
