package io.pack02binary;

import java.io.*;
import java.nio.file.Path;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Работа с архивами средствами NIO не выглядит слишком уж удобной, и примеры здесь очень наивные.
 * Более осмысленная работа с архивами представлена в NIO2
 */

public class Archives {
    public static void main(String[] args) {

        String archivePath = "data/pack02/binary/archive.zip";
        Path archive = Path.of(archivePath);

//        writeArchive(archive);
        readArchive(archive);
    }

    // Запись архива. Вообще, довольно громоздкий способ, и мы попозже добавим более продвинутый вариант
    private static void writeArchive(Path path) {
        try (OutputStream os = new FileOutputStream(path.toFile());
             ZipOutputStream zos = new ZipOutputStream(os);
             DataOutputStream dos = new DataOutputStream(zos)) {

            // Для каждой директории и каждого файла нужно создать соответствующий объект ZipEntry
            ZipEntry dirEntry = new ZipEntry("bin/");
            zos.putNextEntry(dirEntry);

            ZipEntry binFileEntry = new ZipEntry("bin/ints.bin");
            zos.putNextEntry(binFileEntry);

            IntStream.range(0, 100).forEach(i -> {
                try {
                    dos.writeInt(i);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            ZipEntry otherDirEntry = new ZipEntry("text/");
            zos.putNextEntry(otherDirEntry);

            ZipEntry textFileEntry = new ZipEntry("text/file.txt");
            zos.putNextEntry(textFileEntry);

            dos.writeUTF("Hello, Mr. Mouse!");

        } catch (Exception ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
    }

    private static void readArchive(Path path) {
        try (InputStream is = new FileInputStream(path.toFile());
             ZipInputStream zis = new ZipInputStream(is);
             DataInputStream dis = new DataInputStream(zis)) {

            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {

                if (zipEntry.isDirectory()) {
                    System.out.println("Reading directory: " + zipEntry);
                } else {
                    if (zipEntry.getName().endsWith(".bin")) {
                        try {
                            while (true) {
                                System.out.println(dis.readInt());
                            }
                        } catch (EOFException ex) {

                        }
                    } else if (zipEntry.getName().endsWith(".txt")) {
                        System.out.println(dis.readUTF());
                    }
                }
                zipEntry = zis.getNextEntry();
            }

        } catch (Exception ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
    }
}
