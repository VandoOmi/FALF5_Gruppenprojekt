package ui;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.shell.jline3.PicocliJLineCompleter;
import ui.commands.ExitCommand;
import ui.commands.LeistungCommand;
import ui.commands.PatientCommand;
import ui.commands.PflegekraftCommand;

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

    @Override
    public void run() {
        CommandLine cmd = new CommandLine(this);
        cmd.usage(System.out);
    }

    public static void main(String[] args) throws Exception {
        CommandLine cmd = new CommandLine(new Main());
        boolean run = true;

        String banner = """
                
                  ____  __ _
                 |  _ \\/ _| | ___  __ _  ___
                 | |_) | |_| |/ _ \\/ _` |/ _ \\
                 |  __/|  _| |  __/ (_| |  __/
                 |_|   |_| |_|\\___|\\__, |\\___|
                                    |___/
                 Patientenverwaltung v1.0
                """;
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,cyan " + banner + "|@"));

        Terminal terminal = TerminalBuilder.builder().build();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new PicocliJLineCompleter(cmd.getCommandSpec()))
                .build();

        while (run) {
            try {
                String line = reader.readLine("pflege> ");
                if (line.isBlank()) {
                    continue;
                }
                String[] arguments = line.trim().split("\\s+");
                if (arguments.length > 0 && arguments[0].equals("exit")) {
                    System.out.println("Auf Wiedersehen!");
                    run = false;
                } else {
                    cmd.execute(arguments);
                }
            } catch (UserInterruptException | EndOfFileException e) {
                break;
            }
        }
    }
}
