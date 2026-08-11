package esprit.tn.foyer_bi10.repositories;

import esprit.tn.foyer_bi10.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByUsername(String username);

    /** Compte le nombre d'utilisateurs ayant un rôle donné — utilisé pour protéger le dernier ADMIN. */
    long countByRole(esprit.tn.foyer_bi10.entity.Role role);
}
