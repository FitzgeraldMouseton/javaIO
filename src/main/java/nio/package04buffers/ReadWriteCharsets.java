package nio.package04buffers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ReadWriteCharsets {
    public static void main(String[] args) {

        Charset latin1 = StandardCharsets.ISO_8859_1;
        Charset utf8 = StandardCharsets.UTF_8;
        String hello = "Bonjour, Monsieur sóuris";
        Path path = Path.of("data/pack04/channels/file.txt");

        // Для работы со стоками мы должны использовать CharBuffer
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put(hello);
        charBuffer.flip();

        // Однако Channel не может работать с CharBuffer
        ByteBuffer byteBuffer = latin1.encode(charBuffer);

        writeFile(path, byteBuffer);

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
            CharBuffer charBuffer = StandardCharsets.ISO_8859_1.decode(byteBuffer);
            System.out.println(new String(charBuffer.array()));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
