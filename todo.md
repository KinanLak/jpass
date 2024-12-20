# Descriptif Détaillé du Projet de Gestionnaire de Mots de Passe

## Objectif Global

Développer une application de gestion de mots de passe sécurisée et conviviale, inspirée de 1Password, avec une interface minimaliste et élégante. Inspiration 1Password

## Types d'Entrées à Gérer

1. **Mots de Passe Web**

    - Titre/Description
    - URL du site
    - Nom d'utilisateur
    - Mot de passe
    - Identifiant unique
    - Favicon automatique
    - Date d'expiration
    - Notes supplémentaires
    - Catégories/Tags

2. **Cartes Bancaires**

    - Nom de la banque
    - Numéro de carte
    - Date d'expiration
    - CVV (crypté)
    - Nom du titulaire
    - Type de carte (Visa, Mastercard, etc.)

3. **Identités**

    - Nom complet
    - Date de naissance
    - Adresse
    - Numéro de téléphone
    - Email
    - Numéro de passeport/CNI
    - Autres documents d'identité

4. **Notes Sécurisées**
    - Titre
    - Contenu texte
    - Catégorie
    - Date de création

## Fonctionnalités Principales

### Authentification

-   Écran de connexion principal
-   Mot de passe principal fort
-   Option de récupération (question secrète)
-   Verrouillage après X tentatives (plus tard)
-   Option de déconnexion automatique

### Gestion des Entrées

-   Ajout de nouvelles entrées
-   Modification d'entrées existantes
-   Suppression d'entrées
-   Recherche et filtrage
-   Copier mot de passe/identifiants avec un clic

### Sécurité

-   Cryptage AES-256 des données
-   Stockage sécurisé dans un fichier
-   Clé de cryptage dérivée du mot de passe principal
-   Génération de mots de passe forts
-   Détection de mots de passe faibles/réutilisés

### Interface Utilisateur

-   Design minimaliste type macOS/iOS
-   Palette de couleurs sobre (noir, blanc, gris) avec couleur d'accentuation #8b39ff
-   Icônes élégantes et modernes
-   Mode sombre/clair (plus tard)
-   Animations douces lors des transitions (plus tard)

### Fonctionnalités Avancées

-   Import/Export sécurisé
-   Synchronisation cloud optionnelle (plus tard)
-   Alertes pour mots de passe expirés
-   Vérification de la force des mots de passe
-   Historique des modifications

## Architecture Technique Recommandée

### Design Patterns

-   **Singleton** pour la gestion de configuration
-   **Factory** pour création des différents types d'entrées
-   **Strategy** pour les algorithmes de cryptage
-   **Observer** pour les notifications

### Structure du Projet

-   `model/`: Classes de données
-   `service/`: Logique métier
-   `ui/`: Interfaces graphiques Swing
-   `security/`: Fonctions de cryptage
-   `utils/`: Outils utilitaires

### Technologies

-   Java 21+
-   Swing pour l'UI
-   Bibliothèque de cryptage (javax.crypto)
-   JSON pour la sérialisation
-   Bibliothèque de génération de favicon

## Détails Techniques Spécifiques

### Récupération de Favicon

-   Utiliser l'API https://icon.horse/icon/domaine.com
-   Méthode de fallback vers `domaine.com/favicon.ico` en utilisant des requêtes HTTP
-   Mise en cache des icônes
-   Option de téléchargement/upload manuel

### Cryptage

-   Utilisation d'AES-256
-   Sel unique par entrée
-   Dérivation de clé PBKDF2
-   Rotation des clés de cryptage (plus tard)

### Stockage

-   Fichier de configuration chiffré
-   Format JSON
-   Sauvegarde incrémentale
-   Gestion des versions

## Contraintes à Respecter

-   Utilisation exclusive de Swing
-   Documentation Javadoc complète
-   Génération d'un fichier JAR exécutable
-   Performances optimisées

## Points d'Amélioration Potentiels (Plus Tard)

-   Support de l'authentification biométrique
-   Intégration avec gestionnaires de mots de passe tiers
-   Extensions navigateur

## Livrables Attendus

1. Code source complet
2. Fichier JAR
3. Documentation Javadoc
4. README technique
5. Présentation du projet

Cette spécification offre un cadre détaillé et technique pour le développement d'un gestionnaire de mots de passe robuste et moderne, tout en respectant les contraintes initiales du projet.
