package co.com.bancolombia.model.exceptions.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractExceptionEnum {

    BAD_REQUEST_EMPTY_FIELD(
            badRequestCode(),
            "Parametro ${name} es obligatorio y no puede ser vacio",
            400,
            badRequestTitle()),
    BAD_REQUEST_NUMBER_POSITIVE(
            badRequestCode(),
            "Parametro ${name} no puede ser menor a 1",
            400,
            badRequestTitle()),
    BAD_REQUEST_EMPTY_HEADER(
            badRequestCode(),
            "Encabezado ${name} es obligatorio y no puede ser vacio",
            400,
            badRequestTitle()),
    BAD_REQUEST_INCORRECT_PATTERN(
            badRequestCode(),
            "El valor del parametro ${name} no cumple con la expresion regular",
            400,
            badRequestTitle()),
    BAD_REQUEST_VALUE_NOT_ALLOWED(
            badRequestCode(),
            "El valor del parametro ${name} no hace parte de los valores validos",
            400,
            badRequestTitle()),
    BAD_REQUEST_LENGTH_NOT_VALID(
            badRequestCode(),
            "Longitud del parametro ${name} es diferente a la longitud esperada",
            400,
            badRequestTitle()),
    UNEXPECTED_REQUEST_EXCEPTION("SI500",
            "Ha ocurrido un error validando la peticion",
            500,
            "Internal Server Error"),
    BAD_REQUEST_BODY_NULL(
            badRequestCode(),
            "El cuerpo de peticion no puede ser vacio",
            400,
            badRequestTitle()),
    BAD_REQUEST_MANDATORY_OBJECT(
            badRequestCode(),
            "El objeto ${name} es obligatorio",
            400,
            badRequestTitle());

    private static final String BAD_REQUEST_CODE = "SI400";
    private static final String BAD_REQUEST_TITLE = "Bad Request";

    private final String code;
    private final String message;
    private final Integer status;
    private final String title;

    private static String badRequestCode() {
        return BAD_REQUEST_CODE;
    }

    private static String badRequestTitle() {
        return BAD_REQUEST_TITLE;
    }

}