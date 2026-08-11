package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Chambre;
import esprit.tn.foyer_bi10.exception.BusinessException;
import esprit.tn.foyer_bi10.repositories.ChambreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChambreServiceImpl implements IChambreService {

    ChambreRepository chambreRepository;

    @Override
    public Chambre addChambre(Chambre chambre) {
        // Refuser si le numéro de chambre existe déjà dans le même bloc
        if (chambre.getBloc() != null && chambre.getBloc().getIdBloc() != null) {
            boolean existe = chambreRepository.existsByNumeroChambreAndBlocIdBloc(
                    chambre.getNumeroChambre(), chambre.getBloc().getIdBloc());
            if (existe) {
                throw new BusinessException(
                    "Le numéro de chambre " + chambre.getNumeroChambre()
                    + " existe déjà dans ce bloc.");
            }
        }
        return chambreRepository.save(chambre);
    }

    @Override
    public void deleteChambre(Long idChambre) {
        chambreRepository.deleteById(idChambre);
    }

    @Override
    public Chambre UpdateChambre(Chambre chambre) {
        return chambreRepository.save(chambre);
    }

    @Override
    public List<Chambre> getALLChambre() {
        return chambreRepository.findAll();
    }
}
