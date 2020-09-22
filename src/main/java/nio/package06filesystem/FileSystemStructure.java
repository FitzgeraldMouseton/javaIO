package nio.package06filesystem;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

/**
 * Основная информация - Evernote
 */
public class FileSystemStructure {
    public static void main(String[] args) throws IOException {


        FileSystemProvider.installedProviders().forEach(fileSystemProvider -> System.out.println(fileSystemProvider.getScheme()));

        // Два способа поучения текущей файловой системы. В "file:///" - "file://" - схема, "/" - корневая папка
        FileSystem defaultFS = FileSystems.getDefault();
        FileSystem defaultFS2 = FileSystems.getFileSystem(URI.create("file:///"));

        // У фаловой системы можно получать различные ее аттрибуты
        defaultFS.getRootDirectories().forEach(System.out::println);

        // Интересный факт - в FS API везде возвращаются Iterable, а не лист, т.к. Iterable - "ленивая", и соответственно,
        // более легкая структура
        Iterable<FileStore> fileStores = defaultFS.getFileStores();
        fileStores.forEach(fileStore -> System.out.println(fileStore.name() + ": " + fileStore.type()));

    }

    /*
    Есть три базовых интерфейса для получения параметров - BasicFileAttributes, DosFileAttributes и PosixFileAttributes.
    Вторые два расширяют первый.
    */
    // 1 способ
    private static void getFileAttributesA() throws IOException {
        Path path = Path.of("");
        FileSystem defaultFS = FileSystems.getDefault();
        FileSystemProvider provider = defaultFS.provider();
        DosFileAttributes windowsAttributes = provider.readAttributes(path, DosFileAttributes.class);
        PosixFileAttributes linuxAttributes = provider.readAttributes(path, PosixFileAttributes.class);
    }

    // 2 способ
    // В статическом методе класса Files инкапсулирован код из метода getFileAttributesA().
    private static void getFileAttributesB() throws IOException {
        Path path = Path.of("");
        DosFileAttributes dosFileAttributes = Files.readAttributes(path, DosFileAttributes.class);
        PosixFileAttributes posixFileAttributes = Files.readAttributes(path, PosixFileAttributes.class);
    }
}
