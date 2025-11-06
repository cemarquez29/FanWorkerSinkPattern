package co.com.bancolombia.sqs.listener.helper;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class MessageValidator {
    private final Validator validator;

    public MessageValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validateData(T data) {
        return Mono.fromCallable(() -> validator.validate(data))
                .flatMap(violations -> {
                    if (violations.isEmpty()) return Mono.just(data);
                    log.error("Validación de campos fallida: {}", violations);
                    return Mono.error(new ConstraintViolationException(violations));
                });
    }

}