package io.pack02binary.serialization;

import io.pack02binary.serialization.model.Person;
import io.pack02binary.serialization.model.PersonExt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Сериализация - это процесс записи java-объекта в какую-то форму представления этого объекта, которая может быть передана по сети,
 * сохранена на диске и т.п. с возможностью, разумеется, восстановить изначальный объект, когда потребуется. Это могут быть,
 * например, xml-, json-файл или просто какая-то нечитаемая последовательность байтов.
 * В этом примере мы будем сериализировать объекты класса Person. Для того, чтобы заняться сериализацией/десериализацией нам понадобятся:
 * 1) Состояние класса (логично и понятно)
 * 2) Название класса (тоже понятно)
 * 3) Некоторый id (версия) класса. Пример - один сервис посылает другому сервису объект класса Person. Но у того сервиса уже есть своя версия
 * этого класса (например у первого сервиса у Person есть поля имя и возраст, а у второго - имя, возраст и умение скакать на лошади (boolean)).
 * И тут будет полезной возможность отличить один клас от другого.
 * Для того, чтобы сделать объект сериалищуемым, нужно заимплементировать интерфейс Serializable, и создать в классе статическое поле
 * serialVersionUID. Однако можно и не добавлять, потому что JVM сама сгенерирует его, если понадобится.
 * По умолчанию все поля объекта сериализуются, если мы хотим что-то исключить, то к сигнатуре этого чего-то в нужно длбавить слово transient.
 * В java реализован свой механизм сериализации. Есть три способа его изменить:
 * 1) Создать у объекта пару методов - writeObject()/readObject()
 * 2) Имплементировать интерфейс Externalizable (вместо Serializable)
 * 3) Работа с прокси-объектами через методы readProvide() и readResolve();
 */

public class WritingDataObjects {

    public static void main(String[] args) throws IOException {
        String filePath = "data/pack02/binary/serialization/person-serializable.bin";
        String customFilePath = "data/pack02/binary/serialization/person-custom-serializable.bin";
        String externalizableFilePath = "data/pack02/binary/serialization/person-externalizable.bin";
        String proxyFilePath = "data/pack02/binary/serialization/person-proxy.bin";
        Path path = Path.of(filePath);
        Path customPath = Path.of(customFilePath);
        Path externalizablePath = Path.of(externalizableFilePath);
        Path proxyPath = Path.of(proxyFilePath);
        Files.createDirectories(path.getParent());

        Person person1 = new Person("Fitz", 34);
        Person person2 = new Person("Squirry", 29);
        List<Person> people = List.of(person1, person2);

        PersonExt personExt1 = new PersonExt("Fitz", 34);
        PersonExt personExt2 = new PersonExt("Squirry", 29);
        List<PersonExt> peopleExt = List.of(personExt1, personExt2);

        // Запускать с закомментированными readObject()/writeObject() и без transient у полей
//        serializePeople(path, people);
        // Запускать наоборот
//        serializePeople(customPath, people);
        //
//        serializePeople(externalizablePath, peopleExt);
        //
        serializePeople(proxyPath, people);

//        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
//             ObjectOutputStream oos = new ObjectOutputStream(os)) {
//
//            oos.writeObject(new Mouse("317", 25));
//            System.out.println("person.bin: " + Files.size(path));
//        } catch (IOException exception) {
//            exception.getLocalizedMessage();
//        }

    }

    private static void serializePeople(Path path, List<?> people) {
        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {

//            oos.writeObject(people);
            oos.writeObject(people.get(0));
            oos.writeObject(people.get(1));
            System.out.println("person.bin: " + Files.size(path));
        } catch (IOException exception) {
            exception.getLocalizedMessage();
        }
    }
}
