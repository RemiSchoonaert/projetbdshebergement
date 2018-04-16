package bs.devweb.projet.entities;

public class Association {

    private Integer id;
    private Etudiant etudiant;
    private String nom;

    public Association(Integer id, Etudiant etudiant, String nom) {
        this.id = id;
        this.etudiant = etudiant;
        this.nom = nom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
