package tsvetkoff.domain;

import com.fasterxml.jackson.annotation.JsonValue;
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
public class GraphNameDto {
    @JsonValue
    private Map<String, List<GraphDto>> graphs;
}
