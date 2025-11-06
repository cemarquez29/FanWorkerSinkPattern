package co.com.bancolombia.sqs.listener.helper;

import co.com.bancolombia.sqs.listener.config.SQSProperties;
import co.com.bancolombia.sqs.listener.dto.WorkerEntryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Log4j2
@Builder
public class SQSListener {
    private final @Qualifier("sqsListenerConfig")SqsAsyncClient client;
    private final SQSProperties properties;
    private final Function<Message, Mono<Void>> processor;
    private String operation;
    private final ObjectMapper mapper;
    private final MessageValidator messageValidator;

    public SQSListener start() {
        this.operation = "MessageFrom:" + properties.queueUrl();
        ExecutorService service = Executors.newFixedThreadPool(properties.numberOfThreads());
        Flux<Void> flow = listenRetryRepeat().publishOn(Schedulers.fromExecutorService(service));
        for (var i = 0; i < properties.numberOfThreads(); i++) {
            flow.subscribe();
        }
        return this;
    }

    private Flux<Void> listenRetryRepeat() {
        return listen()
                .doOnError(e -> log.error("Error listening sqs queue", e))
                .repeat();
    }

    private Flux<Void> listen() {
        return getMessages()
                .flatMap(message -> Mono.fromCallable(() -> parse(message))
                        .flatMap(messageValidator::validateData)
                        .cast(WorkerEntryDTO.class)
                        .flatMap(dto ->
                                processor.apply(message)
                                        .then(confirm(message))
                        )
                        .onErrorResume(ConstraintViolationException.class, e -> {
                            log.warn("Descartando mensaje inválido {}, {}", message.messageId(), e.getMessage());
                            return confirm(message);
                        })
                        .onErrorResume(JsonProcessingException.class, e -> {
                            log.warn("JSON mal formado {}, {}", message.body(), e.getOriginalMessage());
                            return confirm(message);
                        })
                        .onErrorResume(ex -> {
                            log.warn("Error transitorio, se reintentará. {}, {}", message.messageId(), ex.toString());
                            return Mono.empty();
                        }));
    }

    private WorkerEntryDTO parse(Message message) throws JsonProcessingException {
        return mapper.readValue(message.body(), WorkerEntryDTO.class);
    }

    private Mono<Void> confirm(Message message) {
        return Mono.fromCallable(() -> getDeleteMessageRequest(message.receiptHandle()))
                .flatMap(request -> Mono.fromFuture(client.deleteMessage(request)))
                .then();
    }

    private Flux<Message> getMessages() {
        return Mono.fromCallable(this::getReceiveMessageRequest)
                .flatMap(request -> Mono.fromFuture(client.receiveMessage(request)))
                .doOnNext(response -> log.debug("{} received messages from sqs", response.messages().size()))
                .flatMapMany(response -> Flux.fromIterable(response.messages()));
    }

    private ReceiveMessageRequest getReceiveMessageRequest() {
        return ReceiveMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .maxNumberOfMessages(properties.maxNumberOfMessages())
                .waitTimeSeconds(properties.waitTimeSeconds())
                .visibilityTimeout(properties.visibilityTimeoutSeconds())
                .build();
    }

    private DeleteMessageRequest getDeleteMessageRequest(String receiptHandle) {
        return DeleteMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .receiptHandle(receiptHandle)
                .build();
    }
}
