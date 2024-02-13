package org.example.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.example.audit.AwareUserDetails;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SecurityService {
    private final AuthenticationContext authenticationContext;

    @Autowired
    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public Optional<User> getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth)) {
            return Optional.of(((AwareUserDetails) auth.getPrincipal()).getUser());
        }
        return Optional.empty();
    }

    public Optional<String> getAuthenticatedUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth)) {
            User user = ((AwareUserDetails) auth.getPrincipal()).getUser();

            return Optional.of(user.getName());
        }

        return Optional.empty();
    }

    public boolean isCurrentUserAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth)){
            User user = ((AwareUserDetails) auth.getPrincipal()).getUser();

            return user.getRole().getName().equals("ROLE_ADMIN");
        }

        return false;
    }

    public void logout() {
        authenticationContext.logout();
    }
}
