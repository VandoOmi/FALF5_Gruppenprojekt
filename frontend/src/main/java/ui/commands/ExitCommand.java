package ui.commands;

import picocli.CommandLine.Command;

@Command(name = "exit", description = "Programm beenden")
public class ExitCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Auf Wiedersehen!");
        System.exit(0);
    }
}
