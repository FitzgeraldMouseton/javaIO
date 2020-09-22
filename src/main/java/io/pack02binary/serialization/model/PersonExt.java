package io.pack02binary.serialization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Создадим класс-аналог Person для сериализации при помощи интерфейса Externalizable. Просто для наглядности,
 * чтобы не пихать все в Person. Имплементация Externalizable заставляет реализовать два метода. Поля не добавляются
 * в конечный файл (как если бы у них стояло слово transient), вся реализация прописывается в методе writeExternal
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonExt implements Externalizable {

    private String name;
    private int age;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        String primaryKey = name + "::" + age;
        out.write(primaryKey.getBytes());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        byte[] buffer = new byte[1024];
        final int read = in.read(buffer);
        String content = new String(buffer, 0, read);
        final String[] strings = content.split("::");
        this.name = strings[0];
        this.age = Integer.parseInt(strings[1]);
    }
}
