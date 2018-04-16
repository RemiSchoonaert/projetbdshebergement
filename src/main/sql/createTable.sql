CREATE TABLE statut (
  statut_id int NOT NULL PRIMARY KEY,
  statut_nom varchar(20) NOT NULL
);

CREATE TABLE etudiant (
  etudiant_id SERIAL NOT NULL PRIMARY KEY,
  statut_id int REFERENCES statut,
  etudiant_nom varchar(30) NOT NULL,
  prenom varchar(30) NOT NULL,
  mdp char(96) NOT NULL,
  mail varchar(50) NOT NULL,
  telephone char(10) NOT NULL,
  verifie varchar(5) NOT NULL,
  mdp_verification char(96) NOT NULL
);

CREATE TABLE association (
  association_id SERIAL NOT NULL PRIMARY KEY,
  etudiant_id int REFERENCES etudiant,
  association_nom varchar(30) NOT NULL
);

CREATE TABLE gerer (
  etudiant_id int REFERENCES etudiant,
  association_id int REFERENCES association
);

CREATE TABLE licencier (
  etudiant_id int REFERENCES etudiant,
  association_id int REFERENCES association
);

CREATE TABLE lieu (
  lieu_id int NOT NULL PRIMARY KEY,
  lieu_nom varchar(30) NOT NULL
);

CREATE TABLE materiel (
  materiel_id SERIAL NOT NULL PRIMARY KEY,
  lieu_id int NOT NULL REFERENCES lieu,
  association_id int NOT NULL REFERENCES association,
  designation varchar(50) NOT NULL,
  quantite int NOT NULL,
  prix_achat DECIMAL (10,2) NOT NULL
);

CREATE TABLE emprunt (
  emprunt_id SERIAL NOT NULL PRIMARY KEY ,
  etudiant_id int NOT NULL REFERENCES etudiant,
  materiel_id int NOT NULL REFERENCES materiel,
  quantite int NOT NULL,
  emprunt_debut date NOT NULL,
  emprunt_fin date NOT NULL,
  valide varchar(5) NOT NULL,
  termine varchar(5) NOT NULL,
  emprunt_demande date NOT NULL
);

/** Pour les tests on set la valeur à 10 pour eviter des cle primaires identiques avec les lignes ajoutees manuellement **/
select setval('association_association_id_seq', 10, true),
  setval('emprunt_emprunt_id_seq', 10, true),
  setval('etudiant_etudiant_id_seq', 10, true),
  setval('materiel_materiel_id_seq', 10, true)
;
