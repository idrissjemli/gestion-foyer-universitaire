package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Role;
import esprit.tn.foyer_bi10.entity.Utilisateur;
import esprit.tn.foyer_bi10.exception.BusinessException;
import esprit.tn.foyer_bi10.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation des règles métier : unicité du username, chiffrement BCrypt,
 * et protection du dernier compte ADMIN contre la suppression.
 */
@Service
@AllArgsConstructor
public class UtilisateurServiceImpl implements IUtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur addUtilisateur(Utilisateur utilisateur) {
        // Refuser un username déjà pris
        if (utilisateurRepository.findByUsername(utilisateur.getUsername()).isPresent()) {
            throw new BusinessException(
                "Le nom d'utilisateur « " + utilisateur.getUsername() + " » est déjà utilisé.");
        }
        // Chiffrer le mot de passe avant persistance
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void deleteUtilisateur(Long id) {
        Utilisateur cible = utilisateurRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable (id=" + id + ")."));

        // Refuser la suppression du dernier compte ADMIN
        if (cible.getRole() == Role.ADMIN && utilisateurRepository.countByRole(Role.ADMIN) <= 1) {
            throw new BusinessException(
                "Impossible de supprimer le dernier compte administrateur.");
        }
        utilisateurRepository.deleteById(id);
    }
}
