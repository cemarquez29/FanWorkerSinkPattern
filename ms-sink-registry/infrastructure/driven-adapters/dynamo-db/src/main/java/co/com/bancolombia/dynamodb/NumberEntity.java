package co.com.bancolombia.dynamodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NumberEntity {

    private String batchId;
    private int seq;
    private int squaredValue;

    @DynamoDbPartitionKey
    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    @DynamoDbSortKey
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @DynamoDbAttribute("squaredValue")
    public int getSquaredValue() {
        return squaredValue;
    }

    public void setSquaredValue(int squaredValue) {
        this.squaredValue = squaredValue;
    }
}
