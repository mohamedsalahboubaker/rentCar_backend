package com.rental.services;

import com.rental.model.Client;
import com.rental.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    // Créer un client
    @Transactional
    public Client createClient(Client client) {
        // Vérifier si le téléphone existe déjà
        if (clientRepository.findByTelephone(client.getTelephone()).isPresent()) {
            throw new RuntimeException("Un client avec ce numéro de téléphone existe déjà");
        }
        // Initialiser reservationCount à 0 si null
        if (client.getReservationCount() == null) {
            client.setReservationCount(0);
        }
        return clientRepository.save(client);
    }

    // Récupérer tous les clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Récupérer un client par ID
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    // Récupérer un client par téléphone
    public Optional<Client> getClientByTelephone(String telephone) {
        return clientRepository.findByTelephone(telephone);
    }

    // Rechercher des clients par nom
    public List<Client> searchClientsByNom(String nom) {
        return clientRepository.findByNomContainingIgnoreCase(nom);
    }

    // Rechercher des clients par téléphone (contient)
    public List<Client> searchClientsByTelephone(String telephone) {
        return clientRepository.findByTelephoneContaining(telephone);
    }

    // Rechercher par nom ou prénom
    public List<Client> searchClientsByNomOrPrenom(String nom, String prenom) {
        return clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, prenom);
    }

    // Mettre à jour un client
    @Transactional
    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));

        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setTelephone(clientDetails.getTelephone());
        client.setEmail(clientDetails.getEmail());
        client.setRadar(clientDetails.getRadar());
        client.setDegat(clientDetails.getDegat());

        return clientRepository.save(client);
    }

    // Supprimer un client
    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));
        clientRepository.delete(client);
    }

    // Incrémenter le compteur de réservations
    @Transactional
    public void incrementReservationCount(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + clientId));

        // Si le compteur est null, l'initialiser à 0
        if (client.getReservationCount() == null) {
            client.setReservationCount(0);
        }
        client.setReservationCount(client.getReservationCount() + 1);
        clientRepository.save(client);
    }

    // Vérifier si le client est éligible pour une remise (6ème réservation)
    public boolean isEligibleForRemise(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + clientId));

        // Si null, retourner false
        if (client.getReservationCount() == null) {
            return false;
        }
        return client.getReservationCount() >= 6;
    }
}