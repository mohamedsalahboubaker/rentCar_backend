package com.rental.controllers;

import com.rental.model.Stock;
import com.rental.model.enums.StockType;
import com.rental.services.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // Créer un nouveau type de stock
    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        Stock created = stockService.createStock(stock);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Récupérer tous les stocks
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    // Récupérer un stock par type
    @GetMapping("/{type}")
    public ResponseEntity<Stock> getStockByType(@PathVariable StockType type) {
        Stock stock = stockService.getStockByType(type)
                .orElseThrow(() -> new RuntimeException("Type de stock non trouvé"));
        return ResponseEntity.ok(stock);
    }

    // Ajouter des quantités
    @PostMapping("/{type}/add")
    public ResponseEntity<Stock> addQuantity(
            @PathVariable StockType type,
            @RequestParam Integer quantity) {
        Stock updated = stockService.addQuantity(type, quantity);
        return ResponseEntity.ok(updated);
    }

    // Ajouter 2 unités (spécial pour eau essuie-glace)
    @PostMapping("/{type}/add-two")
    public ResponseEntity<Stock> addTwo(@PathVariable StockType type) {
        Stock updated = stockService.addTwo(type);
        return ResponseEntity.ok(updated);
    }

    // Retirer des quantités
    @PostMapping("/{type}/remove")
    public ResponseEntity<Stock> removeQuantity(
            @PathVariable StockType type,
            @RequestParam Integer quantity) {
        Stock updated = stockService.removeQuantity(type, quantity);
        return ResponseEntity.ok(updated);
    }

    // Retirer 1 unité
    @PostMapping("/{type}/remove-one")
    public ResponseEntity<Stock> removeOne(@PathVariable StockType type) {
        Stock updated = stockService.removeOne(type);
        return ResponseEntity.ok(updated);
    }

    // Mettre à jour le seuil d'alerte
    @PutMapping("/{type}/seuil")
    public ResponseEntity<Stock> updateSeuilAlerte(
            @PathVariable StockType type,
            @RequestParam Integer seuil) {
        Stock updated = stockService.updateSeuilAlerte(type, seuil);
        return ResponseEntity.ok(updated);
    }

    // Récupérer les stocks en alerte
    @GetMapping("/alertes")
    public ResponseEntity<List<Stock>> getStockAlertes() {
        List<Stock> alertes = stockService.getStockAlertes();
        return ResponseEntity.ok(alertes);
    }

    // Vérifier si un stock est en alerte
    @GetMapping("/{type}/alerte")
    public ResponseEntity<Boolean> isStockAlerte(@PathVariable StockType type) {
        boolean isAlerte = stockService.isStockAlerte(type);
        return ResponseEntity.ok(isAlerte);
    }

    // Supprimer un type de stock
    @DeleteMapping("/{type}")
    public ResponseEntity<Void> deleteStock(@PathVariable StockType type) {
        stockService.deleteStock(type);
        return ResponseEntity.noContent().build();
    }
}