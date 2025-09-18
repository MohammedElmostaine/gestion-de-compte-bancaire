package model;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Operation {
    protected UUID numero;
    protected LocalDateTime date;
    protected float montant;

    public Operation(LocalDateTime date, float montant) {
        this.numero = UUID.randomUUID();
        this.date = date;
        this.montant = montant;
    }

    public UUID getNumero() {
        return numero;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }
}
