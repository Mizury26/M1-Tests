Feature: Gestion des livres
(mais )  Scenario: Ajouter un livre
    When j'ajoute un livre avec le titre "Clean Code" et l'auteur "Robert Martin"
    Then le code retour est 201

  Scenario: Récupérer les livres
    When je récupère la liste des livres
    Then le code retour est 200

  Scenario: Réserver un livre avec succès
    Given j'ajoute un livre avec le titre "Clean Architecture" et l'auteur "Robert Martin"
    When je réserve ce livre via son ID
    Then le code retour est 200

  Scenario: Tenter de réserver un livre déjà réservé
    Given j'ajoute un livre avec le titre "Refactoring" et l'auteur "Martin Fowler"
    And je réserve ce livre via son ID
    When je réserve ce livre via son ID
    Then le code retour est 412
