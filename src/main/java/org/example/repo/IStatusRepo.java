package org.example.repo;

import org.example.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStatusRepo extends JpaRepository<Status, Integer> {
    Status findByName(String name);
}
