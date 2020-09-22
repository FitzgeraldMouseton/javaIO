package io.pack01chars;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
Простейшие операции для записи файлов.
Для работы с текстовыми файлами в java имеются два базовых абстрактных класса - Writer и Reader.
От них унаследованы различные имплементации, которые делятся на два типа:
1) По типу источника чтения/записи - диск (FileReader), память (CharArrayReader, StringReader);
2) По типу добавляемого функционала - BufferedReader, LineNumberReader, PushbackReader (примерно то же и для writer'ов).
Как правило первые "оборачиваются" во вторые при помощи паттерна декоратор (new BufferedReader(new FileReader(file)))

Path и File являются чем-то вроде модели представления файла или директории. File существует с первой версии java,
а Path был добавлен в NIO2. Path содержит весь функционал File и всякие дополнительные штуки (через статические методы Files).
В частности Path, будучи интерфейсом, имеет (в отличие от File) имплементации для различных файловых и операционных систем,
позволяя более полно использовать их возможности. Соответственно, нет особых причин использовать File в новых приложениях.
Тем более что File и Path легко конвертируются друг в друга.

Создание конкретных экземпляров при помощи конструктора (new BufferedReader, например) по-прежнему вполне себе используется,
однако в nio2 была добавлена возможность их создания при помощи фабричного метода интерфейса Files (Files.newBufferedReader(path)).
Среди преимуществ нового метода есть такая важная вещь, как возможность передать нужную кодировку в качестве второго параметра.

 Как правило запись идет не сразу на носитель, а в буфер в памяти. Поэтому в конце нужно вызвать метод flush(), однако
 этот метод автоматически вызовется при закрытии потока.
 */
public class ElementaryWriter {
    public static void main(String[] args) throws IOException {

        // Создадим path c адресом, по которому хотим создать новый файл. Далее создадим дерево каталогов
        // Создавать Path через Paths.get() не рекомендуется, т.к. в будущих версиях java этот класс может быть помечен @deprecated
        // (Просто раньше не было возможности создавать статические методы у интерфейсов)
        Path path = Path.of("data/pack01/chars/textfile.txt");
        Files.createDirectories(path.getParent());

        writeFileA(path);
        writeFileB(path);
//        writeFileC(path);
        writeFileD();

    }

    // Простейший метод для записи строки в файл. Можно обернуть writer в BufferedWriter, например.
    private static void writeFileA(Path path) {
        try (Writer writer = new FileWriter(path.toFile())) {
            writer.write("Hello");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
    Более продвинутый метод, в котором мы используем возможности нового метода создания BufferedReader.
    Позволяет записать текст в желаемой кодировке, а так же добавляет возможность открывать файл с определенными намерениями:
    1) CREATE - создаст файл, если он отсутствует
    2) CREATE_NEW - то же самое, но если файл уже существует, выбросится исключение
    3) WRITE - запишет или перезапишет файл
    4) APPEND - информация допишется в конец файла
    5) DELETE_ON_CLOSE - удалит файл после закрытия
     */
    private static void writeFileB(Path path) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND)) {
            bufferedWriter.write(", mouse!");
            bufferedWriter.newLine();
            bufferedWriter.write("Good day, neighbour, innit?");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
    В этом методе мы используем метод append(), однако он работает не совсем так, как можно ожидать. Он добавляет
    текст к тевущему writer'у, а не к файлу. То есть, если у нас есть файл, в котором хранятся все самые важные для нас данные,
    и мы решим дописать в него еще что-то важное методом append(), то мы просто тупо перезапишем этот файл к чертовой матери.
    UPD. Если немного подумать, то можно понять, что все так и должно работать. Мы записываем файл в память через этот writer,
    а уже потом, либо методом flush(), либо закрытием канала сбрасываем данные на диск. Так оно и работает.
     */
//    private static void writeFileC(Path path) {
//        try (Writer writer = new FileWriter(path.toFile())) {
//            writer.write("Good day, neighbour, innit?");
//            writer.append("\nDamn.. Yes, definitely it is a wonderful day! Fuck you, moron..", 7, 45);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//    }

    // Создадим еще один файл при помощи PrintWriter
    private static void writeFileD() {
        Path path = Paths.get("data/pack01/chars/formatted-text-file.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

            // Запишем строчку
            printWriter.println("Hello, Mr. Mouse");

            // Запишем что-нибудь с форматированием
            printWriter.printf("%d %o 0x%04x\n", 12, 8, 248);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
