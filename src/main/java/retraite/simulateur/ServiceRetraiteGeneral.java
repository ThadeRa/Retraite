package retraite.simulateur;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;

// TODO bools handicapé, carrierelongue, 0TrimsManquant, trimEnfants, trimsHypothétique

public class ServiceRetraiteGeneral {
    ServiceRetraiteEnfant serviceRetraiteEnfant = new ServiceRetraiteEnfant();
    ServiceSurcote serviceSurcote = new ServiceSurcote();

    float SAM = 0;

    // Situation de l'utilisateur :
    boolean handicap = false;
    boolean carrierelongue = false;

    int nbTrimestresValidesTOTAL = 0;

    public double calculerEpargneRetraite(Adherent adherent) {
        setSituation(adherent);
        SAM = adherent.getSAM();
        int nbTrimestresManquants = calculerTrimestresManquants(adherent);
        float taux = calculerTaux(adherent, nbTrimestresManquants);
        float fractionTrim = calculerFractionTrimestres(adherent, nbTrimestresManquants);

        // Calcul de la pension brute
        float epargneBrute = calculerPension(adherent, taux, fractionTrim);

        // Arrondir le montant final
        BigDecimal epargneArrondie = BigDecimal.valueOf(epargneBrute).setScale(2, RoundingMode.HALF_UP);
        System.out.println(epargneArrondie);
        return epargneArrondie.doubleValue();
    }

    public int calculerTrimestresRequis(Date dateNaissance) {
        // Convertir Date en LocalDate pour faciliter les comparaisons
        LocalDate dateNaissanceLocal = new java.sql.Date(dateNaissance.getTime()).toLocalDate();

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

    public int calculerTrimestresHypothetiques(Adherent adherent){
        int trimestresHypo = calculerTrimestresEntreDates(adherent.getDateSimulation(), adherent.getDateRetraiteSouhait());
        return trimestresHypo;
    }

    public int calculerTrimestresManquants(Adherent adherent) {
        int nbTrimValideTotal = calculerTrimestresValidesTotal(adherent);
        int nbTrimRequis = calculerTrimestresRequis(adherent.getDateNaissance()); // TODO variant carrierelongue et handicap
        return Math.max(nbTrimRequis - nbTrimValideTotal, 0);
    }

    public int calculerTrimestresValidesTotal(Adherent adherent) {
        int trimValides = adherent.getTrimValide();
        int trimHandicap = adherent.getTrimHandicap();
        int trimEnfant = serviceRetraiteEnfant.calculerTrimestresParEnfant(adherent);
        int trimHypothetiques = calculerTrimestresHypothetiques(adherent); // TODO trimHypothetiques
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
        if (adherent.getTrimHandicap() > 0) {
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

    public float calculerFractionTrimestres(Adherent adherent, int nbTrimestresManquants) {
        int nbTrimestresRequis = 0;
        nbTrimestresRequis = calculerTrimestresRequis(adherent.getDateNaissance());
        int nbTrimestresValides = nbTrimestresValidesTOTAL;
        float fraction = (float) nbTrimestresValides / nbTrimestresRequis;
        fraction = Math.min(1, fraction);
        return fraction;
    }

    public float calculerPension(Adherent adherent, float taux, float fractionTrim) {
        float epargneBrute = SAM * taux * fractionTrim;

        float surcoteTrimSupplementaires = serviceSurcote.calculerSurcote(adherent, calculerTrimestresRequis(adherent.getDateNaissance()));
        epargneBrute = epargneBrute * surcoteTrimSupplementaires;

        // Ajouter la majoration pour les enfants si applicable
        if (adherent.getNbEnfants() >= 3) {
            epargneBrute *= 1.10F; // Majoration de 10% pour 3 enfants ou plus
        }
        return epargneBrute;
    }


    public void setSituation(Adherent adherent){
        if (adherent.getCarriereLongue().equals("16")
        || adherent.getCarriereLongue().equals("18")
                || adherent.getCarriereLongue().equals("20")
                || adherent.getCarriereLongue().equals("21")){
            carrierelongue=true;
        }
        if (adherent.getTrimHandicap()>0){
            handicap=true;
        }


    }
}
