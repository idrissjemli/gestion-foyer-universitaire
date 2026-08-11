package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Reservation;
import esprit.tn.foyer_bi10.entity.TypeChambre;
import esprit.tn.foyer_bi10.exception.BusinessException;
import esprit.tn.foyer_bi10.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReservationService implements IReservationService {

    ReservationRepository reservationRepository;

    // Capacité maximale par type de chambre
    private static final Map<TypeChambre, Integer> CAPACITE_PAR_TYPE = Map.of(
            TypeChambre.SIMPLE, 1,
            TypeChambre.DOUBLE, 2,
            TypeChambre.TRIPLE, 3
    );

    @Override
    public Reservation addReservation(Reservation reservation) {
        if (reservation.getChambre() != null && reservation.getChambre().getIdChambre() != null) {
            Long idChambre = reservation.getChambre().getIdChambre();

            // Refuser si la chambre est déjà réservée pour la même année universitaire
            boolean doublonAnnee = reservationRepository
                    .existsByChambreIdChambreAndAnneUniversitaire(
                            idChambre, reservation.getAnneUniversitaire());
            if (doublonAnnee) {
                throw new BusinessException(
                    "Cette chambre est déjà réservée pour l'année universitaire indiquée.");
            }

            // Refuser si le nombre de réservations actives atteint la capacité du type
            TypeChambre type = reservation.getChambre().getTypeC();
            if (type != null) {
                int capaciteMax = CAPACITE_PAR_TYPE.getOrDefault(type, 1);
                long reservationsActives = reservationRepository
                        .countByChambreIdChambreAndEstValidTrue(idChambre);
                if (reservationsActives >= capaciteMax) {
                    throw new BusinessException(
                        "La chambre a atteint sa capacité maximale ("
                        + capaciteMax + " réservation(s) active(s) pour une chambre "
                        + type.name() + ").");
                }
            }
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long idReservation) {
        reservationRepository.deleteById(idReservation);
    }

    @Override
    public Reservation UpdateReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getALLReservation() {
        return reservationRepository.findAll();
    }
}
