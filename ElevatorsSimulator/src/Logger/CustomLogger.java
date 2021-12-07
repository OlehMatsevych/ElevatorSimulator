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
        ConsoloeFromatter clFormatter = new ConsoloeFromatter();
        FileHandler handler = null;
        FileHandler htmlHandler = null;
        FileHandler errorsAndWarningsHandler = null;
        ConsoleHandler clHandler = new ConsoleHandler();
        FileHandler errorsHtmlHandler = null;
        try {
            handler = new FileHandler("./logs/log.txt");
            htmlHandler = new FileHandler("./logs/log.html");
            errorsAndWarningsHandler = new FileHandler("./logs/log-errors.txt");
            errorsHtmlHandler = new FileHandler("./logs/log-errors.html");

        } catch (IOException e) {
            e.printStackTrace();
        }

//        Setting formatter and level
        handler.setFormatter(formatter);

        htmlHandler.setFormatter(htmlFormatter);
        htmlHandler.setLevel(Level.INFO);

        clHandler.setFormatter(clFormatter);
        clHandler.setLevel(Level.ALL);

        errorsAndWarningsHandler.setLevel(Level.WARNING);
        errorsAndWarningsHandler.setFormatter(formatter);

        errorsHtmlHandler.setLevel(Level.WARNING);
        errorsHtmlHandler.setFormatter(htmlFormatter);


        logger.addHandler(handler);
        logger.addHandler(htmlHandler);
        logger.addHandler(clHandler);
        logger.addHandler(errorsAndWarningsHandler);
        logger.addHandler(errorsHtmlHandler);
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

    public static void finest(String message) {
        if(instance == null) {
            instance = new CustomLogger();
        }

        logger.finest(message);
    }
}
