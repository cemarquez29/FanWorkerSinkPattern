package co.com.bancolombia.api.dto.response;

import jakarta.validation.Valid;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreateBatchResponseDTO {

    private String result;
}
