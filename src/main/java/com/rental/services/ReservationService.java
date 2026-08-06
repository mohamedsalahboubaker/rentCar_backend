// com/rental/services/ReservationService.java
package com.rental.services;

import com.rental.model.Client;
import com.rental.model.Reservation;
import com.rental.model.Voiture;
import com.rental.model.enums.ReservationStatus;
import com.rental.repositories.ReservationRepository;
import com.rental.repositories.ClientRepository;
import com.rental.repositories.VoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final VoitureRepository voitureRepository;
    private final ClientService clientService;
    private final NotificationService notificationService;

    // ============================================================
    // 1. Créer une réservation (SANS changer le statut de la voiture)
    // ============================================================
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        // Vérifier que la voiture existe
        Voiture voiture = voitureRepository.findById(reservation.getVoiture().getId())
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));

        // Vérifier que le client existe
        Client client = clientRepository.findById(reservation.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // Vérifier la disponibilité de la voiture sur la période
        if (!checkCarAvailability(voiture.getId(), reservation.getDateDebut(), reservation.getDateFin())) {
            throw new RuntimeException("La voiture n'est pas disponible sur cette période");
        }

        // Calculer le nombre de jours
        long days = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        reservation.setNombreJours((int) days);

        // Calculer le prix total
        reservation.setPrixParJour(voiture.getPrixParJour());
        reservation.setPrixTotal(voiture.getPrixParJour() * days);

        // Définir le statut initial
        reservation.setStatus(ReservationStatus.EN_ATTENTE);

        // Sauvegarder la réservation
        Reservation savedReservation = reservationRepository.save(reservation);

        // Incrémenter le compteur de réservations du client
        clientService.incrementReservationCount(client.getId());

        // Vérifier si c'est la 6ème réservation pour créer une notification admin
        if (clientService.isEligibleForRemise(client.getId())) {
            notificationService.createRemiseNotification(client, savedReservation);
        }

        // ✅ NE PAS CHANGER LE STATUT DE LA VOITURE
        return savedReservation;
    }

    // ============================================================
    // 2. Vérifier la disponibilité d'une voiture sur une période
    // ============================================================
    public boolean checkCarAvailability(Long voitureId, LocalDate dateDebut, LocalDate dateFin) {
        List<Reservation> reservations = reservationRepository.findReservationsForCarInPeriod(
                voitureId, dateDebut, dateFin);
        return reservations.isEmpty();
    }

    // ============================================================
    // 3. Vérifier si la voiture est actuellement louée
    // ============================================================
    public boolean isCarCurrentlyRented(Long voitureId) {
        LocalDate today = LocalDate.now();
        List<Reservation> activeReservations = reservationRepository.findReservationsForCarInPeriod(
                voitureId, today, today);

        for (Reservation r : activeReservations) {
            if (r.getStatus() != ReservationStatus.ANNULEE &&
                    r.getStatus() != ReservationStatus.TERMINEE) {
                return true;
            }
        }
        return false;
    }

    // ============================================================
    // 4. Récupérer les réservations actives (pour aujourd'hui)
    // ============================================================
    public List<Reservation> getActiveReservations() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findActiveReservationsForDate(today);
    }

    // ============================================================
    // 5. Récupérer les réservations par date
    // ============================================================
    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservationRepository.findReservationsForDate(date);
    }

    // ============================================================
    // 6. Récupérer toutes les réservations
    // ============================================================
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // ============================================================
    // 7. Récupérer une réservation par ID
    // ============================================================
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));
    }

    // ============================================================
    // 8. Récupérer les réservations par client
    // ============================================================
    public List<Reservation> getReservationsByClient(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    // ============================================================
    // 9. Récupérer les réservations par voiture
    // ============================================================
    public List<Reservation> getReservationsByVoiture(Long voitureId) {
        return reservationRepository.findByVoitureId(voitureId);
    }

    // ============================================================
    // 10. Récupérer les réservations par statut
    // ============================================================
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    // ============================================================
    // 11. Récupérer les réservations par période
    // ============================================================
    public List<Reservation> getReservationsByPeriod(LocalDate start, LocalDate end) {
        return reservationRepository.findByDateDebutBetween(start, end);
    }

    // ============================================================
    // 12. Récupérer les réservations à venir
    // ============================================================
    public List<Reservation> getUpcomingReservations() {
        return reservationRepository.findByDateDebutAfterAndStatus(LocalDate.now(), ReservationStatus.CONFIRMEE);
    }

    // ============================================================
    // 13. Mettre à jour le statut d'une réservation
    // ============================================================
    @Transactional
    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));

        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    // ============================================================
    // 14. Annuler une réservation
    // ============================================================
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));

        reservation.setStatus(ReservationStatus.ANNULEE);
        reservationRepository.save(reservation);
    }

    // ============================================================
    // 15. Recherche avancée
    // ============================================================
    public List<Reservation> advancedSearch(Long clientId, Long voitureId, LocalDate dateDebut, LocalDate dateFin) {
        // Implémentation simplifiée - à améliorer selon les besoins
        return reservationRepository.findAll();
    }
}