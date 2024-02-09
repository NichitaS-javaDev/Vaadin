package org.example.repo;

import org.example.entity.ConnectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConnectionTypeRepo extends JpaRepository<ConnectionType, Integer> {
}
