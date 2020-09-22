package appendix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Класс для обработки данных из csv-файла
 */

public class ReadCSV {
    public static void main(String[] args) {

        Path path = Path.of("data/appendix/data.csv");
        try (Stream<String> lines = Files.lines(path)) {

            lines.filter(line -> !line.startsWith("#"))
                    .flatMap(ReadCSV::lineToPerson)
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Stream<Person> lineToPerson(String line) {
        // На случай, если что-то пойдет не так (например строка в файе не будет соответствовать заданному условию),
        // поймаем исключение и вернем пустой stream, который не приведет к ошибке в отличие от, например, возвращешия null.
        // Поэтому возвращать будем не Person, а Stream<Person>
        try {
            Person person = new Person();
            String[] elements = line.split(";");
            person.setName(elements[0]);
            person.setAge(Integer.parseInt(elements[1]));
            person.setCity(elements[2]);
            return Stream.of(person);
        } catch (Exception e) {
            System.out.println("Вот эта строчка была так себе");
        }
        return Stream.empty();
    }
}
