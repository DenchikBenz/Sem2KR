package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class LogFile {
    private final PrintWriter logWriter;

    public LogFile(String logFileName) throws IOException {
        this.logWriter = new PrintWriter(new FileWriter(logFileName, true), true);
    }

    public synchronized void log(String message) {
        logWriter.println(message);
    }

    public void close() {
        logWriter.close();
    }
}
