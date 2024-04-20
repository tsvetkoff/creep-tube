package tsvetkoff.domain;

import com.fasterxml.jackson.annotation.JsonValue;
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

public class GraphNameDto {
    @JsonValue
    private List<GraphDto> graphs;

}
