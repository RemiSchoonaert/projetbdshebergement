package bs.devweb.projet.entities;

public class Etudiant {

    private Integer id;
    private String nom;
    private String prenom;
    private String motDePasse;
    private String mail;
    private String telephone;
    private Statut statut;
    private boolean verifie;
    private String motDePasseVerification;

    public Etudiant(Integer id, String nom, String prenom, String motDePasse, String mail, String telephone, Statut statut, boolean verifie, String motDePasseVerification) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.mail = mail;
        this.telephone = telephone;
        this.statut = statut;
        this.verifie = verifie;
        this.motDePasseVerification = motDePasseVerification;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public boolean isVerifie() {
        return verifie;
    }

    public void setVerifie(boolean verifie) {
        this.verifie = verifie;
    }

    public String getMotDePasseVerification() {
        return motDePasseVerification;
    }

    public void setMotDePasseVerification(String motDePasseVerification) {
        this.motDePasseVerification = motDePasseVerification;
    }

}
