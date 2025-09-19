package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Compte {
    protected String code;
    protected float solde;
    protected List<Operation> listeOperations;

    public Compte(String code, float solde, List<Operation> listeOperations) {
        this.code = code;
        this.solde = solde;
        this.listeOperations = (listeOperations != null) ? new ArrayList<>(listeOperations) : new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public List<Operation> getListeOperations() {
        return new ArrayList<>(listeOperations);
    }

    public void setListeOperations(List<Operation> listeOperations) {
        this.listeOperations = (listeOperations != null) ? new ArrayList<>(listeOperations) : new ArrayList<>();
    }

    public abstract float retirer(float montant);

    public abstract float calculerInteret();

    public abstract void afficherDetails();

    public float verser(float montant) {
        if (montant <= 0) {
            return solde;
        }
        solde += montant;
        return solde;
    }

    public void virement(Compte compte, float montant) {
        float avant = this.solde;
        if (this.retirer(montant) != avant) {
            compte.verser(montant);
        }
    }

    public void ajouterOperation(Operation operation) {
        if (operation != null) {
            this.listeOperations.add(operation);
        }
    }

    public boolean supprimerOperation(Operation operation) {
        return this.listeOperations.remove(operation);
    }

    public static boolean estCodeCompteValide(String code) {
        return code != null && code.matches("CPT-\\d{5}");
    }
}
