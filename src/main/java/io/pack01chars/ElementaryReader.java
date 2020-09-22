package io.pack01chars;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Простейшие операции для чтения файлов
 */
public class ElementaryReader {
    public static void main(String[] args) {

        Path path = Paths.get("data/pack01/chars/textfile.txt");

//        readFileA(path);
//        readFileB(path);
//        readFileC(path);
//        readFileD(path);
        readFileE(path);
    }


    // Простейший метод чтения текстового файла. Чтение происходит посимвольно, без буферов и всякого такого,
    // то есть вообще не эффективно
    private static void readFileA(Path path) {
        try (Reader reader = new FileReader(path.toFile())) {
            int chr = reader.read();
            while (chr != -1) {
                System.out.print((char) chr);
                chr = reader.read();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Более продвинутый способ с созданием BufferedReader через конструктор. BufferedReader добавляет метод
    // readline()
    private static void readFileB(Path path) {
        try (Reader reader = new FileReader(path.toFile());
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Вариация на тему BufferedReader с возможностью отслеживать номер строк
    private static void readFileC(Path path) {
        try (Reader reader = new FileReader(path.toFile());
             LineNumberReader lineNumberReader = new LineNumberReader(reader)) {
            String line;
            while ((line = lineNumberReader.readLine()) != null) {
                System.out.println(lineNumberReader.getLineNumber() + ": " + line);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Более современный способ создания BufferedReader. Позволяет, например, неправильно задать кодировку,
    // чтобы не суметь прочитать элементарный текстовый файл.
    private static void readFileD(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_16BE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Stream API, motherfucker!
    private static void readFileE(Path path) {
        try (Stream<String> lines = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1).lines()) {
            lines.forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
