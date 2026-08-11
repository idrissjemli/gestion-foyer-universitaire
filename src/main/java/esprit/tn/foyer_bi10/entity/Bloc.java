package esprit.tn.foyer_bi10.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Bloc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBloc ;
    private String nomBloc ;
    private Long capaciteBloc ;
    @ManyToOne
    Foyer foyer;

    // Un bloc contient plusieurs chambres
    @OneToMany(mappedBy = "bloc")
    @JsonIgnore
    private Set<Chambre> chambres;
}




