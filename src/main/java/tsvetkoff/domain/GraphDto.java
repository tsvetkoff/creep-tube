package tsvetkoff.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author SweetSupremum
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = GraphDtoSerializer.class)
public class GraphDto {

    private Double abscissa;
    private List<Pair<String, Double>> ordinates;
}
