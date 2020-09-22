package io.pack03hybrid;

import io.pack03hybrid.util.AesopReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * Для гибридных потоков мы имеем два базовых класса - InputStreamReader и OutputStreamWriter, которые экстендят
 * Reader и Writer, соответственно. Они могут читать строки и символы, но их работа основана на InputStream и
 * OutputStream, которые передаются в качестве параметра конструктора. Вторым параметром может передаваться Charset.
 *
 * ТЗ. Имеется файл с баснями Эзопа. Нужно переписать этот файл в следующем формате:
 * Aesop's Fables
 * 291
 *   1235       123  The Wolf and The Lamb          (место в файле (оффсет) и размер басни)
 *   3271       245  The Bat and The Weasels
 * <текст басен в сжатом формате>
 *
 * Здесь соседствуют текст и бинарные данные, следовательно для такой задачи нужно использовать как раз
 * гибридные потоки. Еще здесь присутствует немало хардкода, но для примера - и таак сойдет...
 */

public class WriteHybrid {
    public static void main(String[] args) throws IOException {

        AesopReader aesopReader = new AesopReader();
        // Складываем все басни в одну коллекцию
        List<Fable> fables = aesopReader.readFable("data/pack03/aesop.txt");
        // Создадим лист с FableData - понадобится немного позже
        List<FableData> fableDataList = new ArrayList<>();

        // Создаем aesopBos - в этот поток мы запишем итоговый файл (а начнем его писать прям сразу)
        ByteArrayOutputStream aesopBos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(aesopBos, StandardCharsets.UTF_8);
        PrintWriter pw = new PrintWriter(osw);

        // Запишем в aesopBos первые две строчки и список басен и вызовем flush(), т.к. мы пока ничего не закрываем,
        // а записать надо. Делаем мы это для вычисления оффсетов и длин басен, потом этот кусок будет перезаписан с
        // вычисленными данными вместо нулей.
        pw.println("Aesop's Fables");
        pw.printf("%d\n", fables.size());
        for (Fable fable : fables) {
            pw.printf("%7d %7d %s\n", 0, 0, fable.getTitle());
        }
        pw.flush();
        osw.close();//

        // У нас сейчас в aesopBos две строчки и список басен - это и есть отступ от начала файла до первой басни
        int textOffset = aesopBos.size();

        // Оставим на время общий поток aesopBos в покое и создадим новый поток, а который запишем басни в сжатом виде.
        ByteArrayOutputStream allZippedFablesBos = new ByteArrayOutputStream();
        int offset = textOffset;

        /*
          Что тут происходит - для каждой басни мы открываем новый ByteArrayOutputStream fableBos, в который
        записываем сжатую версию басни, а потом эту сжатую версию вписываем в общий allZippedFablesBos.
        При этом мы записываем данные об оффсете и длине каждой сжатой басни в FableData.
        Для первой он будет равен textOffset, для второй - textOffset + длина первой басни и т.д.
         */
        for (Fable fable : fables) {
            ByteArrayOutputStream fableBos = new ByteArrayOutputStream();
            try(GZIPOutputStream zos = new GZIPOutputStream(fableBos)) {

                zos.write(fable.getText().getBytes());

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            int length = fableBos.size();
            allZippedFablesBos.write(fableBos.toByteArray());
            FableData fableData = new FableData(fable, offset, length);
            offset += length;
            fableDataList.add(fableData);
        }
        allZippedFablesBos.close();

        // Здесь мы возвращаемся к записи в общий поток aesopBos и записываем наше "оглавление"
        aesopBos = new ByteArrayOutputStream();
        osw = new OutputStreamWriter(aesopBos, StandardCharsets.UTF_8);
        pw = new PrintWriter(osw);

        pw.println("Aesop's Fables");
        pw.printf("%d\n", fables.size());
        for (FableData fableData : fableDataList) {
            pw.printf("%7d %7d %s\n", fableData.getOffset(), fableData.getLength(), fableData.getFable().getTitle());
        }
        pw.flush();

        // А теперь допишем всю сжатую информацию в общий файл
        aesopBos.write(allZippedFablesBos.toByteArray());
        aesopBos.close();

//        System.out.println(new String(aesopBos.toByteArray()));

        // Последнее действие - сохраняем все в файл. Фухх, блин...
        Path path = Path.of("data/pack03/hybrid/aesop-fables/aesop-compressed.bin");
        Files.createDirectories(path.getParent());

        try (OutputStream os = new FileOutputStream(path.toFile())) {

            os.write(aesopBos.toByteArray());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(Files.size(Path.of("data/pack03/hybrid/aesop-fables/aesop-compressed.bin")));
    }
}
