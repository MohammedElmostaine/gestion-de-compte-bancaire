package model;

import java.util.List;

public class CompteCourant extends Compte {
    private float decouvert;

    public CompteCourant(String code, float solde, List<Operation> listeOperations, float decouvert) {
        super(code, solde, listeOperations);
        this.decouvert = decouvert;
    }

    public float getDecouvert() {
        return decouvert;
    }

    public void setDecouvert(float decouvert) {
        this.decouvert = decouvert;
    }

    @Override
    public float calculerInteret() {
        return 0;
    }

    @Override
    public float retirer(float montant) {
        if (montant <= 0) {
            return solde;
        }
        if (solde - montant >= -decouvert) {
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
        if (montant > 0 && solde - montant >= -decouvert) {
            super.virement(compte, montant);
        }
    }

    @Override
    public void afficherDetails() {
        System.out.println("=== Détails du Compte Courant ===");
        System.out.println("Code: " + code);
        System.out.println("Solde: " + solde + " €");
        System.out.println("Découvert autorisé: " + decouvert + " €");
        System.out.println("Nombre d'opérations: " + listeOperations.size());
        if (!listeOperations.isEmpty()) {
            System.out.println("\n--- Historique des opérations ---");
            for (Operation op : listeOperations) {
                System.out.println(op.toString());
            }
        }
    }
}
