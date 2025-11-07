package co.com.bancolombia.model.numberentry;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class NumberEntry {
    private String batchId;
    private int seq;
    private int value;
    private String timestamp;

    public NumberEntry(String batchId, int seq, int value, String timestamp) {
        this.batchId = batchId;
        this.seq = seq;
        this.value = value;
        this.timestamp = timestamp;
    }


    public long square() {
        return (long) value * value;
    }

    public void updateToSquare() {
        this.value = (int) this.square();
    }

}