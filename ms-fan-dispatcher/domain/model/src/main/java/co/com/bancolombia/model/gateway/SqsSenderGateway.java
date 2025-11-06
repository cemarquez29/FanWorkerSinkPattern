package co.com.bancolombia.model.gateway;

import co.com.bancolombia.model.numberentry.FanEntry;
import reactor.core.publisher.Mono;

public interface SqsSenderGateway {
    public Mono<String> send(FanEntry newEntry);
}
