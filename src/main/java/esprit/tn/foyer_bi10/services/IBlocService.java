package esprit.tn.foyer_bi10.services;

import esprit.tn.foyer_bi10.entity.Bloc;

import java.util.List;

//@FieldDefaults(level = AccessLevel.PUBLIC)
public interface IBlocService {
    Bloc addBloc (Bloc bloc);

    void deleteBloc (Long idBloc);

    Bloc UpdateBloc ( Bloc bloc);

    List<Bloc> getALLBloc();}
