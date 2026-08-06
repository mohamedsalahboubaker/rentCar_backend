package com.rental.controllers;

import com.rental.model.Voiture;
import com.rental.model.enums.Transmission;
import com.rental.model.enums.VoitureStatus;
import com.rental.services.VoitureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/voitures")
@RequiredArgsConstructor
public class VoitureController {

    private final VoitureService voitureService;

    @PostMapping
    public ResponseEntity<Voiture> createVoiture(@RequestBody Voiture voiture) {
        Voiture created = voitureService.createVoiture(voiture);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Voiture>> getAllVoitures() {
        List<Voiture> voitures = voitureService.getAllVoitures();
        return ResponseEntity.ok(voitures);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voiture> getVoitureById(@PathVariable Long id) {
        Voiture voiture = voitureService.getVoitureById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));
        return ResponseEntity.ok(voiture);
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Voiture> getVoitureByMatricule(@PathVariable String matricule) {
        Voiture voiture = voitureService.getVoitureByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));
        return ResponseEntity.ok(voiture);
    }

    @GetMapping("/search/matricule")
    public ResponseEntity<List<Voiture>> searchVoituresByMatricule(@RequestParam String matricule) {
        List<Voiture> voitures = voitureService.searchVoituresByMatricule(matricule);
        return ResponseEntity.ok(voitures);
    }

    @GetMapping("/search/nom")
    public ResponseEntity<List<Voiture>> searchVoituresByNom(@RequestParam String nom) {
        List<Voiture> voitures = voitureService.searchVoituresByNom(nom);
        return ResponseEntity.ok(voitures);
    }

    @GetMapping("/filter/status")
    public ResponseEntity<List<Voiture>> getVoituresByStatus(@RequestParam VoitureStatus status) {
        List<Voiture> voitures = voitureService.getVoituresByStatus(status);
        return ResponseEntity.ok(voitures);
    }

    @GetMapping("/filter/transmission")
    public ResponseEntity<List<Voiture>> getVoituresByTransmission(@RequestParam Transmission transmission) {
        List<Voiture> voitures = voitureService.getVoituresByTransmission(transmission);
        return ResponseEntity.ok(voitures);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Voiture>> getVoituresByStatusAndTransmission(
            @RequestParam VoitureStatus status,
            @RequestParam Transmission transmission) {
        List<Voiture> voitures = voitureService.getVoituresByStatusAndTransmission(status, transmission);
        return ResponseEntity.ok(voitures);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable Long id, @RequestBody Voiture voiture) {
        Voiture updated = voitureService.updateVoiture(id, voiture);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Voiture> updateVoitureStatus(
            @PathVariable Long id,
            @RequestParam VoitureStatus status) {
        Voiture updated = voitureService.updateVoitureStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/vidange")
    public ResponseEntity<Voiture> updateDernierVidange(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateVidange) {
        Voiture updated = voitureService.updateDernierVidange(id, dateVidange);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        voitureService.deleteVoiture(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/disponible")
    public ResponseEntity<Boolean> isVoitureDisponible(@PathVariable Long id) {
        boolean disponible = voitureService.isVoitureDisponible(id);
        return ResponseEntity.ok(disponible);
    }
}