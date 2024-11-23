package retraite.simulateur;

import java.time.LocalDate;
import java.util.Date;

public class ServiceCarriereLongue {
    public LocalDate calculerAgeDepart(Date dateNaissance, String carriereLongue) {
        LocalDate dateNaissanceLocal = new java.sql.Date(dateNaissance.getTime()).toLocalDate();
        LocalDate ageDepartCarriereLongue = null;

        if ("21".equals(carriereLongue)) {
            ageDepartCarriereLongue = dateNaissanceLocal.plusYears(63);
        } else if ("20".equals(carriereLongue)) {
            if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 8, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(60);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(60).plusMonths(3);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(60).plusMonths(6);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(60).plusMonths(9);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(61);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(60).plusMonths(9);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(61);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(61).plusMonths(3);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(61).plusMonths(6);
            } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 12, 31))) {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(61).plusMonths(9);
            } else {
                ageDepartCarriereLongue = dateNaissanceLocal.plusYears(62);
            }
        } else if ("18".equals(carriereLongue)) {
            ageDepartCarriereLongue = dateNaissanceLocal.plusYears(60);
        } else if ("16".equals(carriereLongue)) {
            ageDepartCarriereLongue = dateNaissanceLocal.plusYears(58);
        }

        LocalDate ageDepart;
        if (dateNaissanceLocal.isBefore(LocalDate.of(1958, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(62); // 62 ans pour 1955 - 1957
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1961, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(62); // 62 ans pour 1958 - 1960
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1961, 9, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(62); // 62 ans pour 1er janvier au 31 août 1961
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1962, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(62).plusMonths(3); // 62 ans et 3 mois pour 1er septembre - 31 décembre 1961
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(62).plusMonths(6); // 62 ans et 6 mois pour 1962
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1964, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(62).plusMonths(9); // 62 ans et 9 mois pour 1963
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1965, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(63); // 63 ans pour 1964
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1966, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(63).plusMonths(3); // 63 ans et 3 mois pour 1965
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1967, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(63).plusMonths(6); // 63 ans et 6 mois pour 1966
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1968, 1, 1))) {
            ageDepart = dateNaissanceLocal.plusYears(63).plusMonths(9); // 63 ans et 9 mois pour 1967
        } else {
            ageDepart = dateNaissanceLocal.plusYears(64); // 64 ans pour 1968 et après
        }
        // Retourner la plus petite des deux valeurs
        if (ageDepartCarriereLongue != null && ageDepartCarriereLongue.isBefore(ageDepart)) {
            return ageDepartCarriereLongue;
        }
        return ageDepart;
    }
}
