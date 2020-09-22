package io.pack03hybrid.util;

import io.pack03hybrid.Fable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class AesopReader {

    public List<Fable> readFable(String fileName) {

        List<Fable> fables = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName));) {

            reader.readLine();
            reader.readLine();
            reader.readLine();

            String title = reader.readLine().trim();
            while (title != null) {
                Fable fable = new Fable();
                fable.setTitle(title);

                reader.readLine();

                String nextLine = reader.readLine().trim();
                boolean done = false;
                while (!done) {
                    done = false;
                    fable.addText(nextLine);
                    nextLine = reader.readLine();
                    if (nextLine == null) {
                        done = true;
                    } else if (nextLine.trim().isEmpty()) {
                        nextLine = reader.readLine().trim();
                        if (nextLine.isEmpty()) {
                            done = true;
                        }
                    }
                }
                fables.add(fable);

                if (nextLine == null) {
                    title = null;
                } else {
                    title = reader.readLine().trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fables;
    }
}

