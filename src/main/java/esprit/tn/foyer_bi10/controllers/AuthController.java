package esprit.tn.foyer_bi10.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Expose les informations de l'utilisateur connecté au frontend JS.
 * GET /api/me → { "username": "admin", "role": "ROLE_ADMIN" }
 */
@RestController
public class AuthController {

    @GetMapping("/api/me")
    public Map<String, String> me(@AuthenticationPrincipal UserDetails user) {
        String role = user.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("");
        return Map.of(
                "username", user.getUsername(),
                "role", role
        );
    }
}
