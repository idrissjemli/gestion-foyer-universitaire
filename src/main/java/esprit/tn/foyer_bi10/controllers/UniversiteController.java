package esprit.tn.foyer_bi10.controllers;

import esprit.tn.foyer_bi10.entity.Universite;
import esprit.tn.foyer_bi10.services.IUniversiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/universite")
public class UniversiteController {

    private final IUniversiteService universiteService;

    @Autowired
    public UniversiteController(IUniversiteService universiteService) {
        this.universiteService = universiteService;
    }

    @GetMapping("/all")
    public List<Universite> getAllUniversites() {
        return universiteService.getALLUniversite();
    }

    @PostMapping("/add")
    public Universite addUniversite(@RequestBody Universite universite) {
        return universiteService.addUniversite(universite);
    }

    @PutMapping("/update")
    public Universite updateUniversite(@RequestBody Universite universite) {
        return universiteService.UpdateUniversite(universite);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUniversite(@PathVariable("id") Long idUniversite) {
        universiteService.deleteUniversite(idUniversite);
        return "Universite " + idUniversite + " supprimee";
    }
}