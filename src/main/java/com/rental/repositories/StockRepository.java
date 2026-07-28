package com.rental.repositories;

import com.rental.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Recherche par type (exact)
    Optional<Stock> findByType(String type);

    // Recherche par type (contient)
    List<Stock> findByTypeContaining(String type);

    // Recherche des stocks en dessous du seuil d'alerte
    @Query("SELECT s FROM Stock s WHERE s.quantite < s.seuilAlerte")
    List<Stock> findStockAlerte();

    // Recherche des stocks avec quantité <= seuil
    List<Stock> findByQuantiteLessThanEqual(Integer seuil);
}