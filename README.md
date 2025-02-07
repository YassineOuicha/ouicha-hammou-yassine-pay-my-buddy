# Modèle Physique de Données (MPD) - Pay My Buddy

Ce modèle physique de données (MPD) décrit la structure de la base de données utilisée pour l'application **Pay My Buddy**.

## Tables principales

La base de données est composée de trois principales tables : `user`, `transaction`, et `connections`. Chaque table contient des informations spécifiques, et des relations sont établies entre ces tables via des clés étrangères.

### 1. Table `user`

La table `user` contient des informations sur les utilisateurs de l'application.

#### Colonnes :
- **id** (`INT`, PRIMARY KEY, AUTO_INCREMENT) : Identifiant unique de l'utilisateur.
- **username** (`VARCHAR(100)`, NOT NULL) : Nom d'utilisateur.
- **email** (`VARCHAR(255)`, UNIQUE, NOT NULL) : Adresse email de l'utilisateur (doit être unique).
- **password** (`VARCHAR(255)`, NOT NULL) : Mot de passe de l'utilisateur (crypté pour des raisons de sécurité).

### 2. Table `transaction`

La table `transaction` contient des informations sur les transactions effectuées entre les utilisateurs.

#### Colonnes :
- **id** (`INT`, PRIMARY KEY, AUTO_INCREMENT) : Identifiant unique de la transaction.
- **sender_id** (`INT`, NOT NULL) : Identifiant de l'utilisateur qui envoie l'argent (clé étrangère faisant référence à la table `user`).
- **receiver_id** (`INT`, NOT NULL) : Identifiant de l'utilisateur qui reçoit l'argent (clé étrangère faisant référence à la table `user`).
- **description** (`TEXT`) : Description de la transaction.
- **amount** (`DOUBLE`, NOT NULL) : Montant de la transaction.

#### Relations :
- La colonne `sender_id` est une clé étrangère faisant référence à la colonne `id` de la table `user`.
- La colonne `receiver_id` est également une clé étrangère faisant référence à la colonne `id` de la table `user`.

### 3. Table `connections`

La table `connections` est utilisée pour suivre les relations d'amitié entre les utilisateurs.

#### Colonnes :
- **user_id** (`INT`, NOT NULL) : Identifiant de l'utilisateur (clé étrangère faisant référence à la table `user`).
- **friend_id** (`INT`, NOT NULL) : Identifiant de l'ami de l'utilisateur (clé étrangère faisant référence à la table `user`).

#### Clé primaire :
- La table utilise une clé primaire **composite** composée des colonnes `user_id` et `friend_id`.

#### Relations :
- La colonne `user_id` est une clé étrangère faisant référence à la colonne `id` de la table `user`.
- La colonne `friend_id` est une clé étrangère faisant référence à la colonne `id` de la table `user`.

## Relations entre les tables

### 4.1 Relation entre `user` et `transaction`
- Un utilisateur peut envoyer plusieurs transactions et peut également en recevoir plusieurs.
- Cela crée une relation de type **un-à-plusieurs** entre la table `user` et la table `transaction` à travers les colonnes `sender_id` et `receiver_id`.

### 4.2 Relation entre `user` et `connections`
- Un utilisateur peut avoir plusieurs amis.
- La table `connections` établit une relation **many-to-many** (plusieurs-à-plusieurs) entre les utilisateurs.
- Chaque ligne de cette table indique une relation d'amitié entre deux utilisateurs via `user_id` et `friend_id`.

## Contraintes de la base de données

### 5.1 Clés primaires
- **Table `user`** : La clé primaire est définie sur la colonne `id`.
- **Table `transaction`** : La clé primaire est définie sur la colonne `id`.
- **Table `connections`** : La clé primaire composite est formée par `user_id` et `friend_id`.

### 5.2 Clés étrangères
- **Table `transaction`** : Les colonnes `sender_id` et `receiver_id` font référence à la colonne `id` de la table `user`.
- **Table `connections`** : Les colonnes `user_id` et `friend_id` font également référence à la colonne `id` de la table `user`.

### Intégrité référentielle
Les relations entre les tables sont assurées par les clés étrangères, garantissant que les données restent cohérentes. Par exemple :
- Une transaction ne peut être effectuée que si les utilisateurs impliqués existent dans la table `user`.
- Une relation d'amitié ne peut être créée que pour des utilisateurs existants.

## Conclusion

Le modèle physique de données fournit une vue détaillée de la manière dont les informations sont stockées et reliées dans la base de données de l'application **Pay My Buddy**. L'architecture de la base de données garantit l'intégrité des données et la gestion efficace des relations entre les utilisateurs, leurs transactions et leurs connexions.
