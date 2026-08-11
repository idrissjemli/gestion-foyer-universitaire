package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Utilisateur;

import java.util.List;

/**
 * Contrat métier pour la gestion des comptes utilisateurs.
 */
public interface IUtilisateurService {
    List<Utilisateur> getAllUtilisateurs();
    Utilisateur addUtilisateur(Utilisateur utilisateur);
    void deleteUtilisateur(Long id);
}
