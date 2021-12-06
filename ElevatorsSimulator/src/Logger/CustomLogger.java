package Logger;

import java.io.IOException;
import java.util.logging.*;

public final class CustomLogger {
    private static CustomLogger instance;
    private static Logger logger;

    private CustomLogger() {
        logger = Logger.getLogger(CustomLogger.class.getName());
        logger.setUseParentHandlers(false);

        CustomFormatter formatter = new CustomFormatter();
        FileHandler handler = null;
        try {
            handler = new FileHandler("Log.txt", true);


        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.setFormatter(formatter);

//        Adding HTML Formatter for INFO and higher
        HTMLFormatter htmlFormatter = new HTMLFormatter();
        FileHandler htmlHandler = null;
        try {
            htmlHandler = new FileHandler("Log.html", true);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        Setting formatter and level
        htmlHandler.setFormatter(htmlFormatter);
        htmlHandler.setLevel(Level.INFO);

        logger.addHandler(handler);
        logger.addHandler(htmlHandler);
    }

    public static CustomLogger getInstance() {
        if (instance == null) {
            instance = new CustomLogger();
        }
        return instance;
    }

//    wrapper for logger.info
    public static void info(String message){
        if (instance == null) {
            instance = new CustomLogger();
        }
        logger.info(message);
    }

//    wrapper for logger.warning
    public static void warn(String message){
        if (instance == null) {
            instance = new CustomLogger();
        }
        logger.warning(message);
    }

//    wrapper for logger.severe
    public static void error(String message){
        if (instance == null) {
            instance = new CustomLogger();
        }
        logger.severe(message);
    }
}
