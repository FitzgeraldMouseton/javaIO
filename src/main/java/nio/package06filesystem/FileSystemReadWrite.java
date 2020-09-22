package nio.package06filesystem;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;

public class FileSystemReadWrite {
    public static void main(String[] args) throws URISyntaxException, IOException {

        /*
        Создание файла. Если файл дан нам в виде File file = new File("/tmp"), то нам нужно сначала создать
        объект нужной FS, и при помощи него создать файл. Если же у нас не file, а URI (URI file = new URI("file:///tmp")),
        то тут уже содержится информация о том, в какой FS нужно его создать, и мы можем это сделать при помощи FileSystemProvider.
        Создание файла через path получается в дефолтной FS, т.к. в основе Path.of() лежит FileSystems.getDefault().getPath(first, more).
        Однако path можно получить, передав URI со схемой нуной FS в качестве параметра
         */

        File file = new File("data/pack06/tmp1");
        URI uri = new URI("file:///Users/scholar/IdeaProjects/javaIO/data/pack06/files");

        // Создалим директорию. Для этого получим экземпляры FileSystem и FileSystemProvider. По умалчанию используется
        // ФС для работы с диском.
        FileSystem defaultFS = FileSystems.getDefault();
        FileSystemProvider defaultFSP = defaultFS.provider();

        // Можем создать Path для дефолтной ФС, а можем - для конкретной
        Path path = Path.of("data/pack06/files");
//        Path path = Path.of(URI.create("file:///Users/scholar/IdeaProjects/javaIO/data/pack06/files"));

        // Этот метод создает по одной папке зараз. Он лежит в основе метода Files.createDirectories();
        defaultFSP.createDirectory(path);
    }
}
