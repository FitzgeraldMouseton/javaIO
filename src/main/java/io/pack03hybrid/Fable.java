package io.pack03hybrid;

import lombok.Data;

@Data
public class Fable {

    private String title;
    private String text;

    public void addText(String text) {
        if (this.text == null) {
            this.text = text;
        } else {
            this.text += "\n" + text;
        }
    }

    @Override
    public String toString() {
        return "Fable [title=" + title + ", text=" + text + "]";
    }
}
