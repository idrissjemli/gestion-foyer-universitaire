package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Chambre;
import esprit.tn.foyer_bi10.entity.Reservation;
import esprit.tn.foyer_bi10.entity.TypeChambre;
import esprit.tn.foyer_bi10.exception.BusinessException;
import esprit.tn.foyer_bi10.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    // ── Helpers ────────────────────────────────────────────────────────────

    private Chambre chambreSimple(Long id) {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(id);
        chambre.setNumeroChambre(100L + id);
        chambre.setTypeC(TypeChambre.SIMPLE);
        return chambre;
    }

    private Reservation reservationPourChambre(Chambre chambre) {
        Reservation r = new Reservation();
        r.setAnneUniversitaire(new Date());
        r.setEstValid(true);
        r.setChambre(chambre);
        return r;
    }

    // ── Tests ──────────────────────────────────────────────────────────────

    @Test
    void ajouterUneReservationSurUneChambreDejaReserveeLaMemeAnneeLeveUneBusinessException() {
        Chambre chambre = chambreSimple(1L);
        Reservation reservation = reservationPourChambre(chambre);

        // La chambre est déjà réservée pour cette date
        when(reservationRepository.existsByChambreIdChambreAndAnneUniversitaire(
                1L, reservation.getAnneUniversitaire()))
                .thenReturn(true);

        assertThatThrownBy(() -> reservationService.addReservation(reservation))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("déjà réservée")
                .hasMessageContaining("année universitaire");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void ajouterUneReservationQuiDepaseLaCapaciteSimpleLeveUneBusinessException() {
        Chambre chambre = chambreSimple(2L); // SIMPLE → max 1 réservation active
        Reservation reservation = reservationPourChambre(chambre);

        // Pas de doublon sur l'année
        when(reservationRepository.existsByChambreIdChambreAndAnneUniversitaire(
                2L, reservation.getAnneUniversitaire()))
                .thenReturn(false);

        // 1 réservation active déjà présente → plafond SIMPLE atteint
        when(reservationRepository.countByChambreIdChambreAndEstValidTrue(2L))
                .thenReturn(1L);

        assertThatThrownBy(() -> reservationService.addReservation(reservation))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("capacité maximale")
                .hasMessageContaining("SIMPLE");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void ajouterUneReservationValideAppelleReservationRepositorySave() {
        Chambre chambre = chambreSimple(3L); // SIMPLE → max 1
        Reservation reservation = reservationPourChambre(chambre);

        Reservation reservationSauvegardee = reservationPourChambre(chambre);
        reservationSauvegardee.setIdReservation(99L);

        // Aucun doublon, aucune réservation active → tout est libre
        when(reservationRepository.existsByChambreIdChambreAndAnneUniversitaire(
                3L, reservation.getAnneUniversitaire()))
                .thenReturn(false);
        when(reservationRepository.countByChambreIdChambreAndEstValidTrue(3L))
                .thenReturn(0L);
        when(reservationRepository.save(reservation)).thenReturn(reservationSauvegardee);

        Reservation resultat = reservationService.addReservation(reservation);

        verify(reservationRepository).save(reservation);
        assertThat(resultat.getIdReservation()).isEqualTo(99L);
    }
}
