package ui.commands;

import dataLayer.dataAccessObjects.db.daos.PflegekraftDaoSqlite;
import dataLayer.dataAccessObjects.IDao;
import models.Pflegekraft;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import ui.TableFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Command(name = "pflegekraft",
         description = "Pflegekraefte verwalten",
         subcommands = {
             PflegekraftCommand.ListCommand.class,
             PflegekraftCommand.GetCommand.class,
             PflegekraftCommand.AddCommand.class,
             PflegekraftCommand.EditCommand.class,
             PflegekraftCommand.DeleteCommand.class
         })
public class PflegekraftCommand implements Runnable {

    private static final String DB_PATH = "patientenverwaltung.db";

    @Override
    public void run() {
        new picocli.CommandLine(this).usage(System.out);
    }

    static IDao<Pflegekraft, Long> getDao() {
        return new PflegekraftDaoSqlite(DB_PATH);
    }

    @Command(name = "list", description = "Alle Pflegekraefte anzeigen")
    static class ListCommand implements Runnable {
        @Override
        public void run() {
            List<Pflegekraft> list = getDao().findAll();
            if (list.isEmpty()) {
                System.out.println("Keine Pflegekraefte vorhanden.");
                return;
            }
            String[] headers = {"ID", "Vorname", "Nachname", "Telefon"};
            List<String[]> rows = new ArrayList<>();
            for (Pflegekraft p : list) {
                rows.add(new String[]{
                    String.valueOf(p.getId()),
                    p.getVorname(),
                    p.getNachname(),
                    p.getTelefon()
                });
            }
            TableFormatter.printTable(headers, rows);
        }
    }

    @Command(name = "get", description = "Pflegekraft nach ID anzeigen")
    static class GetCommand implements Runnable {
        @Parameters(index = "0", description = "Pflegekraft-ID")
        long id;

        @Override
        public void run() {
            Optional<Pflegekraft> opt = getDao().findById(id);
            if (opt.isEmpty()) {
                TableFormatter.printError("Pflegekraft mit ID " + id + " nicht gefunden.");
                return;
            }
            Pflegekraft p = opt.get();
            System.out.println("ID:       " + p.getId());
            System.out.println("Vorname:  " + p.getVorname());
            System.out.println("Nachname: " + p.getNachname());
            System.out.println("Telefon:  " + p.getTelefon());
        }
    }

    @Command(name = "add", description = "Neue Pflegekraft anlegen")
    static class AddCommand implements Runnable {
        @Option(names = "--vorname", required = true, description = "Vorname") String vorname;
        @Option(names = "--nachname", required = true, description = "Nachname") String nachname;
        @Option(names = "--telefon", required = true, description = "Telefonnummer") String telefon;

        @Override
        public void run() {
            long id = System.currentTimeMillis();
            Pflegekraft pflegekraft = new Pflegekraft(id, vorname, nachname, telefon);
            getDao().create(pflegekraft);
            TableFormatter.printSuccess("Pflegekraft erfolgreich angelegt (ID: " + id + ")");
        }
    }

    @Command(name = "edit", description = "Pflegekraft bearbeiten")
    static class EditCommand implements Runnable {
        @Parameters(index = "0", description = "Pflegekraft-ID") long id;
        @Option(names = "--vorname", description = "Vorname") String vorname;
        @Option(names = "--nachname", description = "Nachname") String nachname;
        @Option(names = "--telefon", description = "Telefonnummer") String telefon;

        @Override
        public void run() {
            IDao<Pflegekraft, Long> dao = getDao();
            Optional<Pflegekraft> opt = dao.findById(id);
            if (opt.isEmpty()) {
                TableFormatter.printError("Pflegekraft mit ID " + id + " nicht gefunden.");
                return;
            }
            Pflegekraft p = opt.get();
            if (vorname != null) p.setVorname(vorname);
            if (nachname != null) p.setNachname(nachname);
            if (telefon != null) p.setTelefon(telefon);
            dao.update(p);
            TableFormatter.printSuccess("Pflegekraft " + id + " erfolgreich aktualisiert.");
        }
    }

    @Command(name = "delete", description = "Pflegekraft loeschen")
    static class DeleteCommand implements Runnable {
        @Parameters(index = "0", description = "Pflegekraft-ID") long id;
        @Option(names = "--force", description = "Ohne Nachfrage loeschen") boolean force;

        @Override
        public void run() {
            IDao<Pflegekraft, Long> dao = getDao();
            if (!force) {
                TableFormatter.printError("Bitte --force angeben um zu loeschen.");
                return;
            }
            Optional<Pflegekraft> opt = dao.findById(id);
            if (opt.isEmpty()) {
                TableFormatter.printError("Pflegekraft mit ID " + id + " nicht gefunden.");
                return;
            }
            dao.deleteById(id);
            TableFormatter.printSuccess("Pflegekraft " + id + " geloescht.");
        }
    }
}
