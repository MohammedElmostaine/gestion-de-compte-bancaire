package model;

import java.time.LocalDateTime;

public class Retrait extends Operation {

    private String destination;

    public Retrait(LocalDateTime date, float montant, String destination) {
        super(date, montant);
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Retrait{" +
                "numero=" + numero +
                ", date=" + date +
                ", montant=" + montant +
                ", destination='" + destination + '\'' +
                '}';
    }
}
