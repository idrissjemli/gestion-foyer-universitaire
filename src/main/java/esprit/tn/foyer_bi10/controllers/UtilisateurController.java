package esprit.tn.foyer_bi10.controllers;

import esprit.tn.foyer_bi10.entity.Utilisateur;
import esprit.tn.foyer_bi10.services.IUtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de gestion des comptes — réservés à ADMIN (règle définie dans SecurityConfig).
 */
@RestController
@RequestMapping("/utilisateur")
@AllArgsConstructor
public class UtilisateurController {

    private final IUtilisateurService utilisateurService;

    @GetMapping("/all")
    public List<Utilisateur> getAll() {
        return utilisateurService.getAllUtilisateurs();
    }

    @PostMapping("/add")
    public Utilisateur add(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.addUtilisateur(utilisateur);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
    }
}
