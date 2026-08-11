package esprit.tn.foyer_bi10.controllers;

import esprit.tn.foyer_bi10.entity.Reservation;
import esprit.tn.foyer_bi10.services.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final IReservationService reservationService;

    @Autowired
    public ReservationController(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/all")
    public List<Reservation> getAllReservations() {
        return reservationService.getALLReservation();
    }

    @PostMapping("/add")
    public Reservation addReservation(@RequestBody Reservation reservation) {
        return reservationService.addReservation(reservation);
    }

    @PutMapping("/update")
    public Reservation updateReservation(@RequestBody Reservation reservation) {
        return reservationService.UpdateReservation(reservation);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long idReservation) {
        reservationService.deleteReservation(idReservation);
        return "Reservation " + idReservation + " supprimee";
    }
}