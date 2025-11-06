package co.com.bancolombia.model.gateway;

import co.com.bancolombia.model.Result;
import co.com.bancolombia.model.ResultModel;
import co.com.bancolombia.model.numberentry.FanEntry;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DynamoRepositoryGateway {
    public Mono<List<Result>> getResults(ResultModel resultModel);
}
