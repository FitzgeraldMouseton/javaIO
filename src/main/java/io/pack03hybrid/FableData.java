package io.pack03hybrid;

import lombok.Data;

/**
 * Объект, в который мы будем сохранять информацию о каждой конкретной юасне в процессе разбора файла
 */

@Data
public class FableData {

    private Fable fable;
    private int offset;
    private int length;

    public FableData(Fable fable, int offset, int length) {
        this.fable = fable;
        this.offset = offset;
        this.length = length;
    }
}
