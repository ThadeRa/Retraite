package retraite.simulateur;

import java.util.List;

public class ServiceRetraiteEnfant {
    public int calculerTrimestresParEnfant(Adherent adherent) {
        int nbTrimestresEnfants = 0;
        List<Enfant> enfants = adherent.getEnfants();
        if (adherent.getSexe() == 1) { // Est un homme
            for (Enfant e : enfants) {
                if (e.isEducationPartageeEnfant()) {
                    nbTrimestresEnfants += 2;
                }
            }
        } else { // Est une femme
            for (Enfant e : enfants) {
                if (e.isMaterniteEnfant()) {
                    nbTrimestresEnfants += 4;
                }
                if (e.isEducationPartageeEnfant()) {
                    nbTrimestresEnfants += 2;
                } else if (e.isEducationEnfant()) {
                    nbTrimestresEnfants += 4;
                }
            }
        }
        return nbTrimestresEnfants;
    }
}
