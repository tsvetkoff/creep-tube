package tsvetkoff.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author SweetSupremum
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GraphNameDto {
    @JsonValue
    private List<GraphDto> graphs;

    public static void main(String[] args) throws JsonProcessingException {
        GraphNameDtoCollectionWrapper graphNameDto = new GraphNameDtoCollectionWrapper();
        List<GraphDto> graphs = new ArrayList<>();

        graphs.add(GraphDto.builder().abscissa(1.).ordinates(List.of(Pair.of("t1", 1.0), Pair.of("t2", 1.1))).build());
        graphs.add(GraphDto.builder().abscissa(2.).ordinates(List.of(Pair.of("t1", 1.1), Pair.of("t2", 1.2))).build());
        GraphNameDto graphNameDtos = new GraphNameDto(graphs);
        graphNameDto.setGraphs(List.of(Pair.of("sigma", graphNameDtos),Pair.of("sigma2", graphNameDtos)));
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(graphNameDto));

    }
}
