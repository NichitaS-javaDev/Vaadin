package org.example.audit;

import org.example.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(((AwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
    }
}
