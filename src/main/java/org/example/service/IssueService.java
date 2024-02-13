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
    private final SecurityService securityService;

    @Autowired
    public IssueService(IIssueRepo issueRepo, IUserRepo userRepo, IStatusRepo statusRepo, SecurityService securityService) {
        this.issueRepo = issueRepo;
        this.userRepo = userRepo;
        this.statusRepo = statusRepo;
        this.securityService = securityService;
    }

    private boolean isCurrentUserIssueOwner(Issue issue){
        Optional<String> optionalUsername = securityService.getAuthenticatedUserName();

        return optionalUsername.map(s -> s.equals(issue.getCreatedBy().getName())).orElse(false);
    }

    private boolean isIssueAssignedToCurrentUser(Issue issue){
        Optional<String> optionalUsername = securityService.getAuthenticatedUserName();

        return optionalUsername.map(s -> s.equals(issue.getAssignedTo().getName())).orElse(false);
    }

    public boolean isUserAuthorizedToModifyIssue(Issue issue){
        return isCurrentUserIssueOwner(issue) || isIssueAssignedToCurrentUser(issue) || securityService.isCurrentUserAdmin();
    }

    public List<Issue> findAllByCurrentUserAndStatus(String statusName){
        Optional<User> user = securityService.getAuthenticatedUser();
        if (user.isPresent()){
            Status status = statusRepo.findByName(statusName);
            return issueRepo.findAllByCreatedByAndStatus(user.get(), status);
        } else {
            return new ArrayList<>();
        }
    }

    public Long countAllByCurrentUserAndStatus(String statusName){
        Optional<User> user = securityService.getAuthenticatedUser();
        if (user.isPresent()){
            Status status = statusRepo.findByName(statusName);
            return issueRepo.countAllByCreatedByAndStatus(user.get(), status);
        } else {
            return 0L;
        }
    }

    public List<Issue> fetchPage(int page, int limit) {
        return issueRepo.findAll(PageRequest.of(page, limit)).getContent();
    }

    public void save(@Valid Issue issue){
        Long id = issue.getId();
        if (Objects.nonNull(id)){
            Optional<Issue> optionalIssue = issueRepo.findById(id);
            if (optionalIssue.isPresent()) {
                Issue currentIssue = optionalIssue.get();
                if (!currentIssue.getAssignedTo().getUsername().equals(issue.getAssignedTo().getUsername())) {
                    issue.setAssignedDate(new Date(new java.util.Date().getTime()));
                }
            }
        } else {
            if (Objects.nonNull(issue.getAssignedTo())){
                issue.setAssignedDate(new Date(new java.util.Date().getTime()));
            }
        }

        issueRepo.save(issue);
    }

    public void delete(Issue issue){
        issueRepo.delete(issue);
    }
}
