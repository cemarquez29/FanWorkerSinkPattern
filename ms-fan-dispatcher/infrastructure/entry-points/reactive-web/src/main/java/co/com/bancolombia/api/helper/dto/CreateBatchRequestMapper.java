package co.com.bancolombia.api.helper.dto;

import co.com.bancolombia.api.dto.request.CreateBatchRequestDTO;
import co.com.bancolombia.model.CreateBatchModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateBatchRequestMapper {

    public CreateBatchModel dtoToModel(CreateBatchRequestDTO dto) {
        return CreateBatchModel.builder()
                .size(dto.getSize())
                .min(dto.getMin())
                .max(dto.getMax())
                .build();
    }
}
