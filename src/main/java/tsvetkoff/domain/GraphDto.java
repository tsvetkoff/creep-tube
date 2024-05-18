package tsvetkoff.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author SweetSupremum
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GraphDto {

    @JsonProperty("graphDto")
    private Map<String, Map<String, double[]>> nameLabelOrdinatesMap;
    private double[] r;
    private List<Double> t;
}
