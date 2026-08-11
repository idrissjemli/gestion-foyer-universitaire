package esprit.tn.foyer_bi10.controllers;

import esprit.tn.foyer_bi10.entity.Bloc;
import esprit.tn.foyer_bi10.services.IBlocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bloc")
public class BlocController {

    private final IBlocService blocService;

    @Autowired
    public BlocController(IBlocService blocService) {
        this.blocService = blocService;
    }

    @GetMapping("/all")
    public List<Bloc> getAllBlocs() {
        return blocService.getALLBloc();
    }

    @PostMapping("/add")
    public Bloc addBloc(@RequestBody Bloc bloc) {
        return blocService.addBloc(bloc);
    }

    @PutMapping("/update")
    public Bloc updateBloc(@RequestBody Bloc bloc) {
        return blocService.UpdateBloc(bloc);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBloc(@PathVariable("id") Long idBloc) {
        blocService.deleteBloc(idBloc);
        return "Bloc " + idBloc + " supprime";
    }
}