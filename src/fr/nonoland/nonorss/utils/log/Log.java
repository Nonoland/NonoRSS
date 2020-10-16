package fr.nonoland.nonorss.utils.log;

public class Log {

    public static void sendMessage(StatusCode code, String message) {
        if(code == StatusCode.Info)
            System.out.println("[Info] " + message);
        if(code == StatusCode.Warning)
            System.out.println("[Warning] " + message);
        if(code == StatusCode.Error)
            System.out.println("[Error] " + message);
    }

}
