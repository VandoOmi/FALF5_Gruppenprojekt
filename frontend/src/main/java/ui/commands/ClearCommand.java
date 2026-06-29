package ui.commands;

import picocli.CommandLine.Command;

@Command(name = "clear", description = "Terminal leeren")
public class ClearCommand implements Runnable {

    @Override
    public void run() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
