package tsvetkoff.domain;

import com.fasterxml.jackson.annotation.JsonRootName;
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
@JsonSerialize(using = GraphsSerializer.class)
@JsonRootName("graphs")
public class GraphNameDtoCollectionWrapper {
    private List<Pair<String, GraphNameDto>> graphs;
}
