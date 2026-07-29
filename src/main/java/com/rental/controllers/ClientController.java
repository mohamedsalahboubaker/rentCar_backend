package com.rental.controllers;

import com.rental.model.Client;
import com.rental.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // Créer un client
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client created = clientService.createClient(client);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Récupérer tous les clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Récupérer un client par ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return ResponseEntity.ok(client);
    }

    // Rechercher un client par téléphone
    @GetMapping("/telephone/{telephone}")
    public ResponseEntity<Client> getClientByTelephone(@PathVariable String telephone) {
        Client client = clientService.getClientByTelephone(telephone)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return ResponseEntity.ok(client);
    }

    // Rechercher des clients par nom
    @GetMapping("/search/nom")
    public ResponseEntity<List<Client>> searchClientsByNom(@RequestParam String nom) {
        List<Client> clients = clientService.searchClientsByNom(nom);
        return ResponseEntity.ok(clients);
    }

    // Rechercher des clients par téléphone (contient)
    @GetMapping("/search/telephone")
    public ResponseEntity<List<Client>> searchClientsByTelephone(@RequestParam String telephone) {
        List<Client> clients = clientService.searchClientsByTelephone(telephone);
        return ResponseEntity.ok(clients);
    }

    // Mettre à jour un client
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client updated = clientService.updateClient(id, client);
        return ResponseEntity.ok(updated);
    }

    // Supprimer un client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // Vérifier si un client est éligible pour une remise
    @GetMapping("/{id}/eligible-remise")
    public ResponseEntity<Boolean> isEligibleForRemise(@PathVariable Long id) {
        boolean eligible = clientService.isEligibleForRemise(id);
        return ResponseEntity.ok(eligible);
    }
}