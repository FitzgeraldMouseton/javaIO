package nio.package06filesystem;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Для работы с архивами применяется JarFS. В архивы можно копировать уже имеющуюся информацию, или записывать ее напрямую
 */
public class Archives {
    public static void main(String[] args) throws IOException {

        // Схема для jarFS выглядит так - "jar:file:///".
        URI zipFile = URI.create("jar:file:///Users/scholar/IdeaProjects/javaIO/data/pack06/file.zip");

        // Map с опциями, с которыми будет создаваться архив. В качестве ключей могут быть "create" и "encode" (если
        // в архив добавляется текстовый файл)
        Map<String, String> options = new HashMap<>();
        options.put("create", "true");

        /*
        Это действие создает ФС и требуемый файл (без родительских каталогов)
        Для дальнейшей работы с ФС, ее можно воспринимать как корневой каталог. В нем можно создавать новые файлы и
        каталоги - вся эта структура будет частью zip файла file.zip
         */
        try (FileSystem zipFS = FileSystems.newFileSystem(zipFile, options)) {
            // Это способ добавления данных в архив при помощи копированияуже имеющегося файла
            Path dir = zipFS.getPath("/fables");
            zipFS.provider().createDirectory(dir);
            Path aesop = Path.of("data/pack06/aesop.txt");
            Path aesopCompressed = zipFS.getPath("fables/aesop-compressed.txt");
            Files.copy(aesop, aesopCompressed);

            // Способ записи нарпямую в архив. Созадим в файле file.zip еще одну папку, а в ней файл. Для этого
            // в ФС zipFS создадим OutputStream (можно так же использовать FileChannel или ByteChannel)
            Path binDir = zipFS.getPath("/BinDir");
            Path binFile = zipFS.getPath("/BinDir/ints.bin");
            zipFS.provider().createDirectory(binDir);
            try (OutputStream os = zipFS.provider().newOutputStream(binFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                DataOutputStream dos = new DataOutputStream(os)) {

                IntStream.range(0, 1000).forEach(i -> {
                    try {
                        dos.writeInt(i);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }




        }
    }
}
