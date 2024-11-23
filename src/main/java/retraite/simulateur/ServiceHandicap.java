package retraite.simulateur;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class ServiceHandicap {
    public int calculerTrimestresRequisHandicap(Date dateNaissance, Date dateRetraiteSouhaitee) {
        // Convertir Date en LocalDate pour faciliter les calculs
        LocalDate dateNaissanceLocal = new java.sql.Date(dateNaissance.getTime()).toLocalDate();
        LocalDate dateRetraiteLocal = new java.sql.Date(dateRetraiteSouhaitee.getTime()).toLocalDate();

        // Calculer l'âge de départ à la retraite
        int ageDepart = Period.between(dateNaissanceLocal, dateRetraiteLocal).getYears();

        // Extraire l'année de naissance
        int anneeNaissance = dateNaissanceLocal.getYear();

        // Déterminer les trimestres requis en fonction de l'année de naissance et de l'âge de départ
        if (anneeNaissance < 1961 || (anneeNaissance == 1961 && dateNaissanceLocal.getMonthValue() < 9)) {
            return 88; // Avant le 1er septembre 1961
        } else if (anneeNaissance == 1961 || anneeNaissance == 1962) {
            return 68; // Entre le 1er septembre 1961 et 31 décembre 1962
        } else if (anneeNaissance == 1963) {
            return 68;
        } else if (anneeNaissance == 1964) {
            if (ageDepart == 58) return 79;
            return 69;
        } else if (anneeNaissance == 1965) {
            if (ageDepart == 57) return 89;
            if (ageDepart == 58) return 79;
            return 69;
        } else if (anneeNaissance == 1966) {
            if (ageDepart == 56) return 99;
            if (ageDepart == 57) return 89;
            if (ageDepart == 58) return 79;
            return 69;
        } else if (anneeNaissance >= 1967 && anneeNaissance <= 1969) {
            if (ageDepart == 55) return 110;
            if (ageDepart == 56) return 100;
            if (ageDepart == 57) return 90;
            if (ageDepart == 58) return 80;
            return 70;
        } else if (anneeNaissance >= 1970 && anneeNaissance <= 1972) {
            if (ageDepart == 55) return 111;
            if (ageDepart == 56) return 101;
            if (ageDepart == 57) return 91;
            if (ageDepart == 58) return 81;
            return 71;
        } else {
            if (ageDepart == 55) return 112;
            if (ageDepart == 56) return 102;
            if (ageDepart == 57) return 92;
            if (ageDepart == 58) return 82;
            return 72;
        }

        // Si aucun cas ne correspond
        //throw new IllegalArgumentException("Combinaison année de naissance et âge de départ invalide.");
    }

    public LocalDate calculerAgeDepartHandicap(Date dateNaissance, int trimestresCotises) {
        LocalDate dateNaissanceLocal = new java.sql.Date(dateNaissance.getTime()).toLocalDate();
        int anneeNaissance = dateNaissanceLocal.getYear();

        // Vérification des conditions en fonction de l'année de naissance et des trimestres cotisés
        if (anneeNaissance < 1961 || (anneeNaissance == 1961 && dateNaissanceLocal.getMonthValue() < 9)) {
            if (trimestresCotises >= 68) {
                return dateNaissanceLocal.plusYears(59);
            }
        } else if (anneeNaissance <= 1963) {
            if (trimestresCotises >= 68) {
                return dateNaissanceLocal.plusYears(59);
            }
        } else if (anneeNaissance == 1964) {
            if (trimestresCotises >= 79) {
                return dateNaissanceLocal.plusYears(58);
            } else if (trimestresCotises >= 69) {
                return dateNaissanceLocal.plusYears(59);
            }
        } else if (anneeNaissance == 1965) {
            if (trimestresCotises >= 89) {
                return dateNaissanceLocal.plusYears(57);
            } else if (trimestresCotises >= 79) {
                return dateNaissanceLocal.plusYears(58);
            } else if (trimestresCotises >= 69) {
                return dateNaissanceLocal.plusYears(59);
            }
        } else if (anneeNaissance == 1966) {
            if (trimestresCotises >= 99) {
                return dateNaissanceLocal.plusYears(56);
            } else if (trimestresCotises >= 89) {
                return dateNaissanceLocal.plusYears(57);
            } else if (trimestresCotises >= 79) {
                return dateNaissanceLocal.plusYears(58);
            } else if (trimestresCotises >= 69) {
                return dateNaissanceLocal.plusYears(59);
            }
        } else if (anneeNaissance >= 1967 && anneeNaissance <= 1969) {
            if (trimestresCotises >= 110) {
                return dateNaissanceLocal.plusYears(55);
            } else if (trimestresCotises >= 100) {
                return dateNaissanceLocal.plusYears(56);
            } else if (trimestresCotises >= 90) {
                return dateNaissanceLocal.plusYears(57);
            } else if (trimestresCotises >= 80) {
                return dateNaissanceLocal.plusYears(58);
            } else if (trimestresCotises >= 70) {
                return dateNaissanceLocal.plusYears(59);
            }
        } else if (anneeNaissance >= 1970 && anneeNaissance <= 1972) {
            if (trimestresCotises >= 111) {
                return dateNaissanceLocal.plusYears(55);
            } else if (trimestresCotises >= 101) {
                return dateNaissanceLocal.plusYears(56);
            } else if (trimestresCotises >= 91) {
                return dateNaissanceLocal.plusYears(57);
            } else if (trimestresCotises >= 81) {
                return dateNaissanceLocal.plusYears(58);
            } else if (trimestresCotises >= 71) {
                return dateNaissanceLocal.plusYears(59);
            }
        } else {
            if (trimestresCotises >= 112) {
                return dateNaissanceLocal.plusYears(55);
            } else if (trimestresCotises >= 102) {
                return dateNaissanceLocal.plusYears(56);
            } else if (trimestresCotises >= 92) {
                return dateNaissanceLocal.plusYears(57);
            } else if (trimestresCotises >= 82) {
                return dateNaissanceLocal.plusYears(58);
            } else if (trimestresCotises >= 72) {
                return dateNaissanceLocal.plusYears(59);
            }
        }

        // Par défaut, aucune condition remplie
        throw new IllegalArgumentException("Les conditions d'âge et de trimestres cotisés ne sont pas remplies.");
    }
}
