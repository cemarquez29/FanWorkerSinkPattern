package co.com.bancolombia.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreateBatchModel {
    private int size;

    private int max;

    private int min;
}
