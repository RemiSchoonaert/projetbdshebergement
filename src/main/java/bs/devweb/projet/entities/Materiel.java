package bs.devweb.projet.entities;

public class Materiel {

    private Integer id;
    private Lieu lieu;
    private Association association;
    private String designation;
    private Integer quantite;
    private Double prix;

    public Materiel(Integer id, Lieu lieu, Association association, String designation, Integer quantite, Double prix) {
        this.id = id;
        this.lieu = lieu;
        this.association = association;
        this.designation = designation;
        this.quantite = quantite;
        this.prix = prix;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

}
