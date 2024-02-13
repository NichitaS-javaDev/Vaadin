package org.example.repo;

import org.example.entity.Issue;
import org.example.entity.Status;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IIssueRepo extends JpaRepository<Issue, Long> {
    List<Issue> findAllByCreatedByAndStatus(User user, Status status);
    Long countAllByCreatedByAndStatus(User user, Status status);
}
