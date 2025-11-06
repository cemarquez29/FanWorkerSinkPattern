package co.com.bancolombia.model.gateway;

import co.com.bancolombia.model.numberentry.WorkerEntry;
import reactor.core.publisher.Mono;

public interface DynamoRepositoryGateway {
    public Mono<Void> save(WorkerEntry newEntry);
}
