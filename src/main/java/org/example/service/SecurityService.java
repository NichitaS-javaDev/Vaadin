package org.example.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.example.entity.User;
import org.example.repo.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class SecurityService {
    private final AuthenticationContext authenticationContext;
    private final IUserRepo userRepo;

    @Autowired
    public SecurityService(AuthenticationContext authenticationContext, IUserRepo userRepo) {
        this.authenticationContext = authenticationContext;
        this.userRepo = userRepo;
    }

    public UserDetails getAuthenticatedUser() {
        Optional<UserDetails> userDetails = authenticationContext.getAuthenticatedUser(UserDetails.class);

        return userDetails.orElse(null);
    }

    public String getAuthenticatedUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth)) {
            String currentUsername = auth.getName();
            User currentUser = userRepo.findUserByUsernameOrEmail(currentUsername, currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
            return currentUser.getName();
        }

        return null;
    }

    public void logout() {
        authenticationContext.logout();
    }
}
