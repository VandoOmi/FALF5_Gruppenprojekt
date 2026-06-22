package ui.commands;

import dataLayer.dataAccessObjects.db.daos.LeistungDaoSqlite;
import dataLayer.dataAccessObjects.IDao;
import models.Leistung;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import ui.TableFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Command(name = "leistung",
         description = "Leistungen verwalten",
         subcommands = {
             LeistungCommand.ListCommand.class,
             LeistungCommand.GetCommand.class,
             LeistungCommand.AddCommand.class,
             LeistungCommand.EditCommand.class,
             LeistungCommand.DeleteCommand.class
         })
public class LeistungCommand implements Runnable {

    private static final String DB_PATH = "patientenverwaltung.db";

    @Override
    public void run() {
        new picocli.CommandLine(this).usage(System.out);
    }

    static IDao<Leistung, String> getDao() {
        return new LeistungDaoSqlite(DB_PATH);
    }

    @Command(name = "list", description = "Alle Leistungen anzeigen")
    static class ListCommand implements Runnable {
        @Override
        public void run() {
            List<Leistung> list = getDao().findAll();
            if (list.isEmpty()) {
                System.out.println("Keine Leistungen vorhanden.");
                return;
            }
            String[] headers = {"LK-Nr", "Bezeichnung", "Beschreibung"};
            List<String[]> rows = new ArrayList<>();
            for (Leistung l : list) {
                rows.add(new String[]{
                    l.getLkNr(),
                    l.getBezeichnung(),
                    l.getBeschreibung()
                });
            }
            TableFormatter.printTable(headers, rows);
        }
    }

    @Command(name = "get", description = "Leistung nach LK-Nr anzeigen")
    static class GetCommand implements Runnable {
        @Parameters(index = "0", description = "LK-Nummer")
        String lkNr;

        @Override
        public void run() {
            Optional<Leistung> opt = getDao().findById(lkNr);
            if (opt.isEmpty()) {
                TableFormatter.printError("Leistung mit LK-Nr " + lkNr + " nicht gefunden.");
                return;
            }
            Leistung l = opt.get();
            System.out.println("LK-Nr:        " + l.getLkNr());
            System.out.println("Bezeichnung:  " + l.getBezeichnung());
            System.out.println("Beschreibung: " + l.getBeschreibung());
        }
    }

    @Command(name = "add", description = "Neue Leistung anlegen")
    static class AddCommand implements Runnable {
        @Option(names = "--lkNr", required = true, description = "Leistungskomplex-Nummer") String lkNr;
        @Option(names = "--bezeichnung", required = true, description = "Bezeichnung") String bezeichnung;
        @Option(names = "--beschreibung", required = true, description = "Beschreibung") String beschreibung;

        @Override
        public void run() {
            Leistung leistung = new Leistung(lkNr, bezeichnung, beschreibung);
            getDao().create(leistung);
            TableFormatter.printSuccess("Leistung erfolgreich angelegt (LK-Nr: " + lkNr + ")");
        }
    }

    @Command(name = "edit", description = "Leistung bearbeiten")
    static class EditCommand implements Runnable {
        @Parameters(index = "0", description = "LK-Nummer") String lkNr;
        @Option(names = "--bezeichnung", description = "Bezeichnung") String bezeichnung;
        @Option(names = "--beschreibung", description = "Beschreibung") String beschreibung;

        @Override
        public void run() {
            IDao<Leistung, String> dao = getDao();
            Optional<Leistung> opt = dao.findById(lkNr);
            if (opt.isEmpty()) {
                TableFormatter.printError("Leistung mit LK-Nr " + lkNr + " nicht gefunden.");
                return;
            }
            Leistung l = opt.get();
            if (bezeichnung != null) l.setBezeichnung(bezeichnung);
            if (beschreibung != null) l.setBeschreibung(beschreibung);
            dao.update(l);
            TableFormatter.printSuccess("Leistung " + lkNr + " erfolgreich aktualisiert.");
        }
    }

    @Command(name = "delete", description = "Leistung loeschen")
    static class DeleteCommand implements Runnable {
        @Parameters(index = "0", description = "LK-Nummer") String lkNr;
        @Option(names = "--force", description = "Ohne Nachfrage loeschen") boolean force;

        @Override
        public void run() {
            IDao<Leistung, String> dao = getDao();
            if (!force) {
                TableFormatter.printError("Bitte --force angeben um zu loeschen.");
                return;
            }
            Optional<Leistung> opt = dao.findById(lkNr);
            if (opt.isEmpty()) {
                TableFormatter.printError("Leistung mit LK-Nr " + lkNr + " nicht gefunden.");
                return;
            }
            dao.deleteById(lkNr);
            TableFormatter.printSuccess("Leistung " + lkNr + " geloescht.");
        }
    }
}
