package org.example.repo;

import org.example.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICityRepo extends JpaRepository<City, Integer> {
}
