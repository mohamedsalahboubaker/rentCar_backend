package com.rental.services;

import com.rental.model.Voiture;
import com.rental.model.enums.Transmission;
import com.rental.model.enums.VoitureStatus;
import com.rental.repositories.VoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoitureService {

    private final VoitureRepository voitureRepository;

    @Transactional
    public Voiture createVoiture(Voiture voiture) {
        if (voitureRepository.findByMatricule(voiture.getMatricule()).isPresent()) {
            throw new RuntimeException("Une voiture avec ce matricule existe déjà");
        }
        // Définir les valeurs par défaut
        if (voiture.getNombrePlaces() == null) voiture.setNombrePlaces(5);
        if (voiture.getNombrePortes() == null) voiture.setNombrePortes(5);
        if (voiture.getNombreBagages() == null) voiture.setNombreBagages(4);
        if (voiture.getClimatisation() == null) voiture.setClimatisation(true);
        if (voiture.getAirbags() == null) voiture.setAirbags(true);

        // ✅ Initialiser la liste des images
        if (voiture.getImages() == null) {
            voiture.setImages(new ArrayList<>());
        }

        // ✅ Log pour vérifier les images reçues
        System.out.println("📸 Nombre d'images reçues: " + voiture.getImages().size());

        return voitureRepository.save(voiture);
    }

    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll();
    }

    public Optional<Voiture> getVoitureById(Long id) {
        return voitureRepository.findById(id);
    }

    public Optional<Voiture> getVoitureByMatricule(String matricule) {
        return voitureRepository.findByMatricule(matricule);
    }

    public List<Voiture> searchVoituresByMatricule(String matricule) {
        return voitureRepository.findByMatriculeContaining(matricule);
    }

    public List<Voiture> searchVoituresByNom(String nom) {
        return voitureRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Voiture> getVoituresByStatus(VoitureStatus status) {
        return voitureRepository.findByStatus(status);
    }

    public List<Voiture> getVoituresByTransmission(Transmission transmission) {
        return voitureRepository.findByTransmission(transmission);
    }

    public List<Voiture> getVoituresByStatusAndTransmission(VoitureStatus status, Transmission transmission) {
        return voitureRepository.findByStatusAndTransmission(status, transmission);
    }

    @Transactional
    public Voiture updateVoiture(Long id, Voiture voitureDetails) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));

        voiture.setMatricule(voitureDetails.getMatricule());
        voiture.setNom(voitureDetails.getNom());
        // ❌ Supprimé : voiture.setPhotoUrl(...) — le champ photoUrl n'existe plus.
        // Les photos sont désormais gérées uniquement via `images` (base64), ci-dessous.
        voiture.setTransmission(voitureDetails.getTransmission());
        voiture.setStatus(voitureDetails.getStatus());
        voiture.setDernierVidange(voitureDetails.getDernierVidange());
        voiture.setPrixParJour(voitureDetails.getPrixParJour());
        voiture.setCautionRequise(voitureDetails.getCautionRequise());
        voiture.setMontantCaution(voitureDetails.getMontantCaution());

        // Nouveaux champs
        voiture.setNombrePlaces(voitureDetails.getNombrePlaces());
        voiture.setNombrePortes(voitureDetails.getNombrePortes());
        voiture.setNombreBagages(voitureDetails.getNombreBagages());
        voiture.setCarburant(voitureDetails.getCarburant());
        voiture.setClimatisation(voitureDetails.getClimatisation());
        voiture.setAirbags(voitureDetails.getAirbags());
        voiture.setDescription(voitureDetails.getDescription());

        // ✅ Mettre à jour les images
        if (voitureDetails.getImages() != null) {
            voiture.setImages(voitureDetails.getImages());
        } else if (voiture.getImages() == null) {
            voiture.setImages(new ArrayList<>());
        }

        return voitureRepository.save(voiture);
    }

    @Transactional
    public Voiture updateVoitureStatus(Long id, VoitureStatus status) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
        voiture.setStatus(status);
        return voitureRepository.save(voiture);
    }

    @Transactional
    public Voiture updateDernierVidange(Long id, LocalDate dateVidange) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
        voiture.setDernierVidange(dateVidange);
        return voitureRepository.save(voiture);
    }

    @Transactional
    public void deleteVoiture(Long id) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
        voitureRepository.delete(voiture);
    }

    public boolean isVoitureDisponible(Long id) {
        Voiture voiture = voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
        return voiture.getStatus() == VoitureStatus.DISPONIBLE;
    }
}