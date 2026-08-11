package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Bloc;
import esprit.tn.foyer_bi10.exception.BusinessException;
import esprit.tn.foyer_bi10.repositories.BlocRepository;
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
class BlocServiceTest {

    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    BlocService blocService;

    @Test
    void ajouterUnBlocAvecCapaciteZeroLeveUneBusinessException() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(0L);

        assertThatThrownBy(() -> blocService.addBloc(bloc))
                .isInstanceOf(BusinessException.class)
                .hasMessage("La capacité du bloc doit être supérieure à 0.");

        // Le repository ne doit jamais être appelé
        verify(blocRepository, never()).save(any());
    }

    @Test
    void ajouterUnBlocAvecCapaciteNullLeveUneBusinessException() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc B");
        bloc.setCapaciteBloc(null);

        assertThatThrownBy(() -> blocService.addBloc(bloc))
                .isInstanceOf(BusinessException.class)
                .hasMessage("La capacité du bloc doit être supérieure à 0.");

        verify(blocRepository, never()).save(any());
    }

    @Test
    void ajouterUnBlocAvecCapaciteValideAppelleBlocRepositorySave() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc C");
        bloc.setCapaciteBloc(50L);

        Bloc blocSauvegarde = new Bloc();
        blocSauvegarde.setIdBloc(1L);
        blocSauvegarde.setNomBloc("Bloc C");
        blocSauvegarde.setCapaciteBloc(50L);

        when(blocRepository.save(bloc)).thenReturn(blocSauvegarde);

        Bloc resultat = blocService.addBloc(bloc);

        verify(blocRepository).save(bloc);
        assertThat(resultat.getIdBloc()).isEqualTo(1L);
    }
}
