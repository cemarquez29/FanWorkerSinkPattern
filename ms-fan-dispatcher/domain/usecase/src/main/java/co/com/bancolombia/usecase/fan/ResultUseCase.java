package co.com.bancolombia.usecase.fan;

import co.com.bancolombia.model.Result;
import co.com.bancolombia.model.ResultModel;
import co.com.bancolombia.model.gateway.DynamoRepositoryGateway;
import reactor.core.publisher.Mono;

import java.util.List;

public class ResultUseCase {
    private final DynamoRepositoryGateway dynamoRepositoryGateway;

    public ResultUseCase(DynamoRepositoryGateway dynamoRepositoryGateway) {
        this.dynamoRepositoryGateway = dynamoRepositoryGateway;
    }

    public Mono<List<Result>> getResults(ResultModel resultModel) {
        return dynamoRepositoryGateway.getResults(resultModel);
    }
}
