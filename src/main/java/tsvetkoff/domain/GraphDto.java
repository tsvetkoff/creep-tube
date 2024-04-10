package tsvetkoff.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author SweetSupremum
 */
@Data
@Builder
@AllArgsConstructor
public class GraphDto {
    //ключ либо дабл либо стринг, часы. Значение либо массив либо число. Остальное на фронте
    private Map<Object, Object> graphDetails;
    //это поле нужно на кейс, когда лежит время в ключе типа String
    private double[] abscissa;
}
