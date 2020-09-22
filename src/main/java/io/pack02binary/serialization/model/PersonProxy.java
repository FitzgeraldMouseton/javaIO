package io.pack02binary.serialization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ObjectStreamException;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonProxy implements Serializable {

    private String name;

    private Object readResolve() throws ObjectStreamException {

        final String[] strings = name.split("::");
        String name = strings[0];
        int age = Integer.parseInt(strings[1]);
        return new Person(name, age);
    }
}
