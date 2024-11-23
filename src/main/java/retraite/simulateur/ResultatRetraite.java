package retraite.simulateur;

import java.time.LocalDate;
import java.util.Date;

public class ResultatRetraite {
    double epargne;
    float SAM;
    float taux;
    int trimestresValides;
    int trimestresRequis;
    String ageMinimum;
    LocalDate dateMinimale;
    String ageAuDepart;
    String ageLegalAtteint;

    public ResultatRetraite(double epargne, float SAM, float taux, int trimestresValides, int trimestresRequis, String ageMinimum, LocalDate dateMinimale, String ageAuDepart, String ageLegalAtteint) {
        this.epargne = epargne;
        this.SAM = SAM;
        this.taux = taux;
        this.trimestresValides = trimestresValides;
        this.trimestresRequis = trimestresRequis;
        this.ageMinimum = ageMinimum;
        this.dateMinimale = dateMinimale;
        this.ageAuDepart = ageAuDepart;
        this.ageLegalAtteint = ageLegalAtteint;
    }

    public String getAgeLegalAtteint() {
        return ageLegalAtteint;
    }

    public void setAgeLegalAtteint(String ageLegalAtteint) {
        this.ageLegalAtteint = ageLegalAtteint;
    }

    public double getEpargne() {
        return epargne;
    }

    public void setEpargne(double epargne) {
        this.epargne = epargne;
    }

    public float getSAM() {
        return SAM;
    }

    public void setSAM(float SAM) {
        this.SAM = SAM;
    }

    public float getTaux() {
        return taux;
    }

    public void setTaux(float taux) {
        this.taux = taux;
    }

    public int getTrimestresValides() {
        return trimestresValides;
    }

    public void setTrimestresValides(int trimestresValides) {
        this.trimestresValides = trimestresValides;
    }

    public int getTrimestresRequis() {
        return trimestresRequis;
    }

    public void setTrimestresRequis(int trimestresRequis) {
        this.trimestresRequis = trimestresRequis;
    }

    public String getAgeMinimum() {
        return ageMinimum;
    }

    public void setAgeMinimum(String ageMinimum) {
        this.ageMinimum = ageMinimum;
    }

    public LocalDate getDateMinimale() {
        return dateMinimale;
    }

    public void setDateMinimale(LocalDate dateMinimale) {
        this.dateMinimale = dateMinimale;
    }

    public String getAgeAuDepart() {
        return ageAuDepart;
    }

    public void setAgeAuDepart(String ageAuDepart) {
        this.ageAuDepart = ageAuDepart;
    }
}
