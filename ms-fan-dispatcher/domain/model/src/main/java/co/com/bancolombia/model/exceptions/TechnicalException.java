package co.com.bancolombia.model.exceptions;

import co.com.bancolombia.model.exceptions.messages.TechnicalExceptionEnum;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final TechnicalExceptionEnum technicalExceptionEnum;

    public TechnicalException(TechnicalExceptionEnum businessErrorMessage) {
        super(businessErrorMessage.getMessage());
        this.technicalExceptionEnum = businessErrorMessage;
    }

}
