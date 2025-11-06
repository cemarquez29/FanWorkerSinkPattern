package co.com.bancolombia.sqs.listener.helper;

import co.com.bancolombia.model.numberentry.WorkerEntry;
import co.com.bancolombia.sqs.listener.dto.WorkerEntryDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkerEntryMapper {

    public WorkerEntry dtoToModel(WorkerEntryDTO dto){
        return WorkerEntry.builder()
                .timestamp(dto.getTimestamp())
                .value(dto.getValue())
                .batchId(dto.getBatchId())
                .seq(dto.getSeq())
                .build();
    }
}
