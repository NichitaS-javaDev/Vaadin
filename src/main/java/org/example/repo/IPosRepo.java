package org.example.repo;

import org.example.entity.POS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPosRepo extends JpaRepository<POS, Integer> {
}
