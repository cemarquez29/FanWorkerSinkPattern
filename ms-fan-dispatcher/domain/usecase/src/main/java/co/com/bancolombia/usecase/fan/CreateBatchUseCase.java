package co.com.bancolombia.usecase.fan;

import co.com.bancolombia.model.CreateBatchModel;
import co.com.bancolombia.model.gateway.SqsSenderGateway;
import co.com.bancolombia.model.numberentry.FanEntry;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class CreateBatchUseCase {
    private final SqsSenderGateway sqsSenderGateway;

    public Mono<Void> createBatch(CreateBatchModel model) {
        final String batchId = generateBatchId();

        return Flux.range(1, model.getSize())
                .map(seq -> new FanEntry(
                        batchId,
                        seq,
                        randomBetween(model.getMin(), model.getMax()),
                        String.valueOf(LocalDateTime.now())
                ))
                .flatMap(sqsSenderGateway::send,  16)
                .then(Mono.empty());
    }

    private static int randomBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static String generateBatchId() {
        return "BATCH-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                + "-" + Integer.toHexString(ThreadLocalRandom.current().nextInt());
    }

}
