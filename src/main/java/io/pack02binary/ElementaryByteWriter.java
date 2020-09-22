package io.pack02binary;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Для работы с бинарными данными есть два своих абстрактных базовых класса - InputStream и OutputStream.
 * Имплементации по типу носителя - File-, ByteArray-, Socket- Input/OutputStream;
 * Имплементации, добавляющие функционал - Buffered-, GZip-, Zip-, Object- Input/OutputStream;
 * Имплементации для работы с примитивами и строками - DataInputStream и DataOutputStream.
 *
 */

public class ElementaryByteWriter {
    public static void main(String[] args) throws IOException {

        String filepath = "data/pack02/binary/file.bin";
        String zipFilePath = "data/pack02/binary/zip-file.bin.gz";
        Path path = Path.of(filepath);
        Files.createDirectories(path.getParent());

        Path zipFile = Path.of(zipFilePath);

//        writeBinaryFile(path);
//        inMemoryArrayIO();
        writeZip(zipFile);
    }

    // Простой пример
    private static void writeBinaryFile(Path path) {

        try (OutputStream outputStream = new FileOutputStream(path.toFile());
                DataOutputStream dos = new DataOutputStream(outputStream)) {

            final IntStream range = IntStream.range(0, 100);
            range.forEach(i -> {
                try {
                    dos.writeInt(i);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            System.out.println("File " + path + " size: " + Files.size(path) + " bytes");
        } catch (IOException ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
    }

    // Запись zip-файла. Отличается только добавлением декоратора GZIPOutputStream и все. Очень просто
    // Если убрать StandardOpenOption.CREATE, то нужно добавить dos.close()... Непонятно
    private static void writeZip(Path path) {

        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE);
             GZIPOutputStream gzos = new GZIPOutputStream(os);
             DataOutputStream dos = new DataOutputStream(gzos);
            ) {

            IntStream.range(0, 100).forEach(i -> {
                try {
                    dos.writeInt(i);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            System.out.println("File " + path + " size: " + Files.size(path) + " bytes");
        } catch (Exception ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
    }

    //
    private static void writeArchive(Path path) {

        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE);
             ZipOutputStream zos = new ZipOutputStream(os);
             DataOutputStream dos = new DataOutputStream(zos)) {

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
    Пример работы с чтением-записью в буфер в памяти
     */
    private static void inMemoryArrayIO() {

        // Создаем стрим для записи в память и оборачиваем его в DataOutputStream.
        // Внутри имплементации ByteArrayOutputStream скрыт массив байтов
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {

            // записываем данные в буфер
            dos.writeInt(7);
            dos.writeInt(8);
            dos.writeInt(8);

            byte[] byteArray = baos.toByteArray();
            System.out.println(Arrays.toString(byteArray));

            try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                 DataInputStream dis = new DataInputStream(bais)) {

                System.out.println(dis.readInt());
                System.out.println(dis.readInt());
                System.out.println(dis.readInt());
                System.out.println(dis.readInt());
            }

        } catch (Exception ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
    }
}
