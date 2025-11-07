package co.com.bancolombia.sqs.listener;

import co.com.bancolombia.model.numberentry.NumberEntry;
import co.com.bancolombia.sqs.listener.dto.FanEntryDTO;
import co.com.bancolombia.sqs.listener.helper.FanEntryMapper;
import co.com.bancolombia.usecase.fan.FanUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Log4j2
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final FanUseCase fanUseCase;
    private final ObjectMapper mapper;
    private final FanEntryMapper fanEntryMapper;

    @Override
    public Mono<Void> apply(Message message) {

        log.info("Procesando mensaje: {}", message.body());

        NumberEntry model = null;
        try {
            model = fanEntryMapper.dtoToModel(parse(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return fanUseCase.elevarCuadrado(model)
                .doOnSuccess(v -> log.info("Mensaje procesado correctamente: {}", message.messageId()))
                .doOnError(e -> log.error("Error procesando mensaje {}: {}", message.messageId(), e.getMessage()));
    }


    private FanEntryDTO parse(Message message) throws JsonProcessingException {
        return mapper.readValue(message.body(), FanEntryDTO.class);
    }
}
