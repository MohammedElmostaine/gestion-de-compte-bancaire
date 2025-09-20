package controller;

import model.*;
import java.util.*;
import java.time.LocalDateTime;

public class BancController {
    private Map<String, Compte> comptes;
    private int compteurCompte;
    private Scanner scanner;

    public BancController() {
        this.comptes = new HashMap<>();
        this.compteurCompte = 10000;
        this.scanner = new Scanner(System.in);
    }

    public void demarrerApplication() {
        System.out.println("Bienvenue dans le système de gestion bancaire!");

        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            int choix = lireChoix();

            if (choix == 1) creerCompte();
            else if (choix == 2) effectuerVersement();
            else if (choix == 3) effectuerRetrait();
            else if (choix == 4) effectuerVirement();
            else if (choix == 5) consulterSolde();
            else if (choix == 6) consulterOperations();
            else if (choix == 7) listerComptes();
            else if (choix == 0) {
                continuer = false;
                System.out.println("Au revoir!");
            } else {
                System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private void afficherMenu() {
        System.out.println("\n=== MENU BANCAIRE ===");
        System.out.println("1. Créer un compte");
        System.out.println("2. Effectuer un versement");
        System.out.println("3. Effectuer un retrait");
        System.out.println("4. Effectuer un virement");
        System.out.println("5. Consulter le solde");
        System.out.println("6. Consulter les opérations");
        System.out.println("7. Lister les comptes");
        System.out.println("0. Quitter");
        System.out.print("Votre choix: ");
    }

    private void creerCompte() {
        String codeCompte = genererCodeCompte();
        System.out.println("Nouveau code de compte: " + codeCompte);

        System.out.print("Solde initial: ");
        float soldeInitial = lireMontant();
        if (soldeInitial < 0) {
            System.out.println("Le solde initial doit être >= 0");
            return;
        }

        System.out.println("Type de compte: 1) Courant  2) Épargne");
        System.out.print("Votre choix: ");
        int typeCompte = lireChoix();
        Compte compte;

        if (typeCompte == 1) {
            System.out.print("Découvert autorisé: ");
            float decouvert = lireMontant();
            if (decouvert < 0) {
                System.out.println("Le découvert doit être >= 0");
                return;
            }
            compte = new CompteCourant(codeCompte, soldeInitial, new ArrayList<>(), decouvert);
        } else if (typeCompte == 2) {
            System.out.print("Taux d'intérêt (%): ");
            float tauxInteret = lireMontant();
            if (tauxInteret < 0) {
                System.out.println("Le taux d'intérêt doit être >= 0");
                return;
            }
            compte = new CompteEpargne(codeCompte, soldeInitial, new ArrayList<>(), tauxInteret);
        } else {
            System.out.println("Type de compte invalide");
            return;
        }

        comptes.put(codeCompte, compte);
        System.out.println("Compte créé avec succès: " + codeCompte);
    }

    private void effectuerVersement() {
        String codeCompte = lireCodeCompte();
        if (!Compte.estCodeCompteValide(codeCompte)) {
            System.out.println("Format de code compte invalide. Attendu: CPT-XXXXX");
            return;
        }
        Compte compte = comptes.get(codeCompte);
        if (compte == null) {
            System.out.println("Compte introuvable: " + codeCompte);
            return;
        }

        System.out.print("Montant du versement: ");
        float montant = lireMontant();
        if (montant <= 0) {
            System.out.println("Le montant doit être > 0");
            return;
        }
        System.out.print("Source du versement (ex: Salaire): ");
        String source = scanner.nextLine().trim();

        compte.verser(montant);
        compte.ajouterOperation(new Versement(LocalDateTime.now(), montant, source));
        System.out.println("Versement de " + montant + "€ effectué. Nouveau solde: " + compte.getSolde() + "€");
    }

    private void effectuerRetrait() {
        String codeCompte = lireCodeCompte();
        if (!Compte.estCodeCompteValide(codeCompte)) {
            System.out.println("Format de code compte invalide. Attendu: CPT-XXXXX");
            return;
        }
        Compte compte = comptes.get(codeCompte);
        if (compte == null) {
            System.out.println("Compte introuvable: " + codeCompte);
            return;
        }

        System.out.print("Montant du retrait: ");
        float montant = lireMontant();
        if (montant <= 0) {
            System.out.println("Le montant doit être > 0");
            return;
        }
        System.out.print("Destination du retrait (ex: ATM): ");
        String destination = scanner.nextLine().trim();

        float avant = compte.getSolde();
        compte.retirer(montant);
        if (compte.getSolde() != avant) {
            compte.ajouterOperation(new Retrait(LocalDateTime.now(), montant, destination));
            System.out.println("Retrait de " + montant + "€ effectué. Nouveau solde: " + compte.getSolde() + "€");
        } else {
            System.out.println("Retrait refusé (fonds insuffisants ou règle non respectée)");
        }
    }

    private void effectuerVirement() {
        System.out.println("Compte source:");
        String codeSource = lireCodeCompte();
        if (!Compte.estCodeCompteValide(codeSource)) {
            System.out.println("Format de code compte invalide (source)");
            return;
        }
        Compte compteSource = comptes.get(codeSource);
        if (compteSource == null) {
            System.out.println("Compte source introuvable");
            return;
        }

        System.out.println("Compte destination:");
        String codeDestination = lireCodeCompte();
        if (!Compte.estCodeCompteValide(codeDestination)) {
            System.out.println("Format de code compte invalide (destination)");
            return;
        }
        Compte compteDestination = comptes.get(codeDestination);
        if (compteDestination == null) {
            System.out.println("Compte destination introuvable");
            return;
        }

        if (codeSource.equals(codeDestination)) {
            System.out.println("Le compte source et destination ne peuvent pas être identiques");
            return;
        }

        System.out.print("Montant du virement: ");
        float montant = lireMontant();
        if (montant <= 0) {
            System.out.println("Le montant doit être > 0");
            return;
        }

        float avant = compteSource.getSolde();
        compteSource.virement(compteDestination, montant);
        if (compteSource.getSolde() != avant) {
            LocalDateTime now = LocalDateTime.now();
            compteSource.ajouterOperation(new Retrait(now, montant, "Virement vers " + codeDestination));
            compteDestination.ajouterOperation(new Versement(now, montant, "Virement de " + codeSource));
            System.out.println("Virement effectué avec succès");
        } else {
            System.out.println("Virement refusé (fonds insuffisants ou règle non respectée)");
        }
    }

    private void consulterSolde() {
        String codeCompte = lireCodeCompte();
        if (!Compte.estCodeCompteValide(codeCompte)) {
            System.out.println("Format de code compte invalide. Attendu: CPT-XXXXX");
            return;
        }
        Compte compte = comptes.get(codeCompte);
        if (compte == null) {
            System.out.println("Compte introuvable: " + codeCompte);
            return;
        }

        System.out.println("Solde du compte " + codeCompte + ": " + compte.getSolde() + "€");
    }

    private void consulterOperations() {
        String codeCompte = lireCodeCompte();
        if (!Compte.estCodeCompteValide(codeCompte)) {
            System.out.println("Format de code compte invalide. Attendu: CPT-XXXXX");
            return;
        }
        Compte compte = comptes.get(codeCompte);
        if (compte == null) {
            System.out.println("Compte introuvable: " + codeCompte);
            return;
        }

        List<Operation> operations = compte.getListeOperations();
        if (operations.isEmpty()) {
            System.out.println("Aucune opération trouvée pour ce compte");
        } else {
            System.out.println("\n=== Historique des opérations ===");
            for (Operation op : operations) {
                System.out.println(op.toString());
            }
        }
    }

    private void listerComptes() {
        if (comptes.isEmpty()) {
            System.out.println("Aucun compte disponible");
            return;
        }
        System.out.println("\n=== Liste des comptes ===");
        for (Compte compte : comptes.values()) {
            compte.afficherDetails();
            System.out.println("-------------------------");
        }
    }

    private String genererCodeCompte() {
        String code;
        do {
            code = "CPT-" + String.format("%05d", compteurCompte++);
        } while (comptes.containsKey(code));
        return code;
    }

    private int lireChoix() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }

    private float lireMontant() {
        try { return Float.parseFloat(scanner.nextLine().trim()); }
        catch (Exception e) { return -1f; }
    }

    private String lireCodeCompte() {
        System.out.print("Code du compte (CPT-XXXXX): ");
        return scanner.nextLine().trim();
    }
}