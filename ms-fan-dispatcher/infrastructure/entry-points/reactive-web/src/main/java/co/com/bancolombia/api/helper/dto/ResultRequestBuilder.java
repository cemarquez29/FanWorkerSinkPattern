package co.com.bancolombia.api.helper.dto;

import co.com.bancolombia.api.dto.request.ResultRequestDTO;
import co.com.bancolombia.api.helper.ValidationRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@UtilityClass
public class ResultRequestBuilder {

    public Mono<ResultRequestDTO> getDataFromRequest(ServerRequest serverRequest,
                                                     ValidationRequest validationRequest) {
        return Mono.just(serverRequest)
                .map(ResultRequestBuilder::buildDto)
                .flatMap(validationRequest::validateData);
    }

    private ResultRequestDTO buildDto(ServerRequest serverRequest) {
        return ResultRequestDTO.builder()
                .batchId(serverRequest.queryParam("batchId").orElse(null))
                .build();
    }
}
