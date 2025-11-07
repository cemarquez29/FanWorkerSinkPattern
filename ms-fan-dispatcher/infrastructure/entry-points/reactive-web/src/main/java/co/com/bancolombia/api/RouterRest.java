package co.com.bancolombia.api;

import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.exceptions.messages.TechnicalExceptionEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    private static final String BASE_PATH = "/api/fan";

    private static final String CREATE_PATH = BASE_PATH + "/create-batch";
    private static final String RESULT_PATH = BASE_PATH + "/result";

    private static final Set<String> VALID_PATHS = Set.of(CREATE_PATH, RESULT_PATH);

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(CREATE_PATH), handler::listenPOSTCreateUseCase)
                .andRoute(GET(RESULT_PATH), handler::listenPOSTResultPocketUseCase)
                .andRoute(RequestPredicates.all(), this::determineRouteResponse);
    }

    private Mono<ServerResponse> determineRouteResponse(ServerRequest request) {
        return Mono.error(VALID_PATHS.contains(request.path()) ?
                new TechnicalException(TechnicalExceptionEnum.METHOD_NOT_ALLOWED) :
                new TechnicalException(TechnicalExceptionEnum.URI_NOT_FOUND));
    }
}
