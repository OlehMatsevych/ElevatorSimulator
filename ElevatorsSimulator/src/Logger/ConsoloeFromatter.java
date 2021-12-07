package Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ConsoloeFromatter extends Formatter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
        String color = "";
        var level = record.getLevel();
        if(level == Level.SEVERE) {
            color =  ANSI_RED;
        }
        if(level == Level.WARNING) {
            color =  ANSI_YELLOW;
        }

        if(level == Level.INFO) {
            color =  ANSI_BLUE;
        }

        if(level == Level.FINEST) {
            color =  ANSI_CYAN;
        }

        StringBuilder builder = new StringBuilder(1000);
        builder.append(color);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append("] - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append(ANSI_RESET);
        builder.append("\n");

        return builder.toString();

    }

    public String getHead(Handler h) {


        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }
}
