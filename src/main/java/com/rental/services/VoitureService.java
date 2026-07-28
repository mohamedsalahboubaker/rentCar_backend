package com.rental.services;

import com.rental.model.Voiture;
import com.rental.model.enums.Transmission;
import com.rental.model.enums.VoitureStatus;
import com.rental.repositories.VoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoitureService {

    private final VoitureRepository voitureRepository;

    // Créer une voiture
    @Transactional
    public Voiture createVoiture(Voiture voiture) {
        // Vérifier si le matricule existe déjà
        if (voitureRepository.findByMatricule(voiture.getMatricule()).isPresent()) {
            throw new RuntimeException("Une voiture avec ce matricule existe déjà");
        }
        return voitureRepository.save(voiture);
    }

    // Récupérer toutes les voitures
    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll();
    }

    // Récupérer une voiture par ID
    public Optional<Voiture> getVoitureById(Long id) {
        return voitureRepository.findById(id);
    }

    // Récupérer une voiture par matricule
    public Optional<Voiture> getVoitureByMatricule(String matricule) {
        return voitureRepository.findByMatricule(matricule);
    }

    // Rechercher des voitures par matricule (contient)
    public List<Voiture> searchVoituresByMatricule(String matricule) {
        return voitureRepository.findByMatriculeContaining(matricule);
    }

    // Rechercher des voitures par nom
    public List<Voiture> searchVoituresByNom(String nom) {
        return voitureRepository.findByNomContainingIgnoreCase(nom);
    }

    // Récupérer les voitures par statut
    public List<Voiture> getVoituresByStatus(VoitureStatus status) {
        return voitureRepository.findByStatus(status);
    }

    // Récupérer les voitures par transmission
    public List<Voiture> getVoituresByTransmission(Transmission transmission) {
        return voitureRepository.findByTransmission(transmission);
    }

    // Récupérer les voitures par statut et transmission
    public List<Voiture> getVoituresByStatusAndTransmission(VoitureStatus status, Transmission transmission) {
        return voitureRepository.findByStatusAndTransmission(status, transmission);
    }

    // Mettre à jour une voiture
    @Transactional
    public Voiture updateVoiture(Long id, Voiture voitureDetails) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));

        voiture.setMatricule(voitureDetails.getMatricule());
        voiture.setNom(voitureDetails.getNom());
        voiture.setPhotoUrl(voitureDetails.getPhotoUrl());
        voiture.setTransmission(voitureDetails.getTransmission());
        voiture.setStatus(voitureDetails.getStatus());
        voiture.setDernierVidange(voitureDetails.getDernierVidange());
        voiture.setPrixParJour(voitureDetails.getPrixParJour());
        voiture.setCautionRequise(voitureDetails.getCautionRequise());
        voiture.setMontantCaution(voitureDetails.getMontantCaution());

        return voitureRepository.save(voiture);
    }

    // Mettre à jour le statut d'une voiture
    @Transactional
    public Voiture updateVoitureStatus(Long id, VoitureStatus status) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
        voiture.setStatus(status);
        return voitureRepository.save(voiture);
    }

    // Mettre à jour la date de dernière vidange
    @Transactional
    public Voiture updateDernierVidange(Long id, LocalDate dateVidange) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
        voiture.setDernierVidange(dateVidange);
        return voitureRepository.save(voiture);
    }

    // Supprimer une voiture
    @Transactional
    public void deleteVoiture(Long id) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
        voitureRepository.delete(voiture);
    }

    // Vérifier si une voiture est disponible
    public boolean isVoitureDisponible(Long id) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
        return voiture.getStatus() == VoitureStatus.DISPONIBLE;
    }
}