package co.com.bancolombia.usecase.fan;

import co.com.bancolombia.model.gateway.DynamoRepositoryGateway;
import co.com.bancolombia.model.numberentry.WorkerEntry;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FanUseCase {
    private final DynamoRepositoryGateway dynamoRepositoryGateway;
    public Mono<Void> elevarCuadrado(WorkerEntry newEntry) {
        return dynamoRepositoryGateway.save(newEntry).then();
    }
}
