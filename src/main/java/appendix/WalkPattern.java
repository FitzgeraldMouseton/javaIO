package appendix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 *  Обход дерева каталогов и файлов при помощи walk()
 */
public class WalkPattern {
    public static void main(String[] args) throws IOException {

        Path path = Path.of("data/appendix/media");
        Stream<Path> fileTree = Files.walk(path);

        long countDirectories = Files.walk(path).filter(Files::isDirectory).count();
        long countFiles = Files.walk(path).filter(Files::isRegularFile).count();

        System.out.println("Directories: " + countDirectories);
        System.out.println("Files: " + countFiles);
    }
}
