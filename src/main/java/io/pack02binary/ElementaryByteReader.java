package io.pack02binary;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

public class ElementaryByteReader {
    public static void main(String[] args) {

        String filepath = "data/pack02/binary/file.bin";
        String zipFilePath = "data/pack02/binary/zip-file.bin.gz";
        Path path = Path.of(filepath);
        Path zipFile = Path.of(zipFilePath);

//        readBinaryFile(path);
        readZipFile(zipFile);
    }

    // Чтение бинарного файла, состоящего из интеджеров
    private static void readBinaryFile(Path path) {
        try (InputStream is = Files.newInputStream(path);
             DataInputStream dis = new DataInputStream(is)) {

            try {
                while (true) {
                    System.out.println(dis.readInt());
                }
            } catch (EOFException ex) {

            }

        } catch (IOException ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
    }

    // Чтение zip-файла. Опять разница в одну строчку
    private static void readZipFile(Path path) {
        try (InputStream is = Files.newInputStream(path);
             GZIPInputStream gzis = new GZIPInputStream(is);
             DataInputStream dis = new DataInputStream(gzis)) {

            try {
                while (true) {
                    System.out.println(dis.readInt());
                }
            } catch (EOFException ex) {

            }

        } catch (IOException ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
    }
}
