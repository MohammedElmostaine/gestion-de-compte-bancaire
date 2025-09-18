package model;

import java.time.LocalDateTime;

public class Versement extends Operation {
    private String source;

    public Versement(LocalDateTime date, float montant, String source) {
        super(date, montant);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Versement{" +
                "numero=" + numero +
                ", date=" + date +
                ", montant=" + montant +
                ", source='" + source + '\'' +
                '}';
    }
}
