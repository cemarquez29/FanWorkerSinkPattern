package co.com.bancolombia.sqs.listener.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class FanEntryDTO {

    @NotBlank(message = "El campo bathId está vacio")
    String batchId;

    @NotNull(message = "El campo seq está vacio")
    int seq;

    @NotNull(message = "El campo value está vacio")
    int value;

    @NotBlank(message = "El campo timestamp está vacio")
    String timestamp;
}
