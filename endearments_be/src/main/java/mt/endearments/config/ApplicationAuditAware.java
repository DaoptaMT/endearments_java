package mt.endearments.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("system");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return Optional.of(((org.springframework.security.core.userdetails.UserDetails) principal).getUsername());
        }
        return Optional.of(principal.toString());
    }
}
