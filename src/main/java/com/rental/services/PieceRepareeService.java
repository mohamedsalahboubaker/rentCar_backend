package com.rental.services;

import com.rental.model.PieceReparee;
import com.rental.model.Voiture;
import com.rental.repositories.PieceRepareeRepository;
import com.rental.repositories.VoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PieceRepareeService {

    private final PieceRepareeRepository pieceRepareeRepository;
    private final VoitureRepository voitureRepository;

    // Ajouter une pièce réparée à une voiture
    @Transactional
    public PieceReparee addPieceReparee(Long voitureId, PieceReparee pieceReparee) {
        Voiture voiture = voitureRepository.findById(voitureId)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + voitureId));

        pieceReparee.setVoiture(voiture);
        return pieceRepareeRepository.save(pieceReparee);
    }

    // Récupérer toutes les pièces réparées d'une voiture
    public List<PieceReparee> getPiecesRepareesByVoiture(Long voitureId) {
        return pieceRepareeRepository.findByVoitureIdOrderByDateReparationDesc(voitureId);
    }

    // Récupérer toutes les pièces réparées
    public List<PieceReparee> getAllPiecesReparees() {
        return pieceRepareeRepository.findAll();
    }

    // Récupérer une pièce réparée par ID
    public PieceReparee getPieceRepareeById(Long id) {
        return pieceRepareeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pièce réparée non trouvée avec l'ID: " + id));
    }

    // Mettre à jour une pièce réparée
    @Transactional
    public PieceReparee updatePieceReparee(Long id, PieceReparee pieceDetails) {
        PieceReparee piece = pieceRepareeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pièce réparée non trouvée avec l'ID: " + id));

        piece.setNomPiece(pieceDetails.getNomPiece());
        piece.setPrix(pieceDetails.getPrix());
        piece.setDateReparation(pieceDetails.getDateReparation());
        piece.setNotes(pieceDetails.getNotes());

        return pieceRepareeRepository.save(piece);
    }

    // Supprimer une pièce réparée
    @Transactional
    public void deletePieceReparee(Long id) {
        PieceReparee piece = pieceRepareeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pièce réparée non trouvée avec l'ID: " + id));
        pieceRepareeRepository.delete(piece);
    }

    // Calculer le total des réparations d'une voiture
    public Double getTotalReparationsByVoiture(Long voitureId) {
        Double total = pieceRepareeRepository.sumPrixByVoitureId(voitureId);
        return total != null ? total : 0.0;
    }
}