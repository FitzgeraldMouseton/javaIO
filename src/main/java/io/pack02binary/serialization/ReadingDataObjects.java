package io.pack02binary.serialization;

import io.pack02binary.serialization.model.Person;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ReadingDataObjects {
    public static void main(String[] args) {

        Path path = Path.of("data/pack02/binary/serialization/person-serializable.bin");
        Path customPath = Path.of("data/pack02/binary/serialization/person-custom-serializable.bin");
        Path externalizablePath = Path.of("data/pack02/binary/serialization/person-externalizable.bin");
        Path proxyPath = Path.of("data/pack02/binary/serialization/person-proxy.bin");

        try(InputStream is = Files.newInputStream(proxyPath, StandardOpenOption.READ);
            ObjectInputStream ois = new ObjectInputStream(is)) {

            // Read persons
            Person person1 = (Person) ois.readObject();
            System.out.println(person1);
            Person person2 = (Person) ois.readObject();
            System.out.println(person2);

            // Read personsExt
//            PersonExt personExt1 = (PersonExt) ois.readObject();
//            System.out.println(personExt1);
//            PersonExt personExt2 = (PersonExt) ois.readObject();
//            System.out.println(personExt2);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getClass());
            e.printStackTrace();
        }
    }
}
