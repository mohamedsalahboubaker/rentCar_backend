package com.rental.services;

import com.rental.model.Client;
import com.rental.model.Reservation;
import com.rental.model.Voiture;
import com.rental.model.enums.ReservationStatus;
import com.rental.model.enums.VoitureStatus;
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

    // Créer une réservation
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        // Vérifier que la voiture existe
        Voiture voiture = voitureRepository.findById(reservation.getVoiture().getId())
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));

        // Vérifier que le client existe
        Client client = clientRepository.findById(reservation.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // Vérifier la disponibilité de la voiture
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

        // Mettre à jour le statut de la voiture
        voiture.setStatus(VoitureStatus.LOUE);
        voitureRepository.save(voiture);

        return savedReservation;
    }

    // Vérifier la disponibilité d'une voiture
    public boolean checkCarAvailability(Long voitureId, LocalDate dateDebut, LocalDate dateFin) {
        List<Reservation> reservations = reservationRepository.findReservationsForCarInPeriod(
                voitureId, dateDebut, dateFin);
        return reservations.isEmpty();
    }

    // Récupérer toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Récupérer une réservation par ID
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));
    }

    // Récupérer les réservations par client
    public List<Reservation> getReservationsByClient(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    // Récupérer les réservations par voiture
    public List<Reservation> getReservationsByVoiture(Long voitureId) {
        return reservationRepository.findByVoitureId(voitureId);
    }

    // Récupérer les réservations par statut
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    // Récupérer les réservations par période
    public List<Reservation> getReservationsByPeriod(LocalDate start, LocalDate end) {
        return reservationRepository.findByDateDebutBetween(start, end);
    }

    // Mettre à jour le statut d'une réservation
    @Transactional
    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));

        reservation.setStatus(status);

        // Si la réservation est annulée ou terminée, remettre la voiture disponible
        if (status == ReservationStatus.ANNULEE || status == ReservationStatus.TERMINEE) {
            Voiture voiture = reservation.getVoiture();
            voiture.setStatus(VoitureStatus.DISPONIBLE);
            voitureRepository.save(voiture);
        }

        return reservationRepository.save(reservation);
    }

    // Annuler une réservation
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));

        reservation.setStatus(ReservationStatus.ANNULEE);

        // Remettre la voiture disponible
        Voiture voiture = reservation.getVoiture();
        voiture.setStatus(VoitureStatus.DISPONIBLE);
        voitureRepository.save(voiture);

        reservationRepository.save(reservation);
    }

    // Récupérer les réservations à venir
    public List<Reservation> getUpcomingReservations() {
        return reservationRepository.findByDateDebutAfterAndStatus(LocalDate.now(), ReservationStatus.CONFIRMEE);
    }

    // Recherche avancée
    public List<Reservation> advancedSearch(Long clientId, Long voitureId, LocalDate dateDebut, LocalDate dateFin) {
        // Implémentation d'une recherche avancée
        // À compléter selon les besoins
        return reservationRepository.findAll();
    }
}