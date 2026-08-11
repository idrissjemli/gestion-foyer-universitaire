package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Foyer;

import java.util.List;


//@FieldDefaults(level = AccessLevel.PUBLIC)
public interface IFoyerService {
    Foyer addFoyer(Foyer foyer);

    void deleteFoyer(Long idFoyer);

    Foyer UpdateFoyer(Foyer foyer);

    List<Foyer> getALLFoyer();
}