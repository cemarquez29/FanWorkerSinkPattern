package co.com.bancolombia.api.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponse {
    private Integer status;
    private String title;
    private List<ErrorDetail> errors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ErrorDetail {
        private String code;
        private String detail;
    }
}

