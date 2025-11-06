package co.com.bancolombia.api.helper.dto;

import co.com.bancolombia.api.dto.request.CreateBatchRequestDTO;
import co.com.bancolombia.api.helper.ValidationRequest;
import co.com.bancolombia.model.exceptions.ContractException;
import co.com.bancolombia.model.exceptions.messages.ContractExceptionEnum;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@UtilityClass
public class CreateBatchRequestBuilder {

    public Mono<CreateBatchRequestDTO> getDataFromRequest(ServerRequest serverRequest,
                                                          ValidationRequest validationRequest) {
        return serverRequest.bodyToMono(CreateBatchRequestDTO.class)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new ContractException(ContractExceptionEnum.BAD_REQUEST_BODY_NULL,
                                ContractExceptionEnum.BAD_REQUEST_BODY_NULL.getMessage()))))
                .flatMap(validationRequest::validateData);
    }

}
