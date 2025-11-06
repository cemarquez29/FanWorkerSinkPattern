package co.com.bancolombia.api.exception;

import co.com.bancolombia.api.exception.dto.ErrorResponse;
import co.com.bancolombia.model.exceptions.ContractException;
import co.com.bancolombia.model.exceptions.TechnicalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties resources,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer serverCodecConfigurer,
                                  ObjectMapper objectMapper) {
        super(errorAttributes, resources.getResources(), applicationContext);
        this.objectMapper = objectMapper;
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorMessage);
    }

    private Mono<ServerResponse> renderErrorMessage(ServerRequest request) {
        return just(request)
                .map(this::getError)
                .flatMap(Mono::error)
                .onErrorResume(ContractException.class, ex ->
                        ErrorResponseBuilder.buildErrorResponse(ex, request, objectMapper))
                .onErrorResume(TechnicalException.class, ex ->
                        ErrorResponseBuilder.buildErrorResponse(ex, request, objectMapper))
                .onErrorResume(Throwable.class, throwable ->
                        ErrorResponseBuilder.buildErrorResponse(throwable, request, objectMapper))
                .cast(ErrorResponse.class)
                .flatMap(ErrorResponseBuilder::buildResponse);
    }
}