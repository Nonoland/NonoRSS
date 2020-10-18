package fr.nonoland.nonorss.utils.log;

public class Log {

    /*
    https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
     */

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void sendMessage(StatusCode code, String message) {
        if(code == StatusCode.Info)
            System.out.println("[Info] " + message);
        if(code == StatusCode.Warning)
            System.out.println(ANSI_YELLOW + "[Warning] " + message + ANSI_RESET);
        if(code == StatusCode.Error)
            System.out.println(ANSI_RED + "[Error] " + message + ANSI_RESET);
    }

    public static void sendMessage(StatusCode code, int message) {
        sendMessage(code, Integer.toString(message));
    }

    public static void sendMessage(String message) {
        sendMessage(StatusCode.Info, message);
    }

}
