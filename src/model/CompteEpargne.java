package model;

import java.util.List;

public class CompteEpargne extends Compte {
    private float tauxInteret;

    public CompteEpargne(String code, float solde, List<Operation> listeOperations, float tauxInteret) {
        super(code, solde, listeOperations);
        this.tauxInteret = tauxInteret;
    }

    public float getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(float tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    @Override
    public float calculerInteret() {
        return solde * tauxInteret / 100;
    }

    @Override
    public float retirer(float montant) {
        if (montant <= 0) {
            return solde;
        }
        if (solde >= montant) {
            solde -= montant;
        }
        return solde;
    }

    @Override
    public float verser(float montant) {
        return super.verser(montant);
    }

    @Override
    public void virement(Compte compte, float montant) {
        if (montant > 0 && solde >= montant) {
            super.virement(compte, montant);
        }
    }

    @Override
    public void afficherDetails() {
        System.out.println("=== Détails du Compte Épargne ===");
        System.out.println("Code: " + code);
        System.out.println("Solde: " + solde + " €");
        System.out.println("Taux d'intérêt: " + tauxInteret + "%");
        System.out.println("Intérêts calculés: " + calculerInteret() + " €");
        System.out.println("Nombre d'opérations: " + listeOperations.size());
        if (!listeOperations.isEmpty()) {
            System.out.println("\n--- Historique des opérations ---");
            for (Operation op : listeOperations) {
                System.out.println(op.toString());
            }
        }
    }
}
