package co.com.bancolombia.sqs.listener.helper;

import co.com.bancolombia.model.numberentry.NumberEntry;
import co.com.bancolombia.sqs.listener.dto.FanEntryDTO;
import org.springframework.stereotype.Component;

@Component
public class FanEntryMapper {

    public NumberEntry dtoToModel(FanEntryDTO dto){
        return NumberEntry.builder()
                .timestamp(dto.getTimestamp())
                .value(dto.getValue())
                .batchId(dto.getBatchId())
                .seq(dto.getSeq())
                .build();
    }
}
