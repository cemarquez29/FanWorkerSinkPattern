package co.com.bancolombia.api.helper;

import co.com.bancolombia.model.exceptions.ContractException;
import co.com.bancolombia.model.exceptions.messages.ContractExceptionEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class ValidationRequest {
    private static final String REGEX_MESSAGE_ERROR = "%";
    private final Validator validator;

    public ValidationRequest(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validateData(T data) {
        return Mono.just(data)
                .map(validator::validate)
                .map(this::evaluateValidations)
                .onErrorMap(ConstraintViolationException.class, this::getContractException)
                .map(Set::isEmpty)
                .thenReturn(data);
    }

    public ContractException getContractException(ConstraintViolationException exception) {
        String message = exception.getMessage();
        if (message != null && message.contains(REGEX_MESSAGE_ERROR)) {
            String[] parts = message.split(REGEX_MESSAGE_ERROR);
            if (parts.length > 1) {
                String code = parts[1];
                var contractExceptionEnum = ContractExceptionEnum.valueOf(code);
                String field = getFirstInvalidField(exception);
                message = contractExceptionEnum.getMessage().replace("${name}", field);
                return new ContractException(contractExceptionEnum, message);
            }
        }

        return new ContractException(ContractExceptionEnum.UNEXPECTED_REQUEST_EXCEPTION,
                ContractExceptionEnum.UNEXPECTED_REQUEST_EXCEPTION.getMessage());
    }

    public <T> Set<ConstraintViolation<T>> evaluateValidations(Set<ConstraintViolation<T>> constraint) {
        if (!constraint.isEmpty()) {
            throw new ConstraintViolationException(constraint);
        } else {
            return constraint;
        }
    }

    public String getFirstInvalidField(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getPropertyPath().toString())
                .orElse(null);
    }
}