// com/rental/repositories/ReservationRepository.java
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

    // ============================================================
    // 1. Recherche par client
    // ============================================================
    List<Reservation> findByClientId(Long clientId);

    // ============================================================
    // 2. Recherche par voiture
    // ============================================================
    List<Reservation> findByVoitureId(Long voitureId);

    // ============================================================
    // 3. Recherche par statut
    // ============================================================
    List<Reservation> findByStatus(ReservationStatus status);

    // ============================================================
    // 4. Recherche par période (date de début)
    // ============================================================
    List<Reservation> findByDateDebutBetween(LocalDate start, LocalDate end);

    // ============================================================
    // 5. Recherche par date de début après une date
    // ============================================================
    List<Reservation> findByDateDebutAfter(LocalDate date);

    // ============================================================
    // 6. Recherche par date de fin avant une date
    // ============================================================
    List<Reservation> findByDateFinBefore(LocalDate date);

    // ============================================================
    // 7. Vérifier la disponibilité d'une voiture sur une période
    // ============================================================
    @Query("SELECT r FROM Reservation r WHERE r.voiture.id = :voitureId " +
            "AND r.status != 'ANNULEE' " +
            "AND r.status != 'TERMINEE' " +
            "AND ((r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut))")
    List<Reservation> findReservationsForCarInPeriod(@Param("voitureId") Long voitureId,
                                                     @Param("dateDebut") LocalDate dateDebut,
                                                     @Param("dateFin") LocalDate dateFin);

    // ============================================================
    // 8. Réservations actives pour une date donnée
    // ============================================================
    @Query("SELECT r FROM Reservation r WHERE :date BETWEEN r.dateDebut AND r.dateFin " +
            "AND r.status != 'ANNULEE' AND r.status != 'TERMINEE'")
    List<Reservation> findActiveReservationsForDate(@Param("date") LocalDate date);

    // ============================================================
    // 9. Réservations pour une date donnée (tous statuts)
    // ============================================================
    @Query("SELECT r FROM Reservation r WHERE :date BETWEEN r.dateDebut AND r.dateFin")
    List<Reservation> findReservationsForDate(@Param("date") LocalDate date);

    // ============================================================
    // 10. Réservations à venir avec statut spécifique
    // ============================================================
    List<Reservation> findByDateDebutAfterAndStatus(LocalDate date, ReservationStatus status);

    // ============================================================
    // 11. Réservations par client et statut
    // ============================================================
    List<Reservation> findByClientIdAndStatus(Long clientId, ReservationStatus status);

    // ============================================================
    // 12. Réservations par client avec statut différent
    // ============================================================
    List<Reservation> findByClientIdAndStatusNot(Long clientId, ReservationStatus status);

    // ============================================================
    // 13. Compter les réservations par statut
    // ============================================================
    long countByStatus(ReservationStatus status);

    // ============================================================
    // 14. Compter les réservations d'une voiture par statut
    // ============================================================
    long countByVoitureIdAndStatus(Long voitureId, ReservationStatus status);
}