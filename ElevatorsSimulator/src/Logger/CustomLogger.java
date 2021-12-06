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
            handler = new FileHandler("Log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.setFormatter(formatter);

        logger.addHandler(handler);
    }

    public static CustomLogger getInstance() {
        if (instance == null) {
            instance = new CustomLogger();
        }
        return instance;
    }

    public static void log(String message){
        if (instance == null) {
            instance = new CustomLogger();
        }
        logger.info(message);
    }

}
