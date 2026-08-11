package esprit.tn.foyer_bi10.repositories;

import esprit.tn.foyer_bi10.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Vérifie si une chambre est déjà réservée pour une année universitaire donnée
    boolean existsByChambreIdChambreAndAnneUniversitaire(Long idChambre, Date anneUniversitaire);

    // Compte les réservations actives (estValid=true) pour une chambre
    long countByChambreIdChambreAndEstValidTrue(Long idChambre);
}
