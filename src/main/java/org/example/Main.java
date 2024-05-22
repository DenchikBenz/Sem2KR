package org.example;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String variant = "3";
            String dirPath = "v" + variant;
            LogFile logFile = new LogFile("v" + variant + ".log");

            Map<Integer, String> textParts = new HashMap<>();

            File folder = new File(dirPath);
            File[] files = folder.listFiles();

            if (files != null) {
                List<Thread> threads = new ArrayList<>();

                for (File file : files) {
                    Thread thread = new Thread(new FileProcess(file, logFile, textParts));
                    threads.add(thread);
                    thread.start();
                }

                for (Thread thread : threads) {
                    thread.join();
                }
            }

            List<Integer> keys = new ArrayList<>(textParts.keySet());
            Collections.sort(keys);

            try (PrintWriter writer = new PrintWriter(new FileWriter("v" + variant + ".txt"))) {
                for (int key : keys) {
                    writer.print(textParts.get(key));
                }
            }

            logFile.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
