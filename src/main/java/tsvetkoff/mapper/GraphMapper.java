package tsvetkoff.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.GraphDto;
import tsvetkoff.utill.StepUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SweetSupremum
 */
@Component
@Slf4j(topic = "GRAPH_MAPPER")
public class GraphMapper {

    @Value("${graph.points.count:100}")
    /**
     * количество точек + 1 потому что берется нулевой элемент
     * (сначала проходим в цикле по нулевому потом уже добавляем это количество точек)
     * т.е. на 20 точек будет 20+1 (нулевая точка). Если же количество точек будет меньше чем pointCount*2, то выведем все
     * pointCount * 2
     */
    private int pointCount;


    public GraphDto mapLastCallsGraphsToDto(Graph graph) {
        return GraphDto
                .builder()
                .r(StepUtils.getArrayWithSlice(graph.getR(), pointCount))
                .t(StepUtils.getArrayWithSlice(graph.getTimes(), pointCount))
                .nameLabelOrdinatesMap(getGraphWithSlize(graph.getOnlyLastCallGraphs()))
                .build();
    }

    public GraphDto mapTempCallsGraphsToDto(Graph graph) {
        return GraphDto
                .builder()
                .r(StepUtils.getArrayWithSlice(graph.getR(), pointCount))
                .nameLabelOrdinatesMap(getGraphWithSlize(graph.getTwoDimensionalGraphWithName()))
                .build();
    }

    public GraphDto getAll(Graph graph) {
        GraphDto lastCallGraphs = mapLastCallsGraphsToDto(graph);
        GraphDto tempCallsGraphs = mapTempCallsGraphsToDto(graph);
        Map<String, Map<String, double[]>> allGraphs = new LinkedHashMap<>(tempCallsGraphs.getNameLabelOrdinatesMap());
        allGraphs.putAll(lastCallGraphs.getNameLabelOrdinatesMap());
        return GraphDto.builder()
                .r(StepUtils.getArrayWithSlice(lastCallGraphs.getR(), pointCount))
                .t(StepUtils.getArrayWithSlice(lastCallGraphs.getT(), pointCount))
                .nameLabelOrdinatesMap(allGraphs)
                .build();
    }

    public <T> Map<String, Map<String, double[]>> getGraphWithSlize(Map<String, Map<String, double[]>> graph) {
        Map<String, Map<String, double[]>> graphWithSlizes = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, double[]>> graphEntry : graph.entrySet()) {
            Map<String, double[]> slizeGraph = new LinkedHashMap<>();
            String key = graphEntry.getKey();
            Map<String, double[]> coordinatesMap = graphEntry.getValue();
            for (Map.Entry<String, double[]> coordinatesEntry : coordinatesMap.entrySet()) {
                double[] slizeCoordinates = StepUtils.getArrayWithSlice(coordinatesEntry.getValue(), pointCount);
                slizeGraph.put(coordinatesEntry.getKey(), slizeCoordinates);
            }
            graphWithSlizes.put(key, slizeGraph);
        }
        return graphWithSlizes;
    }
}
