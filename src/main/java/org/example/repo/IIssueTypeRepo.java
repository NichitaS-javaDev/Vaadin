package org.example.repo;

import org.example.entity.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIssueTypeRepo extends JpaRepository<IssueType, Integer> {
}
