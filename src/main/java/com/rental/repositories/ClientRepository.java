package com.rental.repositories;

import com.rental.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Recherche par téléphone (exact)
    Optional<Client> findByTelephone(String telephone);

    // Recherche par téléphone (contient)
    List<Client> findByTelephoneContaining(String telephone);

    // Recherche par nom (contient, insensible à la casse)
    List<Client> findByNomContainingIgnoreCase(String nom);

    // Recherche par nom ou prénom
    List<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    // Clients avec plus de X réservations
    List<Client> findByReservationCountGreaterThanEqual(Integer count);
}