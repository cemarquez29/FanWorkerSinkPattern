package co.com.bancolombia.dynamodb;

import co.com.bancolombia.dynamodb.config.TableProperties;
import co.com.bancolombia.dynamodb.helper.TemplateAdapterOperations;
import co.com.bancolombia.model.gateway.DynamoRepositoryGateway;
import co.com.bancolombia.model.numberentry.WorkerEntry;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.time.Instant;
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
    public Mono<Void> save(WorkerEntry newEntry) {
        NumberEntity entity = NumberEntity.builder()
                .batchId(newEntry.getBatchId())
                .seq(newEntry.getSeq())
                .squaredValue(newEntry.getValue())
                .build();

        var condition = Expression.builder()
                .expression("attribute_not_exists(#pk) AND attribute_not_exists(#sk)")
                .putExpressionName("#pk", "batchId")
                .putExpressionName("#sk", "seq")
                .build();

        var request = PutItemEnhancedRequest
                .builder(NumberEntity.class)
                .item(entity)
                .conditionExpression(condition)
                .build();

        return Mono.fromFuture(() -> dbAsyncTable.putItem(request))
                .onErrorResume(ConditionalCheckFailedException.class, e -> Mono.empty())
                .then();
    }

}
