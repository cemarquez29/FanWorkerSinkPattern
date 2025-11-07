package co.com.bancolombia.model.exceptions;

import co.com.bancolombia.model.exceptions.messages.ContractExceptionEnum;
import lombok.Getter;

@Getter
public class ContractException extends RuntimeException{
    private final ContractExceptionEnum contractExceptionEnum;
    private final String message;

    public ContractException(ContractExceptionEnum contractExceptionEnum, String message) {
        super(message);
        this.contractExceptionEnum = contractExceptionEnum;
        this.message = message;
    }
}