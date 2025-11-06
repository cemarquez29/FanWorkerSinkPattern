package co.com.bancolombia.api.exception;

import co.com.bancolombia.api.exception.dto.ErrorResponse;
import co.com.bancolombia.model.exceptions.ContractException;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.exceptions.messages.TechnicalExceptionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@UtilityClass
public class ErrorResponseBuilder {


    private record ErrorMetadata(int status, String title, String code, String detail) {
    }

    private record ErrorContext(ServerRequest request, Throwable throwable) {
    }

    public Mono<ErrorResponse> buildErrorResponse(ContractException ex, ServerRequest request,
                                                  ObjectMapper objectMapper) {
        var metadata = new ErrorMetadata(
                ex.getContractExceptionEnum().getStatus(),
                ex.getContractExceptionEnum().getTitle(),
                ex.getContractExceptionEnum().getCode(),
                ex.getMessage()
        );

        var errorContext = new ErrorContext(request, ex);
        return buildAndLogErrorResponse(metadata, errorContext, objectMapper);
    }

    public Mono<ErrorResponse> buildErrorResponse(TechnicalException ex, ServerRequest request,
                                                  ObjectMapper objectMapper) {
        var metadata = new ErrorMetadata(
                ex.getTechnicalExceptionEnum().getStatus(),
                ex.getTechnicalExceptionEnum().getTitle(),
                ex.getTechnicalExceptionEnum().getCode(),
                ex.getTechnicalExceptionEnum().getMessage()
        );
        var errorContext = new ErrorContext(request, ex);
        return buildAndLogErrorResponse(metadata, errorContext, objectMapper);
    }

    public Mono<ErrorResponse> buildErrorResponse(Throwable throwable, ServerRequest request,
                                                  ObjectMapper objectMapper) {
        var metadata = new ErrorMetadata(
                TechnicalExceptionEnum.UNEXPECTED_EXCEPTION.getStatus(),
                TechnicalExceptionEnum.UNEXPECTED_EXCEPTION.getTitle(),
                TechnicalExceptionEnum.UNEXPECTED_EXCEPTION.getCode(),
                TechnicalExceptionEnum.UNEXPECTED_EXCEPTION.getMessage()
        );
        var errorContext = new ErrorContext(request, throwable);
        return buildAndLogErrorResponse(metadata, errorContext, objectMapper);
    }

    private Mono<ErrorResponse> buildAndLogErrorResponse(ErrorMetadata metadata, ErrorContext errorContext,
                                                         ObjectMapper objectMapper) {


        var errorResponse = ErrorResponse.builder()
                .status(metadata.status())
                .title(metadata.title())
                .errors(List.of(ErrorResponse.ErrorDetail.builder()
                        .code(metadata.code())
                        .detail(metadata.detail())
                        .build()))
                .build();

        return Mono.just(errorResponse);
    }

    public Mono<ServerResponse> buildResponse(ErrorResponse errorResponse) {
        HttpStatus status = Optional.ofNullable(HttpStatus.resolve(errorResponse.getStatus()))
                .orElse(HttpStatus.BAD_REQUEST);

        return ServerResponse.status(status)
                .contentType(APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

}

