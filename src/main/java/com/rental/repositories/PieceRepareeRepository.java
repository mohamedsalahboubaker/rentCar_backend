package com.rental.repositories;



import com.rental.model.PieceReparee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PieceRepareeRepository extends JpaRepository<PieceReparee, Long> {

    // Récupérer toutes les pièces réparées d'une voiture
    List<PieceReparee> findByVoitureId(Long voitureId);

    // Récupérer les pièces réparées par date (plus récentes d'abord)
    List<PieceReparee> findByVoitureIdOrderByDateReparationDesc(Long voitureId);

    // Récupérer le total des réparations d'une voiture
    @Query("SELECT SUM(p.prix) FROM PieceReparee p WHERE p.voiture.id = :voitureId")
    Double sumPrixByVoitureId(Long voitureId);
}