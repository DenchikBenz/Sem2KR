package org.example;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
public class FileProcess implements Runnable {
    private final File file;
    private final LogFile logFile;
    private final Map<Integer, String> textParts;

    public FileProcess(File file, LogFile logFile, Map<Integer, String> textParts) {
        this.file = file;
        this.logFile = logFile;
        this.textParts = textParts;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            int k = dis.readInt();
            int remainingBytes = (int) (file.length() - Integer.BYTES * 3);  // Оставшиеся байты для строки

            byte[] stringBytes = new byte[remainingBytes];
            dis.read(stringBytes);

            String s = new String(stringBytes, StandardCharsets.UTF_8);  // Преобразование байтов в строку
            int d = dis.readInt();  // контрольное число
            int p = dis.readInt();  // номер части

            int actualLength = s.length();  // Фактическое количество символов в строке
            int actualByteSize = s.getBytes(StandardCharsets.UTF_8).length;

            logFile.log(String.format("прочитали файл %s, кол-во байт данных: %d, фактическое кол-во байт: %d, кол-во считанных символов: %d, контрольное число: %d, номер части: %d",
                    file.getName(), k, actualByteSize, actualLength, d, p));

            if (actualLength == d && actualByteSize == k) {
                synchronized (textParts) {
                    textParts.put(p, s);
                }
            } else {
                logFile.log(String.format("Ошибка в файле %s: контрольное число или размер строки не совпадают с фактическими данными", file.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}