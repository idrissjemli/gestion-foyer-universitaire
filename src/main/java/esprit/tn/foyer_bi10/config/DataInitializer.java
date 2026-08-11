package esprit.tn.foyer_bi10.config;

import esprit.tn.foyer_bi10.entity.Role;
import esprit.tn.foyer_bi10.entity.Utilisateur;
import esprit.tn.foyer_bi10.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crée les deux utilisateurs par défaut (admin + etudiant) au premier démarrage
 * si la table utilisateur est vide. Les mots de passe sont chiffrés en BCrypt.
 */
@Component
@AllArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (utilisateurRepository.count() == 0) {
            utilisateurRepository.save(Utilisateur.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build());

            utilisateurRepository.save(Utilisateur.builder()
                    .username("etudiant")
                    .password(passwordEncoder.encode("etudiant123"))
                    .role(Role.ETUDIANT)
                    .build());

            log.info("Utilisateurs par défaut créés : admin / etudiant");
        }
    }
}
