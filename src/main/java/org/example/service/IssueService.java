package org.example.service;

import jakarta.validation.Valid;
import org.example.entity.Issue;
import org.example.entity.Status;
import org.example.entity.User;
import org.example.repo.IIssueRepo;
import org.example.repo.IStatusRepo;
import org.example.repo.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class IssueService {
    private final IIssueRepo issueRepo;
    private final IUserRepo userRepo;
    private final IStatusRepo statusRepo;

    @Autowired
    public IssueService(IIssueRepo issueRepo, IUserRepo userRepo, IStatusRepo statusRepo) {
        this.issueRepo = issueRepo;
        this.userRepo = userRepo;
        this.statusRepo = statusRepo;
    }

    public boolean isCurrentUserIssueOwner(Issue issue){
        return true;
    }

    public List<Issue> findAllByUsernameAndStatus(String statusName){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth)){
            String currentUsername = auth.getName();
            User currentUser = userRepo.findUserByUsernameOrEmail(currentUsername,currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
            Status status = statusRepo.findByName(statusName);

            return issueRepo.findAllByCreatedByAndStatus(currentUser, status);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Issue> fetchPage(int page, int limit) {
        return issueRepo.findAll(PageRequest.of(page, limit)).getContent();
    }

    public void save(@Valid Issue issue){
        Long id = issue.getId();
        Optional<Issue> tmpIssue = issueRepo.findById(id);
        if (tmpIssue.isPresent()) {
            Issue currentIssue = tmpIssue.get();
            if (!currentIssue.getAssignedTo().equals(issue.getAssignedTo())) {
                issue.setAssignedDate(new Date(new java.util.Date().getTime()));
            }
        }

        issueRepo.save(issue);
    }

    public void delete(Issue issue){
        issueRepo.delete(issue);
    }
}
