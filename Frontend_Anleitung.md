# Frontend – Interaktive CLI (Picocli + JLine)

## Uebersicht

Die Patientenverwaltung erhaelt ein terminalbasiertes Frontend als interaktive Shell.
Technologie: **Picocli** (Command-Parsing, ANSI-Farben, Help) + **JLine 3** (Tab-Completion, History, Readline).

## Voraussetzungen

- Java 25+
- Maven
- SQLite-Datenbank (wird automatisch erstellt)

## Dependencies

In `pom.xml` folgende Dependencies im `<dependencies>`-Block ergaenzen:

```xml
<dependency>
    <groupId>info.picocli</groupId>
    <artifactId>picocli</artifactId>
    <version>4.7.7</version>
</dependency>
<dependency>
    <groupId>info.picocli</groupId>
    <artifactId>picocli-shell-jline3</artifactId>
    <version>4.7.7</version>
</dependency>
<dependency>
    <groupId>org.jline</groupId>
    <artifactId>jline</artifactId>
    <version>3.27.1</version>
</dependency>
```

## Projektstruktur

```
src/main/java/
├── ui/
│   ├── Main.java                  # Entry Point + Shell-Loop
│   ├── TableFormatter.java        # ANSI-Tabellenformatierung
│   └── commands/
│       ├── PatientCommand.java    # patient list|get|add|edit|delete
│       ├── PflegekraftCommand.java# pflegekraft list|get|add|edit|delete
│       ├── LeistungCommand.java   # leistung list|get|add|edit|delete
│       └── ExitCommand.java       # Shell beenden
├── models/                        # (bestehend)
├── dataLayer/                     # (bestehend)
└── configuration/                 # (bestehend)
```

## Implementierung Schritt fuer Schritt

### 1. Main.java – Entry Point

Die Main-Klasse ist der Top-Level-Command und startet die interaktive Shell:

```java
@Command(name = "pflege",
         mixinStandardHelpOptions = true,
         version = "Patientenverwaltung 1.0",
         description = "Interaktive Patientenverwaltung",
         subcommands = {
             PatientCommand.class,
             PflegekraftCommand.class,
             LeistungCommand.class,
             ExitCommand.class
         })
public class Main implements Runnable {
    // Shell-Loop mit JLine LineReader + PicocliJLineCompleter
}
```

**Shell-Loop Prinzip:**
1. JLine `Terminal` und `LineReader` erstellen
2. `PicocliJLineCompleter` fuer Tab-Completion registrieren
3. Endlosschleife: Eingabe lesen -> `CommandLine.execute()` -> wiederholen
4. Bei `exit` oder Ctrl+D: Loop beenden

### 2. Subcommand-Struktur (Beispiel: PatientCommand)

```java
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
    // Zeigt Help wenn ohne Subcommand aufgerufen
}
```

Jeder CRUD-Subcommand ist eine innere Klasse mit `implements Runnable`:

| Command | Syntax | Beschreibung |
|---------|--------|--------------|
| `patient list` | keine Argumente | Alle Patienten als Tabelle |
| `patient get <id>` | `@Parameters` | Einzelnen Patienten anzeigen |
| `patient add` | `@Option` pro Feld | Neuen Patienten anlegen |
| `patient edit <id>` | `@Parameters` + `@Option` | Felder aktualisieren |
| `patient delete <id>` | `@Parameters` + `--force` | Patient loeschen |

### 3. Options und Parameters

**Add-Command Beispiel:**
```java
@Command(name = "add", description = "Neuen Patient anlegen")
static class AddCommand implements Runnable {
    @Option(names = "--vorname", required = true) String vorname;
    @Option(names = "--nachname", required = true) String nachname;
    @Option(names = "--geburtsdatum", required = true) LocalDate geburtsdatum;
    @Option(names = {"-p", "--pflegegrad"}, required = true) int pflegegrad;
    @Option(names = "--zimmer", required = true) String zimmer;
    @Option(names = "--vermoegen", required = true) double vermoegen;

    public void run() { /* DAO aufrufen */ }
}
```

**Edit-Command Beispiel (partielle Updates):**
```java
@Command(name = "edit", description = "Patient bearbeiten")
static class EditCommand implements Runnable {
    @Parameters(index = "0", description = "Patient-ID") long id;
    @Option(names = "--vorname") String vorname;      // nicht required!
    @Option(names = "--nachname") String nachname;
    // ... nur gesetzte Felder werden aktualisiert
}
```

### 4. TableFormatter

Utility-Klasse fuer formatierte Ausgabe:
- Dynamische Spaltenbreiten
- ANSI-Farben via `CommandLine.Help.Ansi.AUTO.string()`
- Header fett: `@|bold ID|@`
- Erfolg gruen: `@|green Erfolgreich|@`
- Fehler rot: `@|red Fehler|@`

### 5. ExitCommand

```java
@Command(name = "exit", description = "Programm beenden")
public class ExitCommand implements Runnable {
    public void run() {
        System.out.println("Auf Wiedersehen!");
        System.exit(0);
    }
}
```

## Benutzung

### Starten

```bash
mvn compile exec:java -Dexec.mainClass="ui.Main"
```

### Verfuegbare Befehle

| Befehl | Beschreibung |
|--------|-------------|
| `patient list` | Alle Patienten anzeigen |
| `patient get 1` | Patient mit ID 1 |
| `patient add --vorname Max --nachname Mueller --geburtsdatum 1945-03-15 --pflegegrad 3 --zimmer 204 --vermoegen 12500` | Anlegen |
| `patient edit 1 --zimmer 301` | Zimmer aendern |
| `patient delete 1 --force` | Loeschen ohne Nachfrage |
| `pflegekraft list` | Alle Pflegekraefte |
| `pflegekraft add --vorname Anna --nachname Weber --telefon 0421-12345` | Pflegekraft anlegen |
| `leistung list` | Alle Leistungen |
| `leistung add --lkNr LK01 --bezeichnung Grundpflege --beschreibung "Taegliche Grundpflege"` | Leistung anlegen |
| `help` | Hilfe anzeigen |
| `help patient add` | Hilfe fuer spezifischen Command |
| `exit` | Beenden |

### Features

- **Tab-Completion**: `pat` + Tab -> `patient`
- **History**: Pfeiltasten hoch/runter fuer vorherige Befehle
- **Auto-Help**: `--help` an jeden Befehl anhaengen
- **Fehlerbehandlung**: Ungueltige Eingaben zeigen Fehlermeldung + Usage

## Beispiel-Session

```
$ java -jar patientenverwaltung.jar

  ____  __ _
 |  _ \/ _| | ___  __ _  ___
 | |_) | |_| |/ _ \/ _` |/ _ \
 |  __/|  _| |  __/ (_| |  __/
 |_|   |_| |_|\___|\__, |\___|
                    |___/
 Patientenverwaltung v1.0

pflege> patient list
+----+---------+----------+------------+------------+--------+----------+
| ID | Vorname | Nachname | Geb.datum  | Pflegegrad | Zimmer | Vermoegen|
+----+---------+----------+------------+------------+--------+----------+
|  1 | Max     | Mueller  | 1945-03-15 |          3 | 204    | 12500.00 |
|  2 | Erika   | Schmidt  | 1938-08-22 |          4 | 107    |  8300.00 |
+----+---------+----------+------------+------------+--------+----------+

pflege> patient add --vorname Anna --nachname Weber --geburtsdatum 1950-01-10 --pflegegrad 2 --zimmer 301 --vermoegen 5000
Patient erfolgreich angelegt (ID: 3)

pflege> patient get 3
ID:           3
Vorname:      Anna
Nachname:     Weber
Geburtsdatum: 1950-01-10
Pflegegrad:   2
Zimmer:       301
Vermoegen:    5000.00

pflege> patient edit 3 --zimmer 405
Patient 3 erfolgreich aktualisiert.

pflege> patient delete 3 --force
Patient 3 geloescht.

pflege> pflegekraft list
+----+---------+----------+--------------+
| ID | Vorname | Nachname | Telefon      |
+----+---------+----------+--------------+
|  1 | Maria   | Schulz   | 0421-55678   |
+----+---------+----------+--------------+

pflege> help patient
Usage: pflege patient [COMMAND]
Patienten verwalten

Commands:
  list    Alle Patienten anzeigen
  get     Patient nach ID anzeigen
  add     Neuen Patient anlegen
  edit    Patient bearbeiten
  delete  Patient loeschen

pflege> exit
Auf Wiedersehen!
```

## Anpassungen am bestehenden Code

| Datei | Aenderung | Grund |
|-------|-----------|-------|
| `models/Patient.java` | Konstruktor auf `public` aendern | Zugriff aus `ui`-Package |
| `models/Leistung.java` | Konstruktor auf `public` aendern | Zugriff aus `ui`-Package |
| `pom.xml` | Dependencies hinzufuegen (siehe oben) | Picocli + JLine |
| `pom.xml` | `exec-maven-plugin` hinzufuegen | Einfaches Starten via Maven |

## UML

Siehe `UML_Frontend_Patientenverwaltung.puml` fuer das vollstaendige Klassendiagramm.

Zum Rendern der `.puml`-Datei:
- **VS Code**: Extension "PlantUML" installieren, dann Alt+D fuer Vorschau
- **Online**: Inhalt auf [plantuml.com/plantuml](http://www.plantuml.com/plantuml/uml) einfuegen
- **CLI**: `java -jar plantuml.jar UML_Frontend_Patientenverwaltung.puml`
