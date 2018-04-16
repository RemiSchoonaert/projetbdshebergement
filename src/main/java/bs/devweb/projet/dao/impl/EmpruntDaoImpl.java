package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.EmpruntDao;
import bs.devweb.projet.dao.EtudiantDao;
import bs.devweb.projet.dao.MaterielDao;
import bs.devweb.projet.entities.*;
import bs.devweb.projet.provider.DataSourceProvider;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Cette classe implemente les methodes de l'interface EmpruntDao
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class EmpruntDaoImpl implements EmpruntDao{

    private EtudiantDao etudiantDao = new EtudiantDaoImpl();
    private MaterielDao materielDao = new MaterielDaoImpl();

    /**
     * Cette methode prend en parametre une association et retourne les emprunts qui la concernent
     * @param association est l'association dont on souhaite connaitre les emprunts la concernant
     * @return les emprunts concernant l'association
     */
    @Override
    public List<Emprunt> listEmpruntByAssociation(Association association) {
        String query = "SELECT * FROM emprunt INNER JOIN materiel ON emprunt.materiel_id = materiel.materiel_id WHERE association_id = ?;";
        List<Emprunt> listEmprunt = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, association.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    listEmprunt.add(new Emprunt(
                            resultSet.getInt("emprunt_id"),
                            etudiantDao.getEtudiantById(resultSet.getInt("etudiant_id")),
                            materielDao.getMaterielById(resultSet.getInt("materiel_id")),
                            resultSet.getInt("quantite"),
                            resultSet.getDate("emprunt_debut").toLocalDate(),
                            resultSet.getDate("emprunt_fin").toLocalDate(),
                            resultSet.getBoolean("valide"),
                            resultSet.getBoolean("termine"),
                            resultSet.getDate("emprunt_demande").toLocalDate()
                    )
                );
                }
                return listEmprunt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode prend en entree un emprunt et l'ajoute a la BDD
     * @param emprunt est l'emprunt que l'on souhaite ajouter a la BDD
     * @return l'emprunt ajoute
     */
    @Override
    public Emprunt addEmprunt(Emprunt emprunt) {
        String query = "INSERT INTO emprunt(etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine, emprunt_demande) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, emprunt.getEtudiant().getId());
            statement.setInt(2, emprunt.getMateriel().getId());
            statement.setInt(3, emprunt.getQuantite());
            statement.setDate(4, Date.valueOf(emprunt.getDebut()));
            statement.setDate(5, Date.valueOf(emprunt.getFin()));
            statement.setString(6, String.valueOf(emprunt.isValide()));
            statement.setString(7, String.valueOf(emprunt.isTermine()));
            statement.setDate(8, Date.valueOf(emprunt.getDemande()));
            statement.executeUpdate();
            try (ResultSet ids = statement.getGeneratedKeys()) {
                if (ids.next()) {
                    int generatedId = ids.getInt(1);
                    emprunt.setId(generatedId);
                    return emprunt;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode permet de mettre a jour les informations concernant un emprunt dans la BDD
     * @param emprunt est l'emprunt que l'on met a jour
     */
    @Override
    public void updateEmprunt(Emprunt emprunt) {
        String query = "UPDATE emprunt SET etudiant_id=?, materiel_id=?, quantite=?, emprunt_debut=?, " +
                "emprunt_fin=?, valide=?, termine=?, emprunt_demande=? WHERE emprunt_id=?;";
        try{
            Connection connection = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, emprunt.getEtudiant().getId());
            stmt.setInt(2, emprunt.getMateriel().getId());
            stmt.setInt(3, emprunt.getQuantite());
            stmt.setDate(4, Date.valueOf(emprunt.getDebut()));
            stmt.setDate(5, Date.valueOf(emprunt.getFin()));
            stmt.setString(6, String.valueOf(emprunt.isValide()));
            stmt.setString(7, String.valueOf(emprunt.isTermine()));
            stmt.setDate(8, Date.valueOf(emprunt.getDemande()));
            stmt.setInt(9, emprunt.getId());
            stmt.executeUpdate();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Cette methode retourne un emprunt a partir de son id
     * @param idEmprunt est l'id de l'emprunt que l'on souhaite avoir en sortie
     * @return l'emprunt correspondant a l'id
     */
    @Override
    public Emprunt getEmpruntById(Integer idEmprunt) {
        String query = "SELECT * FROM emprunt WHERE emprunt_id = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idEmprunt);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Emprunt(
                            resultSet.getInt("emprunt_id"),
                            etudiantDao.getEtudiantById(resultSet.getInt("etudiant_id")),
                            materielDao.getMaterielById(resultSet.getInt("materiel_id")),
                            resultSet.getInt("quantite"),
                            resultSet.getDate("emprunt_debut").toLocalDate(),
                            resultSet.getDate("emprunt_fin").toLocalDate(),
                            resultSet.getBoolean("valide"),
                            resultSet.getBoolean("termine"),
                            resultSet.getDate("emprunt_demande").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode permet de supprimer un emprunt de la BDD
     * @param emprunt est l'emprunt que l'on veut supprimer
     */
    @Override
    public void deleteEmprunt(Emprunt emprunt) {
        String query = "DELETE FROM emprunt WHERE emprunt_id=?;";
        try {
            Connection connection = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, emprunt.getId());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cette methode permet de lister des emprunts en fonction de s'ils ont ete valides ou non et s'ils sont termines ou non
     * @param valide indique si l'on veut les emprunts valides ou non en sortie
     * @param termine indique si l'on veut les emprunts termines ou non en sortie
     * @return la liste des emprunts dont les caracteristiques correpondent aux criteres d'entree (valide ou non, termine ou non)
     */
    @Override
    public List<Emprunt> listEmprunt(boolean valide, boolean termine) {
        String query = "SELECT * FROM emprunt WHERE valide = ? AND termine = ?";
        List<Emprunt> listEmprunt = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, String.valueOf(valide));
            statement.setString(2, String.valueOf(termine));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    listEmprunt.add(new Emprunt(
                            resultSet.getInt("emprunt_id"),
                            etudiantDao.getEtudiantById(resultSet.getInt("etudiant_id")),
                            materielDao.getMaterielById(resultSet.getInt("materiel_id")),
                            resultSet.getInt("quantite"),
                            resultSet.getDate("emprunt_debut").toLocalDate(),
                            resultSet.getDate("emprunt_fin").toLocalDate(),
                            resultSet.getBoolean("valide"),
                            resultSet.getBoolean("termine"),
                            resultSet.getDate("emprunt_demande").toLocalDate()
                            )
                    );
                }
                return listEmprunt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode retourne la liste des emprunts concernant un materiel
     * @param materiel le materiel dont on souhaite connaitre les emprunts le concernant
     * @return les emprunts concernant le materiel
     */
    @Override
    public List<Emprunt> listEmpruntConcerningMateriel(Materiel materiel) {
        String query = "SELECT * FROM emprunt WHERE materiel_id=?";
        List<Emprunt> listEmprunt = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, materiel.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    listEmprunt.add(new Emprunt(
                            resultSet.getInt("emprunt_id"),
                            etudiantDao.getEtudiantById(resultSet.getInt("etudiant_id")),
                            materielDao.getMaterielById(resultSet.getInt("materiel_id")),
                            resultSet.getInt("quantite"),
                            resultSet.getDate("emprunt_debut").toLocalDate(),
                            resultSet.getDate("emprunt_fin").toLocalDate(),
                            resultSet.getBoolean("valide"),
                            resultSet.getBoolean("termine"),
                            resultSet.getDate("emprunt_demande").toLocalDate()
                            )
                    );
                }
                return listEmprunt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode retourne une hashmap avec pour chaque entree, une date et un nombre
     * Ce nombre correspond a la quantite de materiel disponible a la date donnee
     * @param materiel le materiel dont on souhaite connaitre les disponibilites
     * @param debut le debut de la periode sur laquelle on souhaite connaitre les disponibilites
     * @param fin la fin de la periode sur laquelle on souhaite connaitre les disponibilites
     * @return une hashmap avec pour chaque date de la periode, la quantite de materiel disponible
     */
    @Override
    public LinkedHashMap<LocalDate, Integer> getAvailability(Materiel materiel, LocalDate debut, LocalDate fin) {
        LinkedHashMap<LocalDate, Integer> availability = new LinkedHashMap<>();
        Integer quantiteMateriel = materiel.getQuantite();
        Integer quantiteEmpruntee;
        Integer quantiteDisponible;
        LocalDate debutEmprunt;
        LocalDate finEmprunt;

        List<Emprunt> listEmpruntConcerningMateriel = this.listEmpruntConcerningMateriel(materiel);
        Integer nbJours = Integer.parseInt(String.valueOf(DAYS.between(debut, fin)+1));
        LocalDate jour = debut.minusDays(1);

        for (int i = 0; i < nbJours; i++) {
            jour = jour.plusDays(1);
            quantiteDisponible = quantiteMateriel;
            for (Emprunt emprunt : listEmpruntConcerningMateriel) {
                debutEmprunt = emprunt.getDebut();
                finEmprunt = emprunt.getFin();
                quantiteEmpruntee = emprunt.getQuantite();
                if ((debutEmprunt.isBefore(jour) || debutEmprunt.isEqual(jour)) && (finEmprunt.isAfter(jour) || finEmprunt.isEqual(jour)) && !emprunt.isTermine()) {
                    quantiteDisponible -= quantiteEmpruntee;
                }
            }
            availability.put(jour, quantiteDisponible);
        }
        return availability;
    }

    /**
     * Cette methode teste si un emprunt est possible ou non
     * @param emprunt l'emprunt que l'on souhaite tester
     * @return un booleen, vrai si l'emprunt est possible, faux sinon
     */
    @Override
    public boolean checkPossibility(Emprunt emprunt) {
        boolean possible = true;
        Materiel materiel = emprunt.getMateriel();
        Integer quantiteAEmprunter = emprunt.getQuantite();
        LocalDate debutEmprunt = emprunt.getDebut();
        LocalDate finEmprunt = emprunt.getFin();
        HashMap<LocalDate, Integer> availability = this.getAvailability(materiel, debutEmprunt, finEmprunt);

        for(Iterator<Map.Entry<LocalDate, Integer>> iteration = availability.entrySet().iterator();
            iteration.hasNext();) {
            Map.Entry ligne = iteration.next();
            Integer quantiteDisponible = (Integer) ligne.getValue();
            if (quantiteDisponible - quantiteAEmprunter < 0) {
                possible = false;
            }
        }
        return possible;
    }

}

