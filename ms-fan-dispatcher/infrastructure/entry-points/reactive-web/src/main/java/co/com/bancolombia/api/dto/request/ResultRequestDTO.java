package co.com.bancolombia.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Valid
@Builder(toBuilder = true)
public class ResultRequestDTO {
    @NotBlank(message = "%BAD_REQUEST_EMPTY_FIELD%")
    private String batchId;
}
