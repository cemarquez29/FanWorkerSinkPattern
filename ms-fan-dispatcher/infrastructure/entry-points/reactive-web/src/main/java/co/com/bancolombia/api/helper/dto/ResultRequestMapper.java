package co.com.bancolombia.api.helper.dto;

import co.com.bancolombia.api.dto.request.ResultRequestDTO;
import co.com.bancolombia.model.ResultModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResultRequestMapper {

    public ResultModel dtoToModel(ResultRequestDTO dto) {
        return ResultModel.builder()
                .batchId(dto.getBatchId())
                .build();
    }
}
