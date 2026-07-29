package com.rental.controllers;

import com.rental.model.PieceReparee;
import com.rental.services.PieceRepareeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pieces-reparees")
@RequiredArgsConstructor
public class PieceRepareeController {

    private final PieceRepareeService pieceRepareeService;

    // Ajouter une pièce réparée à une voiture
    @PostMapping("/voiture/{voitureId}")
    public ResponseEntity<PieceReparee> addPieceReparee(
            @PathVariable Long voitureId,
            @RequestBody PieceReparee pieceReparee) {
        PieceReparee created = pieceRepareeService.addPieceReparee(voitureId, pieceReparee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Récupérer toutes les pièces réparées d'une voiture
    @GetMapping("/voiture/{voitureId}")
    public ResponseEntity<List<PieceReparee>> getPiecesByVoiture(@PathVariable Long voitureId) {
        List<PieceReparee> pieces = pieceRepareeService.getPiecesRepareesByVoiture(voitureId);
        return ResponseEntity.ok(pieces);
    }

    // Récupérer toutes les pièces réparées
    @GetMapping
    public ResponseEntity<List<PieceReparee>> getAllPieces() {
        List<PieceReparee> pieces = pieceRepareeService.getAllPiecesReparees();
        return ResponseEntity.ok(pieces);
    }

    // Récupérer une pièce réparée par ID
    @GetMapping("/{id}")
    public ResponseEntity<PieceReparee> getPieceById(@PathVariable Long id) {
        PieceReparee piece = pieceRepareeService.getPieceRepareeById(id);
        return ResponseEntity.ok(piece);
    }

    // Mettre à jour une pièce réparée
    @PutMapping("/{id}")
    public ResponseEntity<PieceReparee> updatePiece(@PathVariable Long id, @RequestBody PieceReparee piece) {
        PieceReparee updated = pieceRepareeService.updatePieceReparee(id, piece);
        return ResponseEntity.ok(updated);
    }

    // Supprimer une pièce réparée
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePiece(@PathVariable Long id) {
        pieceRepareeService.deletePieceReparee(id);
        return ResponseEntity.noContent().build();
    }

    // Calculer le total des réparations d'une voiture
    @GetMapping("/voiture/{voitureId}/total")
    public ResponseEntity<Double> getTotalReparations(@PathVariable Long voitureId) {
        Double total = pieceRepareeService.getTotalReparationsByVoiture(voitureId);
        return ResponseEntity.ok(total);
    }
}