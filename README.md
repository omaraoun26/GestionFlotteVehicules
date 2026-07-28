# Gestion d'une flotte de véhicules

## Description

Cette application Java permet de gérer une flotte de véhicules de location.

Elle permet de charger les véhicules et leur historique de location depuis des fichiers CSV, de gérer la location, le retour et l'entretien des véhicules, puis de produire des statistiques sur les activités de la flotte.

---

## Membre du projet

- Omar Aoun
- Zied Gherissi

---

## Fonctionnalités développées

- Chargement des véhicules depuis un fichier CSV
- Chargement des locations depuis un fichier CSV
- Validation des données
- Gestion des exceptions personnalisées
- Gestion des locations
- Gestion des retours
- Gestion des entretiens
- Calcul du tarif selon le type de véhicule
- Calcul du revenu total
- Calcul du kilométrage moyen
- Calcul du tarif journalier moyen
- Calcul du taux d'utilisation par type
- Recherche du véhicule le plus utilisé
- Affichage des véhicules nécessitant un entretien
- Génération d'un rapport TXT

---

## Concepts utilisés

- Héritage
- Classe abstraite
- Interfaces
- Polymorphisme
- Redéfinition de méthodes
- ArrayList
- Exceptions personnalisées
- Lecture de fichiers CSV
- Écriture de fichiers TXT
- Gestion des erreurs avec try/catch
- Principes SOLID (SRP et OCP)

---

## Structure du projet

```
src/
└── omar/
    ├── exceptions/
    ├── interfaces/
    ├── modele/
    ├── services/
    └── Main.java
```

---

## Fichiers

- vehicules.csv
- locations.csv
- rapport_flotte.txt

---

## Exécution

1. Ouvrir le projet avec IntelliJ IDEA.
2. Vérifier que les fichiers CSV sont présents.
3. Exécuter Main.java.
4. Consulter la console.
5. Ouvrir rapport_flotte.txt.