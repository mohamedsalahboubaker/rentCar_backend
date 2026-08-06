package com.rental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rental.model.enums.Transmission;
import com.rental.model.enums.VoitureStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voiture")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String matricule;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private VoitureStatus status = VoitureStatus.DISPONIBLE;

    @Column(name = "dernier_vidange")
    private LocalDate dernierVidange;

    @Column(name = "prix_par_jour", nullable = false)
    private Double prixParJour;

    @Column(name = "caution_requise", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean cautionRequise = true;

    @Column(name = "montant_caution")
    private Double montantCaution;

    @Column(name = "nombre_places")
    private Integer nombrePlaces = 5;

    @Column(name = "nombre_portes")
    private Integer nombrePortes = 5;

    @Column(name = "nombre_bagages")
    private Integer nombreBagages = 4;

    @Column(name = "carburant", length = 50)
    private String carburant;

    @Column(name = "climatisation")
    private Boolean climatisation = true;

    @Column(name = "airbags")
    private Boolean airbags = true;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "voiture_images", joinColumns = @JoinColumn(name = "voiture_id"))
    @Column(name = "image_data", columnDefinition = "TEXT")
    private List<String> images = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "voiture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "voiture", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PieceReparee> piecesReparees = new ArrayList<>();

    // ✅ Constructeur avec ID pour les références
    public Voiture(Long id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}