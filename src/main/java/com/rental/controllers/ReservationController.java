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

    // Créer une réservation
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation created = reservationService.createReservation(reservation);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Récupérer toutes les réservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // Récupérer une réservation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    // Récupérer les réservations par client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> getReservationsByClient(@PathVariable Long clientId) {
        List<Reservation> reservations = reservationService.getReservationsByClient(clientId);
        return ResponseEntity.ok(reservations);
    }

    // Récupérer les réservations par voiture
    @GetMapping("/voiture/{voitureId}")
    public ResponseEntity<List<Reservation>> getReservationsByVoiture(@PathVariable Long voitureId) {
        List<Reservation> reservations = reservationService.getReservationsByVoiture(voitureId);
        return ResponseEntity.ok(reservations);
    }

    // Récupérer les réservations par statut
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable ReservationStatus status) {
        List<Reservation> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    // Récupérer les réservations par période
    @GetMapping("/period")
    public ResponseEntity<List<Reservation>> getReservationsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Reservation> reservations = reservationService.getReservationsByPeriod(start, end);
        return ResponseEntity.ok(reservations);
    }

    // Récupérer les réservations à venir
    @GetMapping("/upcoming")
    public ResponseEntity<List<Reservation>> getUpcomingReservations() {
        List<Reservation> reservations = reservationService.getUpcomingReservations();
        return ResponseEntity.ok(reservations);
    }

    // Mettre à jour le statut d'une réservation
    @PatchMapping("/{id}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status) {
        Reservation updated = reservationService.updateReservationStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // Annuler une réservation
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    // Recherche avancée
    @GetMapping("/search")
    public ResponseEntity<List<Reservation>> advancedSearch(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long voitureId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Reservation> reservations = reservationService.advancedSearch(clientId, voitureId, dateDebut, dateFin);
        return ResponseEntity.ok(reservations);
    }

    // Vérifier la disponibilité d'une voiture
    @GetMapping("/disponibilite")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long voitureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        boolean disponible = reservationService.checkCarAvailability(voitureId, dateDebut, dateFin);
        return ResponseEntity.ok(disponible);
    }
}