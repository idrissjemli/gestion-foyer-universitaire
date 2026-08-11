package esprit.tn.foyer_bi10.controllers;

import esprit.tn.foyer_bi10.entity.Foyer;
import esprit.tn.foyer_bi10.services.IFoyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foyer")
public class FoyerController {

    private final IFoyerService foyerService;

    @Autowired
    public FoyerController(IFoyerService foyerService) {
        this.foyerService = foyerService;
    }

    @GetMapping("/all")
    public List<Foyer> getAllFoyers() {
        return foyerService.getALLFoyer();
    }

    @PostMapping("/add")
    public Foyer addFoyer(@RequestBody Foyer foyer) {
        return foyerService.addFoyer(foyer);
    }

    @PutMapping("/update")
    public Foyer updateFoyer(@RequestBody Foyer foyer) {
        return foyerService.UpdateFoyer(foyer);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFoyer(@PathVariable("id") Long idFoyer) {
        foyerService.deleteFoyer(idFoyer);
        return "Foyer " + idFoyer + " supprime";
    }
}