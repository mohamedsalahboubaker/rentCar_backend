package com.rental.services;

import com.rental.model.Stock;
import com.rental.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    // Créer un nouveau type de stock
    @Transactional
    public Stock createStock(Stock stock) {
        if (stockRepository.findByType(stock.getType()).isPresent()) {
            throw new RuntimeException("Un stock de type '" + stock.getType() + "' existe déjà");
        }
        return stockRepository.save(stock);
    }

    // Récupérer tous les stocks
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // Récupérer un stock par type
    public Optional<Stock> getStockByType(String type) {
        return stockRepository.findByType(type);
    }

    // Ajouter des quantités
    @Transactional
    public Stock addQuantity(String type, Integer quantity) {
        Stock stock = stockRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Type de stock non trouvé: " + type));

        stock.setQuantite(stock.getQuantite() + quantity);
        return stockRepository.save(stock);
    }

    // Retirer des quantités
    @Transactional
    public Stock removeQuantity(String type, Integer quantity) {
        Stock stock = stockRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Type de stock non trouvé: " + type));

        if (stock.getQuantite() < quantity) {
            throw new RuntimeException("Quantité insuffisante en stock");
        }

        stock.setQuantite(stock.getQuantite() - quantity);
        return stockRepository.save(stock);
    }

    // Retirer 1 unité (pour essuie-glace, huile, eau radiateur)
    @Transactional
    public Stock removeOne(String type) {
        return removeQuantity(type, 1);
    }

    // Ajouter 2 unités (spécial pour eau essuie-glace)
    @Transactional
    public Stock addTwo(String type) {
        return addQuantity(type, 2);
    }

    // Mettre à jour le seuil d'alerte
    @Transactional
    public Stock updateSeuilAlerte(String type, Integer seuil) {
        Stock stock = stockRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Type de stock non trouvé: " + type));

        stock.setSeuilAlerte(seuil);
        return stockRepository.save(stock);
    }

    // Récupérer les stocks en alerte
    public List<Stock> getStockAlertes() {
        return stockRepository.findStockAlerte();
    }

    // Vérifier si un stock est en alerte
    public boolean isStockAlerte(String type) {
        Stock stock = stockRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Type de stock non trouvé: " + type));
        return stock.getQuantite() < stock.getSeuilAlerte();
    }

    // Supprimer un type de stock
    @Transactional
    public void deleteStock(String type) {
        Stock stock = stockRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Type de stock non trouvé: " + type));
        stockRepository.delete(stock);
    }
}