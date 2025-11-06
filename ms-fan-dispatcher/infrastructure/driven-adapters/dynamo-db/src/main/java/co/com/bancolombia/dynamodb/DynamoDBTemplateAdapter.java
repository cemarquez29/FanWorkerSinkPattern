package co.com.bancolombia.dynamodb;

import co.com.bancolombia.dynamodb.config.TableProperties;
import co.com.bancolombia.model.Result;
import co.com.bancolombia.model.ResultModel;
import co.com.bancolombia.model.gateway.DynamoRepositoryGateway;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;

import java.util.List;


@Repository
public class DynamoDBTemplateAdapter implements DynamoRepositoryGateway {

    private final TableProperties tableProperties;

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;

    private final DynamoDbAsyncTable<NumberEntity> dbAsyncTable;

    public DynamoDBTemplateAdapter(final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
                                   TableProperties tableProperties) {
        this.tableProperties = tableProperties;
        this.enhancedAsyncClient = dynamoDbEnhancedAsyncClient;
        this.dbAsyncTable = dynamoDbEnhancedAsyncClient.table(tableProperties.getTableName(),
                TableSchema.fromBean(NumberEntity.class));

    }


    @Override
    public Mono<List<Result>> getResults(ResultModel resultModel) {
        String batchId = resultModel.getBatchId();

        var key = software.amazon.awssdk.enhanced.dynamodb.Key.builder()
                .partitionValue(batchId)
                .build();

        return Mono.fromSupplier(() -> dbAsyncTable.query(r -> r
                        .queryConditional(
                                software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.keyEqualTo(key))
                ))
                .flatMapMany(Flux::from)
                .flatMap(page -> Flux.fromIterable(page.items()))
                .map(entity -> new Result(entity.getSquaredValue()))
                .sort(java.util.Comparator.comparing(Result::getValue))
                .collectList();
    }

}
