package ui.commands;

import dataLayer.dataAccessObjects.IDao;
import dataLayer.services.DataLayerManager;
import models.Patient;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import ui.TableFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Command(name = "patient",
         description = "Patienten verwalten",
         subcommands = {
             PatientCommand.ListCommand.class,
             PatientCommand.GetCommand.class,
             PatientCommand.AddCommand.class,
             PatientCommand.EditCommand.class,
             PatientCommand.DeleteCommand.class
         })
public class PatientCommand implements Runnable {

    @Override
    public void run() {
        new picocli.CommandLine(this).usage(System.out);
    }

    static IDao<Patient, Long> getDao() {
        return DataLayerManager.getInstance().getDataLayer().getDaoPatient();
    }

    @Command(name = "list", description = "Alle Patienten anzeigen")
    static class ListCommand implements Runnable {
        @Override
        public void run() {
            List<Patient> patients = getDao().findAll();
            if (patients.isEmpty()) {
                System.out.println("Keine Patienten vorhanden.");
                return;
            }
            String[] headers = {"ID", "Vorname", "Nachname", "Geb.datum", "Pflegegrad", "Zimmer", "Vermoegen"};
            List<String[]> rows = new ArrayList<>();
            for (Patient p : patients) {
                rows.add(new String[]{
                    String.valueOf(p.getId()),
                    p.getVorname(),
                    p.getNachname(),
                    p.getGeburtsdatum() == null ? "" : p.getGeburtsdatum().toString(),
                    String.valueOf(p.getPflegegrad()),
                    p.getZimmer(),
                    String.format("%.2f", p.getVermoegen())
                });
            }
            TableFormatter.printTable(headers, rows);
        }
    }

    @Command(name = "get", description = "Patient nach ID anzeigen")
    static class GetCommand implements Runnable {
        @Parameters(index = "0", description = "Patient-ID")
        long id;

        @Override
        public void run() {
            Optional<Patient> opt = getDao().findById(id);
            if (opt.isEmpty()) {
                TableFormatter.printError("Patient mit ID " + id + " nicht gefunden.");
                return;
            }
            Patient p = opt.get();
            System.out.println("ID:           " + p.getId());
            System.out.println("Vorname:      " + p.getVorname());
            System.out.println("Nachname:     " + p.getNachname());
            System.out.println("Geburtsdatum: " + (p.getGeburtsdatum() == null ? "" : p.getGeburtsdatum()));
            System.out.println("Pflegegrad:   " + p.getPflegegrad());
            System.out.println("Zimmer:       " + p.getZimmer());
            System.out.printf("Vermoegen:    %.2f%n", p.getVermoegen());
        }
    }

    @Command(name = "add", description = "Neuen Patient anlegen")
    static class AddCommand implements Runnable {
        @Option(names = "--vorname", required = true, description = "Vorname") String vorname;
        @Option(names = "--nachname", required = true, description = "Nachname") String nachname;
        @Option(names = "--geburtsdatum", required = true, description = "Geburtsdatum (YYYY-MM-DD)") LocalDate geburtsdatum;
        @Option(names = {"-p", "--pflegegrad"}, required = true, description = "Pflegegrad (1-5)") int pflegegrad;
        @Option(names = "--zimmer", required = true, description = "Zimmernummer") String zimmer;
        @Option(names = "--vermoegen", required = true, description = "Vermoegen") double vermoegen;

        @Override
        public void run() {
            long id = System.currentTimeMillis();
            Patient patient = new Patient(id, vorname, nachname, geburtsdatum, pflegegrad, zimmer, vermoegen);
            getDao().create(patient);
            TableFormatter.printSuccess("Patient erfolgreich angelegt (ID: " + id + ")");
        }
    }

    @Command(name = "edit", description = "Patient bearbeiten")
    static class EditCommand implements Runnable {
        @Parameters(index = "0", description = "Patient-ID") long id;
        @Option(names = "--vorname", description = "Vorname") String vorname;
        @Option(names = "--nachname", description = "Nachname") String nachname;
        @Option(names = "--geburtsdatum", description = "Geburtsdatum (YYYY-MM-DD)") LocalDate geburtsdatum;
        @Option(names = {"-p", "--pflegegrad"}, description = "Pflegegrad (1-5)") Integer pflegegrad;
        @Option(names = "--zimmer", description = "Zimmernummer") String zimmer;
        @Option(names = "--vermoegen", description = "Vermoegen") Double vermoegen;

        @Override
        public void run() {
            IDao<Patient, Long> dao = getDao();
            Optional<Patient> opt = dao.findById(id);
            if (opt.isEmpty()) {
                TableFormatter.printError("Patient mit ID " + id + " nicht gefunden.");
                return;
            }
            Patient p = opt.get();
            if (vorname != null) p.setVorname(vorname);
            if (nachname != null) p.setNachname(nachname);
            if (geburtsdatum != null) p.setGeburtsdatum(geburtsdatum);
            if (pflegegrad != null) p.setPflegegrad(pflegegrad);
            if (zimmer != null) p.setZimmer(zimmer);
            if (vermoegen != null) p.setVermoegen(vermoegen);
            dao.update(p);
            TableFormatter.printSuccess("Patient " + id + " erfolgreich aktualisiert.");
        }
    }

    @Command(name = "delete", description = "Patient loeschen")
    static class DeleteCommand implements Runnable {
        @Parameters(index = "0", description = "Patient-ID") long id;
        @Option(names = "--force", description = "Ohne Nachfrage loeschen") boolean force;

        @Override
        public void run() {
            IDao<Patient, Long> dao = getDao();
            if (!force) {
                TableFormatter.printError("Bitte --force angeben um zu loeschen.");
                return;
            }
            Optional<Patient> opt = dao.findById(id);
            if (opt.isEmpty()) {
                TableFormatter.printError("Patient mit ID " + id + " nicht gefunden.");
                return;
            }
            dao.deleteById(id);
            TableFormatter.printSuccess("Patient " + id + " geloescht.");
        }
    }
}
