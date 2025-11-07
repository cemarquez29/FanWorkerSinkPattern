package co.com.bancolombia.api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResultResponseDTO {

    private List<String> results;
}
