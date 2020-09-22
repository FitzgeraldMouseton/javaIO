package appendix;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Посмотрим, как можно совершать обход дерева каталогов и файлов при помощи walkFileTree() (Java 7)
 */

public class WalkFileTreePattern {
    public static void main(String[] args) throws IOException {

        Path path = Path.of("data/appendix/media");
        System.out.println(Files.isDirectory(path));

        // Создадим visitor для подсчета количества файлов.
        // Если использовать не var, а как обычно, то код вроде visitor.countFiles не будет работать, т.к. в
        // интерфейсе FileVisitor нет такого поля. А с var'у все пофиг
        var visitor = new FileVisitor<Path>() {

            private long countFiles = 0L;
            private long countDirectories = 0L;

            @Override
            public FileVisitResult
            preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {

                countDirectories++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                countFiles++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        };

        Files.walkFileTree(path, visitor);

        System.out.println("Files: " + visitor.countFiles);
        System.out.println("Directories: " + visitor.countDirectories);
    }
}
