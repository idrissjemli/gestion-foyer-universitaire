package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Bloc;
import esprit.tn.foyer_bi10.entity.Chambre;
import esprit.tn.foyer_bi10.entity.TypeChambre;
import esprit.tn.foyer_bi10.exception.BusinessException;
import esprit.tn.foyer_bi10.repositories.ChambreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChambreServiceTest {

    @Mock
    ChambreRepository chambreRepository;

    @InjectMocks
    ChambreServiceImpl chambreService;

    // ── Helpers ────────────────────────────────────────────────────────────

    private Bloc blocAvecId(Long id) {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(id);
        bloc.setNomBloc("Bloc " + id);
        bloc.setCapaciteBloc(20L);
        return bloc;
    }

    private Chambre chambreAvecNumeroEtBloc(Long numero, Bloc bloc) {
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(numero);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setBloc(bloc);
        return chambre;
    }

    // ── Tests ──────────────────────────────────────────────────────────────

    @Test
    void ajouterUneChambreAvecNumeroDejaPrisLeveUneBusinessException() {
        Bloc bloc = blocAvecId(1L);
        Chambre chambre = chambreAvecNumeroEtBloc(101L, bloc);

        // Le repository signale que le numéro 101 existe déjà dans le bloc 1
        when(chambreRepository.existsByNumeroChambreAndBlocIdBloc(101L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> chambreService.addChambre(chambre))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("101")
                .hasMessageContaining("existe déjà dans ce bloc");

        verify(chambreRepository, never()).save(any());
    }

    @Test
    void ajouterUneChambreAvecNumeroLibreAppelleChambreRepositorySave() {
        Bloc bloc = blocAvecId(1L);
        Chambre chambre = chambreAvecNumeroEtBloc(102L, bloc);

        Chambre chambreSauvegardee = chambreAvecNumeroEtBloc(102L, bloc);
        chambreSauvegardee.setIdChambre(10L);

        when(chambreRepository.existsByNumeroChambreAndBlocIdBloc(102L, 1L)).thenReturn(false);
        when(chambreRepository.save(chambre)).thenReturn(chambreSauvegardee);

        Chambre resultat = chambreService.addChambre(chambre);

        verify(chambreRepository).save(chambre);
        assertThat(resultat.getIdChambre()).isEqualTo(10L);
    }

    @Test
    void ajouterUneChambreAvecMemmeNumeroMaisDansUnAutreBlocEstAcceptee() {
        // Numéro 101 dans bloc 1 existe déjà, mais on ajoute dans bloc 2 → doit être accepté
        Bloc bloc2 = blocAvecId(2L);
        Chambre chambre = chambreAvecNumeroEtBloc(101L, bloc2);

        Chambre chambreSauvegardee = chambreAvecNumeroEtBloc(101L, bloc2);
        chambreSauvegardee.setIdChambre(20L);

        when(chambreRepository.existsByNumeroChambreAndBlocIdBloc(101L, 2L)).thenReturn(false);
        when(chambreRepository.save(chambre)).thenReturn(chambreSauvegardee);

        Chambre resultat = chambreService.addChambre(chambre);

        verify(chambreRepository).save(chambre);
        assertThat(resultat.getIdChambre()).isEqualTo(20L);
    }
}
