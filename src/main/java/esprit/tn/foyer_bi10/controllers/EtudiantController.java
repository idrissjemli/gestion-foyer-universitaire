package esprit.tn.foyer_bi10.controllers;

import esprit.tn.foyer_bi10.entity.Etudiant;
import esprit.tn.foyer_bi10.services.IEtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etudiant")
public class EtudiantController {

    private final IEtudiantService etudiantService;

    @Autowired
    public EtudiantController(IEtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping("/all")
    public List<Etudiant> getAllEtudiants() {
        return etudiantService.getALLEtudiant();
    }

    @PostMapping("/add")
    public Etudiant addEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.addEtudiant(etudiant);
    }

    @PutMapping("/update")
    public Etudiant updateEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.UpdateEtudiant(etudiant);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEtudiant(@PathVariable("id") Long idEtudiant) {
        etudiantService.deleteEtudiant(idEtudiant);
        return "Etudiant " + idEtudiant + " supprime";
    }
}