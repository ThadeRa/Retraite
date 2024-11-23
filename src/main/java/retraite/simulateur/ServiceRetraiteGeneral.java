package retraite.simulateur;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;


@Service
public class ServiceRetraiteGeneral {
    ServiceRetraiteEnfant serviceRetraiteEnfant = new ServiceRetraiteEnfant();
    ServiceSurcote serviceSurcote = new ServiceSurcote();
    ServiceHandicap serviceHandicap = new ServiceHandicap();
    ServiceCarriereLongue serviceCarriereLongue = new ServiceCarriereLongue();

    float SAM = 0;
    float pension;
    float taux;
    int trimestresValides;
    int trimestresRequis;
    int ageMinimum;
    Date dateMinimale;
    int ageAuDepart;

    // Situation de l'utilisateur :
    boolean handicap = false;
    boolean carrierelongue = false;

    int nbTrimestresValidesTOTAL = 0;

    public ResultatRetraite calculerEpargneRetraite(Adherent adherent) {
        calculerTrimestresValidesTotal(adherent);
        setSituation(adherent);
        SAM = adherent.getSAM();
        int nbTrimestresManquants = calculerTrimestresManquants(adherent);
        float taux = calculerTaux(adherent, nbTrimestresManquants);
        float fractionTrim = calculerFractionTrimestres(adherent);

        // Calcul de la pension brute
        float epargneBrute = calculerPension(adherent, taux, fractionTrim);

        // Arrondir le montant final
        BigDecimal epargneArrondie = BigDecimal.valueOf(epargneBrute).setScale(2, RoundingMode.HALF_UP);
        System.out.println(epargneArrondie);

        return syntheseRetraite(adherent,
                epargneArrondie.doubleValue(),
                taux,
                nbTrimestresValidesTOTAL
        );
    }

    public int calculerTrimestresRequis(Date dateNaissance, Date dateRetraiteSouhaitee) {
        // Convertir Date en LocalDate pour faciliter les comparaisons
        LocalDate dateNaissanceLocal = new java.sql.Date(dateNaissance.getTime()).toLocalDate();

        //HANDICAP-
        if (handicap) {
            return serviceHandicap.calculerTrimestresRequisHandicap(dateNaissance, dateRetraiteSouhaitee);
        }

        if (dateNaissanceLocal.isBefore(LocalDate.of(1960, 1, 1))) {
            return 167; // 1960 et avant
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1961, 9, 1))) {
            return 168; // 1er janvier au 31 août 1961
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1962, 1, 1))) {
            return 169; // 1er septembre au 31 décembre 1961
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1963, 1, 1))) {
            return 169; // 1962
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1964, 1, 1))) {
            return 170; // 1963
        } else if (dateNaissanceLocal.isBefore(LocalDate.of(1965, 1, 1))) {
            return 171; // 1964
        } else {
            return 172; // 1965 et au-delà
        }
    }

    public int calculerTrimestresHypothetiques(Adherent adherent) {
        int trimestresHypo = calculerTrimestresEntreDates(adherent.getDateSimulation(), adherent.getDateRetraiteSouhait());
        return trimestresHypo;
    }

    public int calculerTrimestresManquants(Adherent adherent) {
        int nbTrimValideTotal = nbTrimestresValidesTOTAL;
        int nbTrimRequis = 0;
        nbTrimRequis = calculerTrimestresRequis(adherent.getDateNaissance(), adherent.getDateRetraiteSouhait());
        return Math.max(nbTrimRequis - nbTrimValideTotal, 0);
    }

    public int calculerTrimestresValidesTotal(Adherent adherent) {
        int trimValides = adherent.getTrimValide();
        int trimHandicap = adherent.getTrimHandicap();
        int trimEnfant = serviceRetraiteEnfant.calculerTrimestresParEnfant(adherent);
        int trimHypothetiques = calculerTrimestresHypothetiques(adherent); // TODO trimHypothetiques, selon date de départ, en rajouter
        nbTrimestresValidesTOTAL = trimValides + trimHandicap + trimEnfant + trimHypothetiques;
        return nbTrimestresValidesTOTAL;
    }

    public int calculerTrimestresEntreDates(Date dateDebut, Date dateFin) {
        // Convertir les dates en LocalDate
        LocalDate debut = new java.sql.Date(dateDebut.getTime()).toLocalDate();
        LocalDate fin = new java.sql.Date(dateFin.getTime()).toLocalDate();

        // Vérifier que la date de fin est postérieure à la date de début
        if (!fin.isAfter(debut)) {
            return 0;
        }
        // Calculer les trimestres civils entre les deux dates
        int trimestres = 0;
        // Parcourir les mois entre les deux dates
        while (debut.isBefore(fin)) {
            int mois = debut.getMonthValue();
            // Si le mois est le dernier d'un trimestre civil, ajouter un trimestre
            if (mois == 3 || mois == 6 || mois == 9 || mois == 12) {
                trimestres++;
            }
            // Passer au mois suivant
            debut = debut.plusMonths(1);
        }
        return trimestres;
    }

    private float calculerTauxClassique(int nbTrimestresManquants, float tauxPlein, float tauxDecoteParTrimestre, float tauxMinimum) {
        float taux = tauxPlein - (nbTrimestresManquants * tauxDecoteParTrimestre);
        return Math.max(taux, tauxMinimum); // S'assurer que le taux ne descend pas sous le minimum
    }

    private float calculerTauxAutomatique(Adherent adherent, float tauxPlein, float tauxDecoteParTrimestre, float tauxMinimum) {
        LocalDate dateNaissance = new java.sql.Date(adherent.getDateNaissance().getTime()).toLocalDate();
        LocalDate dateRetraiteSouhaitee = new java.sql.Date(adherent.getDateRetraiteSouhait().getTime()).toLocalDate();

        // Calcul des trimestres manquants pour atteindre 67 ans
        LocalDate dateAtteinte67Ans = dateNaissance.plusYears(67);
        int trimestresManquants = calculerTrimestresEntreDates(
                java.sql.Date.valueOf(dateRetraiteSouhaitee),
                java.sql.Date.valueOf(dateAtteinte67Ans)
        );

        // Décote pour les trimestres manquants
        float taux = tauxPlein - (trimestresManquants * tauxDecoteParTrimestre);

        // S'assurer que le taux ne descend pas en dessous de 0
        return Math.max(taux, tauxMinimum);
    }

    public float calculerTaux(Adherent adherent, int nbTrimestresManquantsPourDecote) {
        float tauxPlein = 0.5f; // Taux plein à 50%
        float tauxDecoteParTrimestre = 0.00625f; // Décote de 0.625 % par trimestre manquant
        float tauxMinimum = 0.375f; // Taux minimum de 37.5 %
        int nbTrimestresManquantsMax = 20; // Limite à 20 trimestres pour la décote
        nbTrimestresManquantsPourDecote = Math.min(nbTrimestresManquantsMax, nbTrimestresManquantsPourDecote);
        float taux;
        // HANDICAP-
        if (handicap) {
            taux = tauxPlein;
        } else {
            switch (adherent.getMethodeTaux()) {
                case 2: // Taux plein classique
                    taux = calculerTauxClassique(nbTrimestresManquantsPourDecote, tauxPlein, tauxDecoteParTrimestre, tauxMinimum);
                    break;

                case 3: // Taux plein automatique
                    taux = calculerTauxAutomatique(adherent, tauxPlein, tauxDecoteParTrimestre, tauxMinimum);
                    break;

                default: // La plus avantageuse
                    taux = Math.max(
                            calculerTauxClassique(nbTrimestresManquantsPourDecote, tauxPlein, tauxDecoteParTrimestre, tauxMinimum),
                            calculerTauxAutomatique(adherent, tauxPlein, tauxDecoteParTrimestre, tauxMinimum)
                    );
            }
        }

        return taux;
    }

    public float calculerFractionTrimestres(Adherent adherent) {
        int nbTrimestresRequis = 0;
        nbTrimestresRequis = calculerTrimestresRequis(adherent.getDateNaissance(), adherent.getDateRetraiteSouhait());
        int nbTrimestresValides = nbTrimestresValidesTOTAL;
        float fraction = (float) nbTrimestresValides / nbTrimestresRequis;
        fraction = Math.min(1, fraction);
        return fraction;
    }

    public float calculerPension(Adherent adherent, float taux, float fractionTrim) {
        float epargneBrute = SAM * taux * fractionTrim;

        // Ajouter la surcote pour trimestres cotisés après âge légal
        int trimRequis = calculerTrimestresRequis(adherent.getDateNaissance(), adherent.getDateRetraiteSouhait());
        float surcoteTrimSupplementaires = serviceSurcote.calculerSurcote(adherent, trimRequis);
        epargneBrute = epargneBrute * surcoteTrimSupplementaires;

        // Ajouter la majoration pour les enfants si applicable
        if (adherent.getNbEnfants() >= 3) {
            epargneBrute *= 1.10F; // Majoration de 10% pour 3 enfants ou plus
        }

        return epargneBrute;
    }


    public void setSituation(Adherent adherent) {
        if (adherent.getCarriereLongue().equals("16")
                || adherent.getCarriereLongue().equals("18")
                || adherent.getCarriereLongue().equals("20")
                || adherent.getCarriereLongue().equals("21")) {
            carrierelongue = true;
        } else {
            carrierelongue = false;
        }

        if (adherent.getTrimHandicap() > 0) {
            handicap = true;
        } else {
            handicap = false;
        }

    }

    public ResultatRetraite syntheseRetraite(Adherent adherent, double epargne, float taux, int trimestresValides) {
        float SAM = adherent.getSAM();
        int trimestresRequis = calculerTrimestresRequis(adherent.getDateNaissance(), adherent.getDateRetraiteSouhait());

        LocalDate dateMinimale;
        if (handicap) {
            dateMinimale = serviceHandicap.calculerAgeDepartHandicap(adherent.getDateNaissance(), trimestresValides);
        } else {
            dateMinimale = serviceCarriereLongue.calculerAgeDepart(adherent.getDateNaissance(), adherent.getCarriereLongue());
        }

        LocalDate localDateNaissance = adherent.getDateNaissance().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period anneeMoisMinim = Period.between(localDateNaissance, dateMinimale);
        int yearsMinim = anneeMoisMinim.getYears();
        int monthsMinim = anneeMoisMinim.getMonths();
        String ageMinimum = yearsMinim + " ans et " + monthsMinim + " mois";

        LocalDate localDateDepart = adherent.getDateRetraiteSouhait().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period anneeMoisDepart = Period.between(localDateNaissance, localDateDepart);
        int yearsDepart = anneeMoisDepart.getYears();
        int monthsDepart = anneeMoisDepart.getMonths();
        String ageAuDepart = yearsDepart + " ans et " + monthsDepart + " mois";

        String ageLegalAtteint = "Non";
        if (yearsDepart >= yearsMinim && monthsDepart >= monthsMinim){
            ageLegalAtteint = "Oui";
        }

        return new ResultatRetraite(
                epargne,
                SAM,
                taux,
                trimestresValides,
                trimestresRequis,
                ageMinimum,
                dateMinimale,
                ageAuDepart,
                ageLegalAtteint);
    }
}
