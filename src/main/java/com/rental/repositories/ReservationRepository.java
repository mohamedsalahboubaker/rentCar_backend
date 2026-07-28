package com.rental.repositories;

import com.rental.model.Reservation;
import com.rental.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Recherche par client
    List<Reservation> findByClientId(Long clientId);

    // Recherche par voiture
    List<Reservation> findByVoitureId(Long voitureId);

    // Recherche par statut
    List<Reservation> findByStatus(ReservationStatus status);

    // Recherche par période
    List<Reservation> findByDateDebutBetween(LocalDate start, LocalDate end);

    // Recherche par date de début
    List<Reservation> findByDateDebutAfter(LocalDate date);

    // Recherche par date de fin
    List<Reservation> findByDateFinBefore(LocalDate date);

    // Recherche par client et statut
    List<Reservation> findByClientIdAndStatus(Long clientId, ReservationStatus status);

    // Vérifier la disponibilité d'une voiture sur une période
    @Query("SELECT r FROM Reservation r WHERE r.voiture.id = :voitureId " +
            "AND r.status != 'ANNULEE' " +
            "AND ((r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut))")
    List<Reservation> findReservationsForCarInPeriod(@Param("voitureId") Long voitureId,
                                                     @Param("dateDebut") LocalDate dateDebut,
                                                     @Param("dateFin") LocalDate dateFin);

    // Réservations à venir (date de début >= aujourd'hui)
    List<Reservation> findByDateDebutAfterAndStatus(LocalDate date, ReservationStatus status);

    // Réservations pour un client avec un statut spécifique
    List<Reservation> findByClientIdAndStatusNot(Long clientId, ReservationStatus status);
}