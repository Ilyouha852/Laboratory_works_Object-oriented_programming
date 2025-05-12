package utils;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class Logger {
    private static final String LOG_FILE = "logs.txt";
    private static final SimpleDateFormat DATE_FORMAT = 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void log(String action, String details) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String logEntry = String.format("[%s] %s: %s\n",
                DATE_FORMAT.format(new Date()),
                action,
                details
            );
            writer.write(logEntry);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка записи в лог: " + ex.getMessage());
        }
    }

    public static void log(String action) {
        log(action, "");
    }
}
