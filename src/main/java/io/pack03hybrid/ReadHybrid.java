package io.pack03hybrid;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;

public class ReadHybrid {

    public static void main(String[] args) throws IOException {

        int lineOfTheFirstFable = 1;
        int n = 290;

        String pathName = "data/pack03/hybrid/aesop-fables/aesop-compressed.bin";
        Path path = Path.of(pathName);
        int size = (int) Files.size(path);

        try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
             BufferedInputStream bis = new BufferedInputStream(is)) {

            /*
             Для того, чтобы потом можно было сделать reset() (а нам это понадобится), нужно заранее вызвать mark().
             В качестве аргумента в этот метод нужно (внезапно!) передать лимит количества байтов, после прочтения которого
             позиция метки(?) (mark position) (вдруг с какой-то стати) станет невалидной. Передадим размер файла + 1.
             */
            bis.mark(size + 1);
            /*
            Нам нужен LineNumberReader, чтобы иметь возможность читать с определенной строки.
            Однако LineNumberReader может декорировать только другой Reader, поэтому нам нужен
            "переходник" между ним и BufferedInputStream.
             */
            InputStreamReader isr = new InputStreamReader(bis);
            LineNumberReader reader = new LineNumberReader(isr);
            reader.readLine();
            while (reader.getLineNumber() < n + lineOfTheFirstFable) {
                reader.readLine();
            }

            String fableData = reader.readLine();
//            System.out.println(fableData);

            int offset = Integer.parseInt(fableData.substring(0, 7).trim());
            int length = Integer.parseInt(fableData.substring(9, 16).trim());
            String title = fableData.substring(16);
//            System.out.printf("%d %d %s\n", offset, length, title);

            /*
            Благодаря строчке оглавления у нас есть данные о расположении нужной басни - мы знаем, сколько нужно отступить
            от начала файла до начала басни, и длину басни. Отлично.
             */
            bis.reset();
            /*
            Еще один классный метод - согласно документации, может скипнуть "немножко" меньше байтов, чем просили,
            возможно, 0 (sic!). Очень удобно, спасибо. Поэтому здесь не обойтись без такой крокозябры.
            Без нее получим баги. Оч странно.
             */
            //TO DO
            int skip = (int) bis.skip(offset);
            int totalSkip = skip;
            while (totalSkip < offset) {
                skip = (int) bis.skip(offset - totalSkip);
                totalSkip += skip;
            }

            // Дальше читаем нужный кусок текста
            byte[] buffer = new byte[4096];
            int read = bis.read(buffer);
            ByteArrayInputStream bis2 = new ByteArrayInputStream(buffer, 0, length);
            GZIPInputStream giz = new GZIPInputStream(bis2);

            byte[] buffer2 = new byte[4096];
            int bytesDecompressed = giz.read(buffer2);

            System.out.println(new String(buffer2, 0, bytesDecompressed));


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
