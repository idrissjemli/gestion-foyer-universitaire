package esprit.tn.foyer_bi10.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChambre;
    private Long numeroChambre;
    @Enumerated(EnumType.STRING)
    private TypeChambre typeC;

    // Une chambre appartient à un seul bloc (correction de la relation inversée)
    @ManyToOne
    private Bloc bloc;

    // Une chambre peut avoir plusieurs réservations
    @OneToMany(mappedBy = "chambre")
    @JsonIgnore
    private Set<Reservation> reservations;
}
