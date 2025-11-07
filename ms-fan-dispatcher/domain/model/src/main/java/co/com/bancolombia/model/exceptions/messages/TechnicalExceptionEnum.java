package co.com.bancolombia.model.exceptions.messages;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionEnum {
    REST_BAD_GATEWAY(
            restBadGatewayCode(),
            "El mensaje de respuesta recibido del servidor es invalido",
            502,
            restBadGatewayTitle()),
    REST_BAD_GATEWAY_500(
            restBadGatewayCode(),
            "El mensaje obtenido del servidor no es valido.",
            502,
            restBadGatewayTitle()),
    UNEXPECTED_EXCEPTION(
            "SI500",
            "Ha ocurrido un error interno controlado en el servidor, inténtelo mas tarde",
            500,
            "Internal Server Error"),
    RESPONSE_TIME_OUT(
            "SP504",
            "El proveedor no respondió en tiempo esperado",
            504,
            "Gateway Timeout"),
    METHOD_NOT_ALLOWED(
            "SI405",
            "El metodo de solicitud no es permitido",
            405,
            "Method Not Allowed"),
    URI_NOT_FOUND(
            notFoundCode(),
            "Ha ocurrido un error, no se encuentra el recurso solicitado.",
            404,
            notFoundTitle()),
    SPR_CONSUMER_EXCEPTION(
            notFoundCode(),
            "No es posible realizar la homologacion del codigo de respuesta.",
            404,
            notFoundTitle()),
    POLICY_MANAGER_EXCEPTION(
            "SI503",
            "Ha ocurrido un error validando la autorizacion del consumidor.",
            503,
            "Service Unavailable");
    private static final String NOT_FOUND_CODE = "SI404";
    private static final String NOT_FOUND_TITLE = "Not Found";
    private static final String REST_BAD_GATEWAY_TITLE = "Bad Gateway";
    private static final String REST_BAD_GATEWAY_CODE = "SI502";

    private final String code;
    private final String message;
    private final Integer status;
    private final String title;
    private static String notFoundCode() {
        return NOT_FOUND_CODE;
    }

    private static String notFoundTitle() {
        return NOT_FOUND_TITLE;
    }

    private static String restBadGatewayCode() {
        return REST_BAD_GATEWAY_CODE;
    }

    private static String restBadGatewayTitle() {
        return REST_BAD_GATEWAY_TITLE;
    }
}