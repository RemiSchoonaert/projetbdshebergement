DELETE FROM emprunt;
DELETE FROM materiel;
DELETE FROM licencier;
DELETE FROM gerer;
DELETE FROM association;
DELETE FROM etudiant;
DELETE FROM lieu;
DELETE FROM statut;

INSERT INTO statut (statut_id, statut_nom) VALUES (1,'Responsable');
INSERT INTO statut (statut_id, statut_nom) VALUES (2,'Sous-Responsable');
INSERT INTO statut (statut_id, statut_nom) VALUES (3,'Visiteur');

INSERT INTO lieu(lieu_id, lieu_nom) VALUES (1, 'Garage Jules Lefevre');
INSERT INTO lieu(lieu_id, lieu_nom) VALUES (2, 'Local BDS');

INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (1,1,'SAMANOS', 'Mathilde', '$argon2i$v=19$m=65536,t=2,p=1$dRZ9qQVbKH37NMneVFNd0g$aTHswq+I7deVcfTCLJtCmHh0NYdWcwjscvYYDjJ8wXc','0648745614','remi.schoonaert@gmail.com', 'true', '$argon2i$v=19$m=65536,t=2,p=1$dRZ9qQVbKH37NMneVFNd0g$aTHswq+I7deVcfTCLJtCmHh0NYdWcwjscvYYDjJ8wXc');
INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (2,1,'DEMORY', 'Thibaut', '$argon2i$v=19$m=65536,t=2,p=1$zcFZxrjwnDR5d35qF/BIzg$PxzGTCxguVwStp/DtLcQFbBc0DOA0IHvJ6p9o7zcSco','0648748448','nitreur@gmail.com', 'true', '$argon2i$v=19$m=65536,t=2,p=1$dRZ9qQVbKH37NMneVFNd0g$aTHswq+I7deVcfTCLJtCmHh0NYdWcwjscvYYDjJ8wXc');
INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (3,2,'EVRARD', 'William', '$argon2i$v=19$m=65536,t=2,p=1$GbQTUEGDFgQsoPa40u5Kbg$yGv2OBCiZKFsBTq0XI6HgmvHaH3ab9kvWNRLa/s3wDo','0632847958','none@hei.yncrea.fr', 'true', '$argon2i$v=19$m=65536,t=2,p=1$dRZ9qQVbKH37NMneVFNd0g$aTHswq+I7deVcfTCLJtCmHh0NYdWcwjscvYYDjJ8wXc');

INSERT INTO association(association_id, etudiant_id, association_nom) VALUES (1, 2, 'Rugby');
INSERT INTO association(association_id, etudiant_id, association_nom) VALUES (2, 1, 'Foot Feminin');

INSERT INTO licencier(etudiant_id, association_id) VALUES (1,2);
INSERT INTO licencier(etudiant_id, association_id) VALUES (2,1);
INSERT INTO licencier(etudiant_id, association_id) VALUES (2,2);
INSERT INTO licencier(etudiant_id, association_id) VALUES (3,1);

INSERT INTO gerer(etudiant_id, association_id) VALUES (1,2);
INSERT INTO gerer(etudiant_id, association_id) VALUES (2,1);
INSERT INTO gerer(etudiant_id, association_id) VALUES (3,1);

INSERT INTO materiel(materiel_id, lieu_id, association_id, designation, quantite, prix_achat) VALUES(1,1,1,'Ballons rugby', 20, 15.99);
INSERT INTO materiel(materiel_id, lieu_id, association_id, designation, quantite, prix_achat) VALUES(2,1,2,'Crampons foot', 11, 31.0);

INSERT INTO emprunt(emprunt_id, etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine) VALUES (1,3,1,5,'2018-02-21','2018-03-21', 'false', 'false');
INSERT INTO emprunt(emprunt_id, etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine) VALUES (2,3,2,8,'2018-03-23','2018-04-11', 'false', 'false');

