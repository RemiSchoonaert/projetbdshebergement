package bs.devweb.projet.entities;

import java.time.LocalDate;

public class Emprunt {

    private Integer id;
    private Etudiant etudiant;
    private Materiel materiel;
    private Integer quantite;
    private LocalDate debut;
    private LocalDate fin;
    private boolean valide;
    private boolean termine;
    private LocalDate demande;

    public Emprunt(Integer id, Etudiant etudiant, Materiel materiel, Integer quantite, LocalDate debut, LocalDate fin, boolean valide, boolean termine, LocalDate demande) {
        this.id = id;
        this.etudiant = etudiant;
        this.materiel = materiel;
        this.quantite = quantite;
        this.debut = debut;
        this.fin = fin;
        this.valide = valide;
        this.termine = termine;
        this.demande = demande;
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

    public Materiel getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDebut() {
        return debut;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public boolean isTermine() {
        return termine;
    }

    public void setTermine(boolean termine) {
        this.termine = termine;
    }

    public LocalDate getDemande() {
        return demande;
    }

    public void setDemande(LocalDate demande) {
        this.demande = demande;
    }
}
