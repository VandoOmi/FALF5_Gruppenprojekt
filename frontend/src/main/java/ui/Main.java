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
import ui.commands.ClearCommand;
import ui.commands.ExitCommand;
import ui.commands.LeistungCommand;
import ui.commands.PatientCommand;
import ui.commands.PflegekraftCommand;

import java.util.ArrayList;
import java.util.List;

@Command(name = "pflege",
         mixinStandardHelpOptions = true,
         version = "Patientenverwaltung 1.0",
         description = "Interaktive Patientenverwaltung",
         subcommands = {
             PatientCommand.class,
             PflegekraftCommand.class,
             LeistungCommand.class,
             ClearCommand.class,
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
                String[] arguments = splitArguments(line.trim());
                if (arguments.length > 0 && arguments[0].equals("exit")) {
                    System.out.println("Auf Wiedersehen!");
                    run = false;
                } else if (arguments.length > 0 && arguments[0].equals("clear")) {
                    terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
                    terminal.flush();
                } else {
                    cmd.execute(arguments);
                }
            } catch (UserInterruptException | EndOfFileException e) {
                break;
            }
        }
    }

    private static String[] splitArguments(String input) {
        List<String> args = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        char quoteChar = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (inQuotes) {
                if (c == quoteChar) {
                    inQuotes = false;
                } else {
                    current.append(c);
                }
            } else if (c == '"' || c == '\'') {
                inQuotes = true;
                quoteChar = c;
            } else if (Character.isWhitespace(c)) {
                if (!current.isEmpty()) {
                    args.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (!current.isEmpty()) {
            args.add(current.toString());
        }
        return args.toArray(new String[0]);
    }
}
