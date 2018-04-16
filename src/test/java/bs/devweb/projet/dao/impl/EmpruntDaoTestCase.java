package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.dao.EmpruntDao;
import bs.devweb.projet.dao.EtudiantDao;
import bs.devweb.projet.dao.MaterielDao;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.provider.DataSourceProvider;
import org.assertj.core.internal.cglib.core.Local;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

public class EmpruntDaoTestCase {

    private EmpruntDao empruntDao = new EmpruntDaoImpl();
    private AssociationDao associationDao = new AssociationDaoImpl();
    private EtudiantDao etudiantDao = new EtudiantDaoImpl();
    private MaterielDao materielDao = new MaterielDaoImpl();

    @Before
    public void initDb() throws Exception {
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM emprunt");
            stmt.executeUpdate("DELETE FROM materiel");
            stmt.executeUpdate("DELETE FROM licencier;");
            stmt.executeUpdate("DELETE FROM gerer;");
            stmt.executeUpdate("DELETE FROM association");
            stmt.executeUpdate("DELETE FROM etudiant");
            stmt.executeUpdate("DELETE FROM lieu");
            stmt.executeUpdate("DELETE FROM statut");
            stmt.executeUpdate("INSERT INTO statut (statut_id, statut_nom) VALUES (1,'Responsable');");
            stmt.executeUpdate("INSERT INTO statut (statut_id, statut_nom) VALUES (2,'Sous-Responsable');");
            stmt.executeUpdate("INSERT INTO statut (statut_id, statut_nom) VALUES (3,'Visiteur');");
            stmt.executeUpdate("INSERT INTO lieu(lieu_id, lieu_nom) VALUES (1, 'Garage Jules Lefevre');");
            stmt.executeUpdate("INSERT INTO lieu(lieu_id, lieu_nom) VALUES (2, 'Local BDS');");
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (1,2,'SAMANOS', 'Mathilde', 'azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty','0648745614','mathilde.samanos@hei.yncrea.fr', 'true', 'fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320');");
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (2,1,'DEMORY', 'Thibaut', 'titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede','0648748448','thibaut.demory@hei.yncrea.fr', 'true', 'md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF');");
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (3,3,'EVRARD', 'William', 'aeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouy','0632847958','william.evrard@hei.yncrea.fr', 'true', 'lff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çf');");
            stmt.executeUpdate("INSERT INTO association(association_id, etudiant_id, association_nom) VALUES (1, 1, 'Rugby');");
            stmt.executeUpdate("INSERT INTO association(association_id, etudiant_id, association_nom) VALUES (2, 2, 'Foot Feminin');");
            stmt.executeUpdate("INSERT INTO materiel(materiel_id, lieu_id, association_id, designation, quantite, prix_achat) VALUES(1,1,1,'Ballons rugby', 20, 15.99);");
            stmt.executeUpdate("INSERT INTO materiel(materiel_id, lieu_id, association_id, designation, quantite, prix_achat) VALUES(2,1,2,'Crampons foot', 11, 31.0);");
            stmt.executeUpdate("INSERT INTO emprunt(emprunt_id, etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine, emprunt_demande) VALUES (1,2,2,5,'2018-02-21','2018-03-21', 'false', 'false', '2018-01-10');");
            stmt.executeUpdate("INSERT INTO emprunt(emprunt_id, etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine, emprunt_demande) VALUES (2,1,2,8,'2018-03-23','2018-04-11', 'false', 'false', '2018-02-20');");
            stmt.executeUpdate("INSERT INTO emprunt(emprunt_id, etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine, emprunt_demande) VALUES (3,1,1,8,'2018-03-23','2018-04-11', 'true', 'false', '2018-01-15');");
        }
    }

    @Test
    public void shouldListEmpruntByAssociation() throws Exception {
        // GIVEN
        Association association = associationDao.getAssociationById(2);
        // WHEN
        List<Emprunt> listEmprunt = empruntDao.listEmpruntByAssociation(association);
        // THEN
        assertThat(listEmprunt).hasSize(2);
        assertThat(listEmprunt).extracting("id",
                "etudiant.id", "etudiant.nom", "etudiant.prenom", "etudiant.motDePasse", "etudiant.telephone", "etudiant.mail",
                "etudiant.statut.id", "etudiant.statut.nom",
                "etudiant.verifie", "etudiant.motDePasseVerification",
                "materiel.id", "materiel.designation", "materiel.quantite", "materiel.prix",
                "materiel.lieu.id", "materiel.lieu.nom",
                "materiel.association.id", "materiel.association.nom",
                "materiel.association.etudiant.id", "materiel.association.etudiant.nom", "materiel.association.etudiant.prenom", "materiel.association.etudiant.motDePasse", "materiel.association.etudiant.telephone", "materiel.association.etudiant.mail",
                "materiel.association.etudiant.statut.id", "materiel.association.etudiant.statut.nom",
                "materiel.association.etudiant.verifie", "materiel.association.etudiant.motDePasseVerification",
                "quantite", "debut", "fin", "valide", "termine", "demande").containsOnly(
                tuple(1,
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        2, "Crampons foot", 11, 31.0,
                        1, "Garage Jules Lefevre",
                        2, "Foot Feminin",
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        5, LocalDate.of(2018,02,21), LocalDate.of(2018,03,21), false, false, LocalDate.of(2018,01,10)),
                tuple(2,
                        1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320",
                        2, "Crampons foot", 11, 31.0,
                        1, "Garage Jules Lefevre",
                        2, "Foot Feminin",
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        8, LocalDate.of(2018,03,23), LocalDate.of(2018,04,11), false, false, LocalDate.of(2018,02,20))
        );
    }

    @Test
    public void shouldGetEmpruntById() throws Exception {
        // WHEN
        Emprunt emprunt = empruntDao.getEmpruntById(1);
        // THEN
        assertThat(emprunt.getId()).isEqualTo(1);
        assertThat(emprunt.getEtudiant().getId()).isEqualTo(2);
        assertThat(emprunt.getEtudiant().getNom()).isEqualTo("DEMORY");
        assertThat(emprunt.getEtudiant().getPrenom()).isEqualTo("Thibaut");
        assertThat(emprunt.getEtudiant().getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(emprunt.getEtudiant().getTelephone()).isEqualTo("0648748448");
        assertThat(emprunt.getEtudiant().getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(emprunt.getEtudiant().getStatut().getId()).isEqualTo(1);
        assertThat(emprunt.getEtudiant().getStatut().getNom()).isEqualTo("Responsable");
        assertThat(emprunt.getEtudiant().isVerifie()).isTrue();
        assertThat(emprunt.getEtudiant().getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
        assertThat(emprunt.getMateriel().getId()).isEqualTo(2);
        assertThat(emprunt.getMateriel().getDesignation()).isEqualTo("Crampons foot");
        assertThat(emprunt.getMateriel().getQuantite()).isEqualTo(11);
        assertThat(emprunt.getMateriel().getPrix()).isEqualTo(31.0);
        assertThat(emprunt.getMateriel().getAssociation().getId()).isEqualTo(2);
        assertThat(emprunt.getMateriel().getAssociation().getNom()).isEqualTo("Foot Feminin");
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getId()).isEqualTo(2);
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getNom()).isEqualTo("DEMORY");
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getPrenom()).isEqualTo("Thibaut");
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getTelephone()).isEqualTo("0648748448");
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getStatut().getId()).isEqualTo(1);
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getStatut().getNom()).isEqualTo("Responsable");
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().isVerifie()).isTrue();
        assertThat(emprunt.getMateriel().getAssociation().getEtudiant().getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
        assertThat(emprunt.getMateriel().getLieu().getId()).isEqualTo(1);
        assertThat(emprunt.getMateriel().getLieu().getNom()).isEqualTo("Garage Jules Lefevre");
        assertThat(emprunt.getQuantite()).isEqualTo(5);
        assertThat(emprunt.getDebut()).isEqualTo(LocalDate.of(2018,02,21));
        assertThat(emprunt.getFin()).isEqualTo(LocalDate.of(2018,03,21));
        assertThat(emprunt.isValide()).isFalse();
        assertThat(emprunt.isTermine()).isFalse();
        assertThat(emprunt.getDemande()).isEqualTo(LocalDate.of(2018,01,10));
    }

    @Test
    public void shouldUpdateEmprunt() throws Exception {
        // GIVEN
        Emprunt emprunt = empruntDao.getEmpruntById(1);
        // WHEN
        emprunt.setValide(true);
        emprunt.setTermine(true);
        empruntDao.updateEmprunt(emprunt);
        Emprunt modifiedEmprunt = empruntDao.getEmpruntById(1);
        // THEN
        assertThat(modifiedEmprunt.getId()).isEqualTo(1);
        assertThat(modifiedEmprunt.getEtudiant().getId()).isEqualTo(2);
        assertThat(modifiedEmprunt.getEtudiant().getNom()).isEqualTo("DEMORY");
        assertThat(modifiedEmprunt.getEtudiant().getPrenom()).isEqualTo("Thibaut");
        assertThat(modifiedEmprunt.getEtudiant().getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(modifiedEmprunt.getEtudiant().getTelephone()).isEqualTo("0648748448");
        assertThat(modifiedEmprunt.getEtudiant().getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(modifiedEmprunt.getEtudiant().getStatut().getId()).isEqualTo(1);
        assertThat(modifiedEmprunt.getEtudiant().getStatut().getNom()).isEqualTo("Responsable");
        assertThat(modifiedEmprunt.getEtudiant().isVerifie()).isTrue();
        assertThat(modifiedEmprunt.getEtudiant().getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
        assertThat(modifiedEmprunt.getMateriel().getId()).isEqualTo(2);
        assertThat(modifiedEmprunt.getMateriel().getDesignation()).isEqualTo("Crampons foot");
        assertThat(modifiedEmprunt.getMateriel().getQuantite()).isEqualTo(11);
        assertThat(modifiedEmprunt.getMateriel().getPrix()).isEqualTo(31.0);
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getId()).isEqualTo(2);
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getNom()).isEqualTo("Foot Feminin");
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getId()).isEqualTo(2);
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getNom()).isEqualTo("DEMORY");
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getPrenom()).isEqualTo("Thibaut");
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getTelephone()).isEqualTo("0648748448");
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getStatut().getId()).isEqualTo(1);
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getStatut().getNom()).isEqualTo("Responsable");
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().isVerifie()).isTrue();
        assertThat(modifiedEmprunt.getMateriel().getAssociation().getEtudiant().getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
        assertThat(modifiedEmprunt.getMateriel().getLieu().getId()).isEqualTo(1);
        assertThat(modifiedEmprunt.getMateriel().getLieu().getNom()).isEqualTo("Garage Jules Lefevre");
        assertThat(modifiedEmprunt.getQuantite()).isEqualTo(5);
        assertThat(modifiedEmprunt.getDebut()).isEqualTo(LocalDate.of(2018,02,21));
        assertThat(modifiedEmprunt.getFin()).isEqualTo(LocalDate.of(2018,03,21));
        assertThat(modifiedEmprunt.isValide()).isTrue();
        assertThat(modifiedEmprunt.isTermine()).isTrue();
        assertThat(modifiedEmprunt.getDemande()).isEqualTo(LocalDate.of(2018,01,10));
    }

    @Test
    public void shouldDeleteEmprunt() throws Exception {
        // GIVEN
        Emprunt toBeDeleted = empruntDao.getEmpruntById(1);
        // WHEN
        empruntDao.deleteEmprunt(toBeDeleted);
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM emprunt WHERE emprunt_id = 1")) {
                assertThat(rs.next()).isFalse();
            }
        }
    }

    @Test
    public void shouldAddEmprunt() throws Exception {
        // GIVEN
        Emprunt emprunt = new Emprunt(null, etudiantDao.getEtudiantById(2), materielDao.getMaterielById(2),
                2, LocalDate.of(2018,02,15), LocalDate.of(2018,03,15),
                false, false, LocalDate.of(2018,02,10));
        // WHEN
        Emprunt addedEmprunt = empruntDao.addEmprunt(emprunt);
        // THEN
        assertThat(addedEmprunt).isNotNull();
        assertThat(addedEmprunt.getId()).isNotNull();
        assertThat(addedEmprunt.getId()).isGreaterThan(0);
        assertThat(addedEmprunt.getEtudiant().getId()).isEqualTo(2);
        assertThat(addedEmprunt.getEtudiant().getNom()).isEqualTo("DEMORY");
        assertThat(addedEmprunt.getEtudiant().getPrenom()).isEqualTo("Thibaut");
        assertThat(addedEmprunt.getEtudiant().getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(addedEmprunt.getEtudiant().getTelephone()).isEqualTo("0648748448");
        assertThat(addedEmprunt.getEtudiant().getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(addedEmprunt.getEtudiant().getStatut().getId()).isEqualTo(1);
        assertThat(addedEmprunt.getEtudiant().getStatut().getNom()).isEqualTo("Responsable");
        assertThat(addedEmprunt.getEtudiant().isVerifie()).isTrue();
        assertThat(addedEmprunt.getEtudiant().getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
        assertThat(addedEmprunt.getMateriel().getId()).isEqualTo(2);
        assertThat(addedEmprunt.getMateriel().getDesignation()).isEqualTo("Crampons foot");
        assertThat(addedEmprunt.getMateriel().getQuantite()).isEqualTo(11);
        assertThat(addedEmprunt.getMateriel().getPrix()).isEqualTo(31.0);
        assertThat(addedEmprunt.getMateriel().getAssociation().getId()).isEqualTo(2);
        assertThat(addedEmprunt.getMateriel().getAssociation().getNom()).isEqualTo("Foot Feminin");
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getId()).isEqualTo(2);
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getNom()).isEqualTo("DEMORY");
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getPrenom()).isEqualTo("Thibaut");
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getTelephone()).isEqualTo("0648748448");
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getStatut().getId()).isEqualTo(1);
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getStatut().getNom()).isEqualTo("Responsable");
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().isVerifie()).isTrue();
        assertThat(addedEmprunt.getMateriel().getAssociation().getEtudiant().getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
        assertThat(addedEmprunt.getMateriel().getLieu().getId()).isEqualTo(1);
        assertThat(addedEmprunt.getMateriel().getLieu().getNom()).isEqualTo("Garage Jules Lefevre");
        assertThat(addedEmprunt.getQuantite()).isEqualTo(2);
        assertThat(addedEmprunt.getDebut()).isEqualTo(LocalDate.of(2018,02,15));
        assertThat(addedEmprunt.getFin()).isEqualTo(LocalDate.of(2018,03,15));
        assertThat(addedEmprunt.isValide()).isFalse();
        assertThat(addedEmprunt.isTermine()).isFalse();
        assertThat(addedEmprunt.getDemande()).isEqualTo(LocalDate.of(2018,02,10));

        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM emprunt WHERE quantite = 2")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("emprunt_id")).isEqualTo(addedEmprunt.getId());
                assertThat(rs.getInt("etudiant_id")).isEqualTo(2);
                assertThat(rs.getInt("materiel_id")).isEqualTo(2);
                assertThat(rs.getDate("emprunt_debut").toLocalDate()).isEqualTo(LocalDate.of(2018,02,15));
                assertThat(rs.getDate("emprunt_fin").toLocalDate()).isEqualTo(LocalDate.of(2018,03,15));
                assertThat(rs.getBoolean("valide")).isFalse();
                assertThat(rs.getBoolean("termine")).isFalse();
                assertThat(rs.getDate("emprunt_demande").toLocalDate()).isEqualTo(LocalDate.of(2018,02,10));
                assertThat(rs.next()).isFalse();
            }
        }
    }

    @Test
    public void shouldListEmprunt() throws Exception {
        // WHEN
        List<Emprunt> listEmprunt = empruntDao.listEmprunt(true, false);
        // THEN
        assertThat(listEmprunt).hasSize(1);
        assertThat(listEmprunt).extracting("id",
                "etudiant.id", "etudiant.nom", "etudiant.prenom", "etudiant.motDePasse", "etudiant.telephone", "etudiant.mail",
                "etudiant.statut.id", "etudiant.statut.nom",
                "etudiant.verifie", "etudiant.motDePasseVerification",
                "materiel.id", "materiel.designation", "materiel.quantite", "materiel.prix",
                "materiel.lieu.id", "materiel.lieu.nom",
                "materiel.association.id", "materiel.association.nom",
                "materiel.association.etudiant.id", "materiel.association.etudiant.nom", "materiel.association.etudiant.prenom", "materiel.association.etudiant.motDePasse", "materiel.association.etudiant.telephone", "materiel.association.etudiant.mail",
                "materiel.association.etudiant.statut.id", "materiel.association.etudiant.statut.nom",
                "materiel.association.etudiant.verifie", "materiel.association.etudiant.motDePasseVerification",
                "quantite", "debut", "fin", "valide", "termine", "demande").containsOnly(
                tuple(3,
                        1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320",
                        1, "Ballons rugby", 20, 15.99,
                        1, "Garage Jules Lefevre",
                        1, "Rugby",
                        1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320",
                        8, LocalDate.of(2018,03,23), LocalDate.of(2018,04,11), true, false, LocalDate.of(2018,01,15))
        );
    }

    @Test
    public void shouldListEmpruntConcerningMateriel() throws Exception {
        // GIVEN
        Materiel materiel = materielDao.getMaterielById(2);
        // WHEN
        List<Emprunt> listEmprunt = empruntDao.listEmpruntConcerningMateriel(materiel);
        // THEN
        assertThat(listEmprunt).hasSize(2);
        assertThat(listEmprunt).extracting("id",
                "etudiant.id", "etudiant.nom", "etudiant.prenom", "etudiant.motDePasse", "etudiant.telephone", "etudiant.mail",
                "etudiant.statut.id", "etudiant.statut.nom",
                "etudiant.verifie", "etudiant.motDePasseVerification",
                "materiel.id", "materiel.designation", "materiel.quantite", "materiel.prix",
                "materiel.lieu.id", "materiel.lieu.nom",
                "materiel.association.id", "materiel.association.nom",
                "materiel.association.etudiant.id", "materiel.association.etudiant.nom", "materiel.association.etudiant.prenom", "materiel.association.etudiant.motDePasse", "materiel.association.etudiant.telephone", "materiel.association.etudiant.mail",
                "materiel.association.etudiant.statut.id", "materiel.association.etudiant.statut.nom",
                "materiel.association.etudiant.verifie", "materiel.association.etudiant.motDePasseVerification",
                "quantite", "debut", "fin", "valide", "termine", "demande").containsOnly(
                tuple(1,
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        2, "Crampons foot", 11, 31.0,
                        1, "Garage Jules Lefevre",
                        2, "Foot Feminin",
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        5, LocalDate.of(2018,02,21), LocalDate.of(2018,03,21), false, false, LocalDate.of(2018,01,10)),
                tuple(2,
                        1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320",
                        2, "Crampons foot", 11, 31.0,
                        1, "Garage Jules Lefevre",
                        2, "Foot Feminin",
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        8, LocalDate.of(2018,03,23), LocalDate.of(2018,04,11), false, false, LocalDate.of(2018,02,20))
        );
    }

    @Test
    public void shouldGetAvailability() throws Exception {
        // GIVEN
        Materiel materiel = materielDao.getMaterielById(2);
        // WHEN
        HashMap<LocalDate, Integer> availability = empruntDao.getAvailability(materiel, LocalDate.of(2018,02,20), LocalDate.of(2018,02,22));
        // THEN
        assertThat(availability).hasSize(3);
        assertThat(availability).containsKey(LocalDate.of(2018,02,20));
        assertThat(availability).containsKey(LocalDate.of(2018,02,21));
        assertThat(availability).containsKey(LocalDate.of(2018,02,22));
        assertThat(availability).containsValues(11);
        assertThat(availability).containsValues(6);
    }

    @Test
    public void shouldCheckPossibility() throws Exception {
        // GIVEN
        Emprunt empruntNotPossible = new Emprunt(4, etudiantDao.getEtudiantById(1), materielDao.getMaterielById(1),
                13, LocalDate.of(2018,02,20), LocalDate.of(2018,04,01),
                false, false, LocalDate.of(2018,02,10));
        Emprunt empruntPossible = new Emprunt(4, etudiantDao.getEtudiantById(1), materielDao.getMaterielById(1),
                12, LocalDate.of(2018,02,20), LocalDate.of(2018,04,01),
                false, false, LocalDate.of(2018,02,05));
        // WHEN
        boolean emprunt1 = empruntDao.checkPossibility(empruntNotPossible);
        boolean emprunt2 = empruntDao.checkPossibility(empruntPossible);
        // THEN
        assertThat(emprunt1).isFalse();
        assertThat(emprunt2).isTrue();
    }

}
