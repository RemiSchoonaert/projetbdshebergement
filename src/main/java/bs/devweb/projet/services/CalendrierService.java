package bs.devweb.projet.services;

import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.services.EmpruntService;
import bs.devweb.projet.services.MaterielService;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Cette classe permet la création d'un calendrier des disponibilites d'un materiel donne
 * via sa methode statique generateCalendar(Integer idMateriel)
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class CalendrierService {

    public static HashMap<HashMap<String, Integer>, List<HashMap<LocalDate, Integer>>> generateCalendar(Integer idMateriel, Integer periode) {
        // on la variable qui contient le calendrier des disponibilites
        LinkedHashMap<HashMap<String, Integer>, List<HashMap<LocalDate, Integer>>> calendrier = new LinkedHashMap<>();
        // on recupere le materiel concerne via son id
        Materiel materiel = MaterielService.getInstance().getMaterielById(idMateriel);
        // on recupere la date actuelle
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        date = date.plusMonths(periode * 3);
        // on cree une liste des mois
        List<String> mois = Arrays.asList("Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre");
        // on cree les en-tetes pour 3 mois
        LinkedHashMap<Integer, String> enTetes = new LinkedHashMap<>();
        String monthYear;
        Integer monthValue;
        for (int i = date.getMonthValue(); i < date.getMonthValue() + 3; i++) {
            monthYear = mois.get(i - 1)+" "+date.plusMonths(i - date.getMonthValue()).getYear();
            enTetes.put(i, monthYear);
        }
        LocalDate debutMois;
        // on cree la variable joursDebutTable qui contiendra, pour chaque mois,
        // la date a laquelle on le fait demarrer
        List<LocalDate> joursDebutTable = new ArrayList<>();
        // on regarde pour chaque mois, on regarde a quel jour correspond le lundi de la 1ere semaine du mois
        // par exemple : pour un mois commencant un jeudi, on cherche a quelle date correspond
        // le lundi de cette meme semaine
        for (int i = 0; i < 3; i++) {
            debutMois = LocalDate.of(date.plusMonths(i).getYear(), date.plusMonths(i).getMonthValue(), 01);
            // tant que ce n'est pas un lundi, on recule d'un jour
            while (debutMois.getDayOfWeek().getValue() != 1) {
                debutMois = debutMois.minusDays(1);
            }
            // on ajoute cette date a joursDebutTable
            joursDebutTable.add(debutMois);
        }
        List<HashMap<LocalDate, Integer>> semaines = new ArrayList<>();
        HashMap<String, Integer> enTete = new HashMap<>();
        HashMap<LocalDate, Integer> semaine;
        LocalDate firstDayOfWeek;
        LocalDate lastDayOfWeek;
        // pour chaque mois du calendrier
        for (int i = 0; i < 3; i++) {
            // on recupere 42 jours (6 semaines) en commencant a partir du jour defini
            // dans la liste joursDebutTable
            // on le fait semaine par semaine
            for (int j = 0; j < 42; j+=7) {
                // on prend le 1er et dernier jour de la semaine
                firstDayOfWeek = joursDebutTable.get(i).plusDays(j);
                lastDayOfWeek = joursDebutTable.get(i).plusDays(j+6);
                // on recupere les disponibilites du materiel pour chaque jour de la semaine
                semaine = EmpruntService.getInstance().getAvailability(materiel, firstDayOfWeek, lastDayOfWeek);
                // on ajoute ces disponibilites a la liste "semaines"
                semaines.add(semaine);
            }
            enTete.put(enTetes.get(date.getMonthValue() + i), date.getMonthValue() + i);
            // on ajoute au calendrier l'en-tete du mois et ses disponibilites pour chaque semaine
            calendrier.put(enTete, semaines);
            enTete = new HashMap<>();
            semaines = new ArrayList<>();
        }
        return calendrier;
    }

}
