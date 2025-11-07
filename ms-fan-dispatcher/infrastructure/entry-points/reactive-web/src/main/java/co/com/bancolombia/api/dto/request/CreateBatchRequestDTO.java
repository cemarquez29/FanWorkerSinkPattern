package co.com.bancolombia.api.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Valid
@Builder(toBuilder = true)
public class CreateBatchRequestDTO {
    @NotNull(message = "%BAD_REQUEST_EMPTY_FIELD%")
    private int size;

    @NotNull(message = "%BAD_REQUEST_EMPTY_FIELD%")
    private int max;

    @NotNull(message = "%BAD_REQUEST_EMPTY_FIELD%")
    private int min;
}
