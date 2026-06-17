Feature: Gestion des livres

  Scenario: Ajouter un livre

    When j'ajoute un livre avec le titre "Clean Code" et l'auteur "Robert Martin"

    Then le code retour est 201

  Scenario: Récupérer les livres

    When je récupère la liste des livres

    Then le code retour est 200