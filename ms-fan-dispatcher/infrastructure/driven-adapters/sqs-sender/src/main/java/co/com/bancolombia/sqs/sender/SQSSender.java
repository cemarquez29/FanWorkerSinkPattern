package co.com.bancolombia.sqs.sender;

import co.com.bancolombia.model.gateway.SqsSenderGateway;
import co.com.bancolombia.model.numberentry.FanEntry;
import co.com.bancolombia.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
public class SQSSender implements SqsSenderGateway {

    private final SQSSenderProperties properties;

    private final SqsAsyncClient client;
    private final ObjectMapper mapper;

    public SQSSender(SQSSenderProperties properties,
                     @Qualifier("sqsSenderConfig") SqsAsyncClient client,
                     ObjectMapper mapper) {
        this.properties = properties;
        this.client = client;
        this.mapper = mapper;
    }

    public Mono<String> send(FanEntry newEntry) {
        return Mono.fromCallable(() -> buildRequest(newEntry))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(FanEntry message) throws JsonProcessingException {
        String json = mapper.writeValueAsString(message);
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(json)
                .build();
    }
}
