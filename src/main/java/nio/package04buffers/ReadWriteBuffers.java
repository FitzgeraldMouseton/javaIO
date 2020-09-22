package nio.package04buffers;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Описание основных концепций - в Evernote
 *
 * Основные этапы записи файла:
 * 1) Создаем буфер
 * 2) Одним из методов putXXX записываем данные
 * 3) Через FileChannel записываем данные из буфера в файл
 */

public class ReadWriteBuffers {
    public static void main(String[] args) throws IOException {

        Path path = Path.of("data/pack04/channels/file.bin");
        Files.createDirectories(path.getParent());

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.putInt(1);
        byteBuffer.putInt(2);
        byteBuffer.putInt(3);
        // Флипнем буфер, чтобы установить лимит на место курсора, а курсор вернуть в начало
        byteBuffer.flip();

        writeFile(path, byteBuffer);

        // Данные записаны в файл, можно очищать буфер
        byteBuffer.clear();

        readFile(path, byteBuffer);

    }

    private static void writeFile(Path path, ByteBuffer byteBuffer) {
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            fileChannel.write(byteBuffer);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void readFile(Path path, ByteBuffer byteBuffer) {
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {

            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            IntBuffer intBuffer = byteBuffer.asIntBuffer();
            try {
                while (true) {
                    System.out.println(intBuffer.get());
                }
            } catch (BufferUnderflowException ex) {

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
