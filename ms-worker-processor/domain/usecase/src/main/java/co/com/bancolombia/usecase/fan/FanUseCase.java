package co.com.bancolombia.usecase.fan;

import co.com.bancolombia.model.gateway.SqsSenderGateway;
import co.com.bancolombia.model.numberentry.NumberEntry;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FanUseCase {
    private final SqsSenderGateway sqsSenderGateway;
    public Mono<Void> elevarCuadrado(NumberEntry newEntry) {
        newEntry.updateToSquare();
        return sqsSenderGateway.send(newEntry).then();
    }
}
