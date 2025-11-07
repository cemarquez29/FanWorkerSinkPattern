package co.com.bancolombia.api.helper.dto;

import co.com.bancolombia.api.dto.response.ResultResponseDTO;
import co.com.bancolombia.model.Result;
import co.com.bancolombia.model.ResultModel;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ResultResponseMapper {

    public ResultResponseDTO modelToDto(List<Result> resultModels){
        return ResultResponseDTO.builder()
                .results(
                        resultModels.stream()
                                .map(result -> String.valueOf(result.getValue()))
                                .toList()
                )
                .build();
    }
}
