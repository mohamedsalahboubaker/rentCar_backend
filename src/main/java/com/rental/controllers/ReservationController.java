// com/rental/controllers/ReservationController.java
package com.rental.controllers;

import com.rental.model.Reservation;
import com.rental.model.enums.ReservationStatus;
import com.rental.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // ============================================================
    // 1. Créer une réservation
    // ============================================================
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        System.out.println("📥 Réservation reçue:");
        System.out.println("  Client ID: " + (reservation.getClient() != null ? reservation.getClient().getId() : "null"));
        System.out.println("  Voiture ID: " + (reservation.getVoiture() != null ? reservation.getVoiture().getId() : "null"));
        System.out.println("  Date début: " + reservation.getDateDebut());
        System.out.println("  Date fin: " + reservation.getDateFin());

        Reservation created = reservationService.createReservation(reservation);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ============================================================
    // 2. Récupérer toutes les réservations
    // ============================================================
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // ============================================================
    // 3. Récupérer une réservation par ID
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    // ============================================================
    // 4. Récupérer les réservations d'un client
    // ============================================================
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> getReservationsByClient(@PathVariable Long clientId) {
        List<Reservation> reservations = reservationService.getReservationsByClient(clientId);
        return ResponseEntity.ok(reservations);
    }

    // ============================================================
    // 5. Récupérer les réservations d'une voiture
    // ============================================================
    @GetMapping("/voiture/{voitureId}")
    public ResponseEntity<List<Reservation>> getReservationsByVoiture(@PathVariable Long voitureId) {
        List<Reservation> reservations = reservationService.getReservationsByVoiture(voitureId);
        return ResponseEntity.ok(reservations);
    }

    // ============================================================
    // 6. Récupérer les réservations par statut
    // ============================================================
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable ReservationStatus status) {
        List<Reservation> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    // ============================================================
    // 7. Récupérer les réservations par période
    // ============================================================
    @GetMapping("/period")
    public ResponseEntity<List<Reservation>> getReservationsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Reservation> reservations = reservationService.getReservationsByPeriod(start, end);
        return ResponseEntity.ok(reservations);
    }

    // ============================================================
    // 8. Récupérer les réservations à venir
    // ============================================================
    @GetMapping("/upcoming")
    public ResponseEntity<List<Reservation>> getUpcomingReservations() {
        List<Reservation> reservations = reservationService.getUpcomingReservations();
        return ResponseEntity.ok(reservations);
    }

    // ============================================================
    // 9. Mettre à jour le statut d'une réservation
    // ============================================================
    @PatchMapping("/{id}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status) {
        Reservation updated = reservationService.updateReservationStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // ============================================================
    // 10. Annuler une réservation
    // ============================================================
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 11. Recherche avancée
    // ============================================================
    @GetMapping("/search")
    public ResponseEntity<List<Reservation>> advancedSearch(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long voitureId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Reservation> reservations = reservationService.advancedSearch(clientId, voitureId, dateDebut, dateFin);
        return ResponseEntity.ok(reservations);
    }

    // ============================================================
    // 12. Vérifier la disponibilité d'une voiture
    // ============================================================
    @GetMapping("/disponibilite")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long voitureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        boolean disponible = reservationService.checkCarAvailability(voitureId, dateDebut, dateFin);
        return ResponseEntity.ok(disponible);
    }

    // ============================================================
    // 13. Vérifier si une voiture est actuellement louée (nouveau)
    // ============================================================
    @GetMapping("/voiture/{voitureId}/actuellement-louee")
    public ResponseEntity<Boolean> isCurrentlyRented(@PathVariable Long voitureId) {
        boolean isRented = reservationService.isCarCurrentlyRented(voitureId);
        return ResponseEntity.ok(isRented);
    }

    // ============================================================
    // 14. Récupérer les réservations actives (pour aujourd'hui)
    // ============================================================
    @GetMapping("/actives")
    public ResponseEntity<List<Reservation>> getActiveReservations() {
        List<Reservation> reservations = reservationService.getActiveReservations();
        return ResponseEntity.ok(reservations);
    }

    // ============================================================
    // 15. Récupérer les réservations par date (nouveau)
    // ============================================================
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Reservation>> getReservationsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Reservation> reservations = reservationService.getReservationsByDate(date);
        return ResponseEntity.ok(reservations);
    }
}