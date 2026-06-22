package ui;

import picocli.CommandLine;

import java.util.List;

public class TableFormatter {

    public static void printTable(String[] headers, List<String[]> rows) {
        int cols = headers.length;
        int[] widths = new int[cols];

        for (int i = 0; i < cols; i++) {
            widths[i] = headers[i].length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < cols; i++) {
                widths[i] = Math.max(widths[i], row[i] == null ? 0 : row[i].length());
            }
        }

        String separator = buildSeparator(widths);
        System.out.println(separator);
        System.out.print("|");
        for (int i = 0; i < cols; i++) {
            String cell = String.format(" %-" + widths[i] + "s |", headers[i]);
            System.out.print(CommandLine.Help.Ansi.AUTO.string("@|bold " + cell + "|@"));
        }
        System.out.println();
        System.out.println(separator);

        for (String[] row : rows) {
            System.out.print("|");
            for (int i = 0; i < cols; i++) {
                String val = row[i] == null ? "" : row[i];
                System.out.printf(" %-" + widths[i] + "s |", val);
            }
            System.out.println();
        }
        System.out.println(separator);
    }

    public static void printSuccess(String message) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|green " + message + "|@"));
    }

    public static void printError(String message) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|red " + message + "|@"));
    }

    private static String buildSeparator(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) {
            sb.append("-".repeat(w + 2)).append("+");
        }
        return sb.toString();
    }
}
