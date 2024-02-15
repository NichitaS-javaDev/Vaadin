package org.example.repo;

import org.example.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRoleRepo extends JpaRepository<UserRole, Long> {
}
