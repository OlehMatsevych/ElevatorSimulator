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
//                Adding HTML Formatter for INFO and higher
        HTMLFormatter htmlFormatter = new HTMLFormatter();
        FileHandler handler = null;
        FileHandler htmlHandler = null;
        FileHandler errorsAndWarningsHandler = null;
        ConsoleHandler clHandler = new ConsoleHandler();
        try {
            handler = new FileHandler("log.txt");
            htmlHandler = new FileHandler("log.html");
            errorsAndWarningsHandler = new FileHandler("log-errors.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }

//        Setting formatter and level
        handler.setFormatter(formatter);

        htmlHandler.setFormatter(htmlFormatter);
        htmlHandler.setLevel(Level.INFO);

        clHandler.setFormatter(formatter);
        clHandler.setLevel(Level.FINEST);

        errorsAndWarningsHandler.setLevel(Level.WARNING);
        errorsAndWarningsHandler.setFormatter(formatter);


        logger.addHandler(handler);
        logger.addHandler(htmlHandler);
        logger.addHandler(clHandler);
        logger.addHandler(errorsAndWarningsHandler);
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
