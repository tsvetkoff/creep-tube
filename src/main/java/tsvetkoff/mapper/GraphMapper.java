package tsvetkoff.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.GraphDto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SweetSupremum
 */
@Component
@Slf4j(topic = "GRAPH_MAPPER")
public class GraphMapper {

    @Value("${graph.points.count}")
    /**
     * количество точек + 1 потому что берется нулевой элемент
     * (сначала проходим в цикле по нулевому потом уже добавляем это количество точек)
     * т.е. на 20 точек будет 20+1 (нулевая точка). Если же количество точек будет меньше чем pointCount*2, то выведем все
     * pointCount * 2
     */ //TODO:
    private int pointCount;


    public GraphDto mapLastCallsGraphsToDto(Graph graph) {
        return GraphDto.builder().r(graph.getR()).t(graph.getTimes()).nameLabelOrdinatesMap(graph.getOnlyLastCallGraphs()).build();
    }

    public GraphDto mapTempCallsGraphsToDto(Graph graph) {
        return GraphDto.builder().r(graph.getR()).nameLabelOrdinatesMap(graph.getTwoDimensionalGraphWithName()).build();
    }

    public GraphDto getAll(Graph graph) {
        GraphDto lastCallGraphs = mapLastCallsGraphsToDto(graph);
        GraphDto tempCallsGraphs = mapTempCallsGraphsToDto(graph);
        Map<String, Map<String, double[]>> allGraphs = new HashMap<>(tempCallsGraphs.getNameLabelOrdinatesMap());
        allGraphs.putAll(lastCallGraphs.getNameLabelOrdinatesMap());
        return GraphDto.builder().r(lastCallGraphs.getR()).t(lastCallGraphs.getT()).nameLabelOrdinatesMap(allGraphs).build();
    }
}
