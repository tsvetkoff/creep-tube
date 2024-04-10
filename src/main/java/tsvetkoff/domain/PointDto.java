package tsvetkoff.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author SweetSupremum
 * {
 * abscissa: 1,
 * ordinate:[
 * for first graph: 1,
 * for second graph: 2,...
 * <p>
 * ]
 * }, ...
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDto {
    private Double abscissa;
    private List<Double> ordinates;
}
