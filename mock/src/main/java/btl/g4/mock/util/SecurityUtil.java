package btl.g4.mock.util;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import btl.g4.mock.security.CustomUserDetail;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Optional<String> getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail user) {
            return Optional.of(user.getUsername());
        }
        return Optional.empty();
    }

    public static Optional<CustomUserDetail> getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail userDetails) {
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }
}
