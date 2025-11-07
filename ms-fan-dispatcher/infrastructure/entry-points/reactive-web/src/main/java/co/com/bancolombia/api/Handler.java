package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.CreateBatchRequestDTO;
import co.com.bancolombia.api.dto.response.CreateBatchResponseDTO;
import co.com.bancolombia.api.helper.ValidationRequest;
import co.com.bancolombia.api.helper.dto.*;
import co.com.bancolombia.usecase.fan.CreateBatchUseCase;
import co.com.bancolombia.usecase.fan.ResultUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final ValidationRequest validationRequest;
    private final ResultUseCase resultUseCase;
    private final CreateBatchUseCase createBatchUseCase;

    public Mono<ServerResponse> listenPOSTCreateUseCase(ServerRequest serverRequest) {
        return CreateBatchRequestBuilder.getDataFromRequest(serverRequest, validationRequest)
                .map(CreateBatchRequestMapper::dtoToModel)
                .flatMap(createBatchUseCase::createBatch)
                .flatMap(response -> ServerResponse.ok().bodyValue(
                        CreateBatchResponseDTO.builder()
                                .result("Se crearon correctamente! para " + response )
                                .build()));
    }

    public Mono<ServerResponse> listenPOSTResultPocketUseCase(ServerRequest serverRequest) {
        return ResultRequestBuilder.getDataFromRequest(serverRequest, validationRequest)
                .map(ResultRequestMapper::dtoToModel)
                .flatMap(resultUseCase::getResults)
                .map(ResultResponseMapper::modelToDto)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));

    }

}
