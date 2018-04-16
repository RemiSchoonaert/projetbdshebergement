package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.EtudiantDao;
import bs.devweb.projet.dao.StatutDao;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.provider.DataSourceProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Cette classe implemente les methodes de l'interface EtudiantDao
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class EtudiantDaoImpl implements EtudiantDao {

    private StatutDao statutDao = new StatutDaoImpl();

    /**
     * Cette methode retourne un etudiant a partir de son id
     * @param idEtudiant l'id de l'etudiant que l'on souhaite avoir en sortie
     * @return l'etudiant dont l'id correspond a celui indique en entree
     */
    @Override
    public Etudiant getEtudiantById(Integer idEtudiant) {
        String query = "SELECT * FROM etudiant WHERE etudiant_id = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idEtudiant);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Etudiant(
                            resultSet.getInt("etudiant_id"),
                            resultSet.getString("etudiant_nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("mdp"),
                            resultSet.getString("mail"),
                            resultSet.getString("telephone"),
                            statutDao.getStatutById(resultSet.getInt("statut_id")),
                            resultSet.getBoolean("verifie"),
                            resultSet.getString("mdp_verification")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode liste les mails et mots de passe des etudiant autorises c'est-a-dire les etudiants qui ont verifie leur compte
     * @return la liste des mails et mots de passe
     */
    @Override
    public HashMap<String, String> listAuthorizedStudents() {
        String query = "SELECT mail, mdp, verifie FROM etudiant;";
        HashMap<String, String> res = new HashMap<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                if (resultSet.getBoolean("verifie")) {
                    res.put(resultSet.getString("mail"), resultSet.getString("mdp"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Cette methode permet d'ajouter un etudiant a la BDD
     * @param etudiant l'etudiant que l'on souhaite ajouter
     * @return l'etudiant ajoute
     */
    @Override
    public Etudiant addEtudiant(Etudiant etudiant) {
        String query = "INSERT INTO etudiant(statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, etudiant.getStatut().getId());
            statement.setString(2, etudiant.getNom());
            statement.setString(3, etudiant.getPrenom());
            statement.setString(4, etudiant.getMotDePasse());
            statement.setString(5, etudiant.getTelephone());
            statement.setString(6, etudiant.getMail());
            statement.setString(7, String.valueOf(etudiant.isVerifie()));
            statement.setString(8, etudiant.getMotDePasseVerification());
            statement.executeUpdate();
            try (ResultSet ids = statement.getGeneratedKeys()) {
                if (ids.next()) {
                    int generatedId = ids.getInt(1);
                    etudiant.setId(generatedId);
                    return etudiant;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode retourne un etudiant a partir de son adresse mail
     * @param mail l'adresse mail de l'etudiant que l'on souhaite avoir en sortie
     * @return l'etudiant dont l'adresse mail correspond a celle indiquee en entree
     */
    @Override
    public Etudiant getEtudiantByMail(String mail) {
        String query = "SELECT * FROM etudiant WHERE mail = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, mail);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Etudiant(
                            resultSet.getInt("etudiant_id"),
                            resultSet.getString("etudiant_nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("mdp"),
                            resultSet.getString("mail"),
                            resultSet.getString("telephone"),
                            statutDao.getStatutById(resultSet.getInt("statut_id")),
                            resultSet.getBoolean("verifie"),
                            resultSet.getString("mdp_verification")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode permet de mettre a jour un etudiant dans la BDD
     * @param etudiant l'etudiant avec ses nouvelles informations
     */
    @Override
    public void updateEtudiant(Etudiant etudiant) {
        String query = "UPDATE etudiant SET statut_id=?, etudiant_nom=?, prenom=?, mdp=?, mail=?, " +
                "telephone=?, verifie=?, mdp_verification=? WHERE etudiant_id=?;";
        try{
            Connection connection = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, etudiant.getStatut().getId());
            stmt.setString(2, etudiant.getNom());
            stmt.setString(3, etudiant.getPrenom());
            stmt.setString(4, etudiant.getMotDePasse());
            stmt.setString(5, etudiant.getMail());
            stmt.setString(6, etudiant.getTelephone());
            stmt.setString(7, String.valueOf(etudiant.isVerifie()));
            stmt.setString(8, etudiant.getMotDePasseVerification());
            stmt.setInt(9, etudiant.getId());
            stmt.executeUpdate();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Cette methode permet de lister les etudiants ayant verifie leur compte ou non
     * @param verifie indique si l'on souhaite les etudiants verifies ou non
     * @return la liste des etudiants verifies ou non
     */
    @Override
    public List<Etudiant> listEtudiant(boolean verifie) {
        String query = "SELECT * FROM etudiant;";
        List<Etudiant> listEtudiant = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                if (resultSet.getBoolean("verifie") && verifie) {
                    listEtudiant.add(new Etudiant(
                                    resultSet.getInt("etudiant_id"),
                                    resultSet.getString("etudiant_nom"),
                                    resultSet.getString("prenom"),
                                    resultSet.getString("mdp"),
                                    resultSet.getString("mail"),
                                    resultSet.getString("telephone"),
                                    statutDao.getStatutById(resultSet.getInt("statut_id")),
                                    resultSet.getBoolean("verifie"),
                                    resultSet.getString("mdp_verification")
                            )
                    );
                } else if (!resultSet.getBoolean("verifie") && !verifie) {
                    listEtudiant.add(new Etudiant(
                                    resultSet.getInt("etudiant_id"),
                                    resultSet.getString("etudiant_nom"),
                                    resultSet.getString("prenom"),
                                    resultSet.getString("mdp"),
                                    resultSet.getString("mail"),
                                    resultSet.getString("telephone"),
                                    statutDao.getStatutById(resultSet.getInt("statut_id")),
                                    resultSet.getBoolean("verifie"),
                                    resultSet.getString("mdp_verification")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listEtudiant;
    }

    /**
     * Cette methode genere un mot de passe de verification aleatoire
     * @return un mot de passe de verification aleatoire
     */
    @Override
    public String genererMdpVerification() {
        String mdp = "";
        final String min = "abcdefghijklmnopqrstuvwxyz";
        final String maj = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String num = "0123456789";
        final String spe = "-_";
        List<String> typeList = new ArrayList<>();
        typeList.add(min);
        typeList.add(maj);
        typeList.add(num);
        typeList.add(spe);
        Integer idType;
        String temporaryType;
        Integer idElement;
        for (int i = 0; i < 50; i++) {
            idType = Math.toIntExact(Math.round(Math.random()*3));
            temporaryType = typeList.get(idType);
            idElement = Math.toIntExact(Math.round(Math.random()*(temporaryType.length()-1)));
            mdp += temporaryType.charAt(idElement);
        }
        return mdp;
    }

}
