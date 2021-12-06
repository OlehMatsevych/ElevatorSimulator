package Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class HTMLFormatter extends Formatter {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        var className = record.getLevel();
        builder.append("<tr>");

//        Loglevel
        builder.append("<td class= \"" + className + "\">");
        builder.append(record.getLevel());
        builder.append("</td>");

//        Time
        builder.append("<td>");
        builder.append(df.format(new Date(record.getMillis())));
        builder.append("</td>");

//        Source
        builder.append("<td>");
        builder.append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName());
        builder.append("</td>");

//        Message
        builder.append("<td>");
        builder.append(formatMessage(record));
        builder.append("</td>");
        builder.append("</tr>");

        return builder.toString();
    }

    public String getHead(Handler h) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                "\n" +
                "    <style>\n" +
                "      *,\n" +
                "      body,\n" +
                "      html {\n" +
                "        padding: 0;\n" +
                "        margin: 0;\n" +
                "        box-sizing: border-box;\n" +
                "      }\n" +
                "\n" +
                "      .WARNING {\n" +
                "        background-color: #ffeb3b;\n" +
                "      }\n" +
                "\n" +
                "      .INFO {\n" +
                "        background-color: #2196f3;\n" +
                "        color: #fff;\n" +
                "      }\n" +
                "\n" +
                "      .SEVERE {\n" +
                "        background-color: #f44336;\n" +
                "        color: #fff;\n" +
                "      }\n" +
                "\n" +
                "    </style>\n" +
                "    <title>Log file</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <table class=\"table table-hover table-striped\">\n" +
                "      <thead>\n" +
                "        <tr>\n" +
                "          <th>Loglevel</th>\n" +
                "          <th>Time</th>\n" +
                "          <th>Source</th>\n" +
                "          <th>Message</th>\n" +
                "        </tr>\n" +
                "      </thead>\n" +
                "      <tbody>";
    }

    public String getTail(Handler h) {
        return "</tbody>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>";
    }
}
