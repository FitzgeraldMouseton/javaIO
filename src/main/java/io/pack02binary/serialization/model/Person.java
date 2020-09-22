package io.pack02binary.serialization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    // Так как мы переопределили способ сериализации, нам в конечном файле больше не нужны ссылки на эти два поля,
    // потому что мы сами записываем данные этих полей в файл в том виде, который почему-то считаем наиболее уместным
    // (Squirry::29)
    private  String name;
    private  int age;

    // =================================== 1. Переопределение методов для дефолтной сериализации =======================

    // Этот метод (должен иметь в точности такую сигнатуру) будет использоваться дефолтным механизмом java для сериализации
    // Тут мы сами решаем, как именно хотим сериализовать файл.
    private void writeObject(ObjectOutputStream oos) throws Exception {

        DataOutputStream dos = new DataOutputStream(oos);
        dos.writeUTF(name + "::" + age);
    }

    private void readObject(ObjectInputStream ois) throws Exception {

        DataInputStream dis = new DataInputStream(ois);
        String content = dis.readUTF();
        String[] strings = content.split("::");
        this.name = strings[0];
        this.age = Integer.parseInt(strings[1]);
    }

    // =================================== 2. Метод для сериализации при помощи прокси-объекта =========================

    private Object writeReplace() throws ObjectStreamException {

        return new PersonProxy(name + "::" + age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
