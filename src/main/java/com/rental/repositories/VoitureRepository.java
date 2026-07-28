package com.rental.repositories;



import com.rental.model.Voiture;
import com.rental.model.enums.Transmission;
import com.rental.model.enums.VoitureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {

    // Recherche par matricule (exact)
    Optional<Voiture> findByMatricule(String matricule);

    // Recherche par matricule (contient)
    List<Voiture> findByMatriculeContaining(String matricule);

    // Recherche par statut
    List<Voiture> findByStatus(VoitureStatus status);

    // Recherche par transmission
    List<Voiture> findByTransmission(Transmission transmission);

    // Recherche par statut et transmission
    List<Voiture> findByStatusAndTransmission(VoitureStatus status, Transmission transmission);

    // Recherche par nom (contient)
    List<Voiture> findByNomContainingIgnoreCase(String nom);
}
