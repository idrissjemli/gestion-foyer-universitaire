package esprit.tn.foyer_bi10.repositories;

import esprit.tn.foyer_bi10.entity.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {

    // Vérifie si un numéro de chambre existe déjà dans un bloc donné
    boolean existsByNumeroChambreAndBlocIdBloc(Long numeroChambre, Long idBloc);
}
