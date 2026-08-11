package esprit.tn.foyer_bi10.controllers;

import esprit.tn.foyer_bi10.entity.Chambre;
import esprit.tn.foyer_bi10.services.IChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chambre")
public class ChambreController {

    private final IChambreService chambreService;

    @Autowired
    public ChambreController(IChambreService chambreService) {
        this.chambreService = chambreService;
    }

    @GetMapping("/all")
    public List<Chambre> getAllChambres() {
        return chambreService.getALLChambre();
    }

    @PostMapping("/add")
    public Chambre addChambre(@RequestBody Chambre chambre) {
        return chambreService.addChambre(chambre);
    }

    @PutMapping("/update")
    public Chambre updateChambre(@RequestBody Chambre chambre) {
        return chambreService.UpdateChambre(chambre);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteChambre(@PathVariable("id") Long idChambre) {
        chambreService.deleteChambre(idChambre);
        return "Chambre " + idChambre + " supprimee";
    }
}