package tsvetkoff.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.GraphDto;
import tsvetkoff.domain.GraphNameDto;
import tsvetkoff.domain.GraphNameDtoCollectionWrapper;
import tsvetkoff.domain.Pair;
import tsvetkoff.domain.Params;
import tsvetkoff.utill.StepUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     */
    private int pointCount;

    public GraphNameDtoCollectionWrapper twoDimensionalMapToDto(Params params, Graph graph) {
        Set<Double> stressTimes = params.getStressTimes();
        double[] r = graph.getR();
        log.error(Thread.currentThread().toString());
        log.error(String.valueOf(r.length));
        List<Pair<String, GraphNameDto>> bodyGraphCollectionWrapper = new ArrayList<>();
        List<Pair<String, Map<String, double[]>>> twoDimensionalGraphWithName = graph.getTwoDimensionalGraphWithName();
        boolean isNotReady = twoDimensionalGraphWithName.stream().anyMatch(nameDataGraph -> {
            Map<String, double[]> timeCoordinates = nameDataGraph.getSecond();
            return timeCoordinates.keySet().isEmpty();
        });
        if (isNotReady) {
            return null;
        }
        twoDimensionalGraphWithName.forEach(nameDataGraph -> {
            String nameGraph = nameDataGraph.getFirst();
            log.error(nameGraph);
            Map<String, double[]> dataGraph = nameDataGraph.getSecond();
            List<GraphDto> coordinates = new ArrayList<>();
            for (double i = 0; i < r.length; i += StepUtils.getStep(r.length, pointCount)) {
                List<Pair<String, Double>> ordinates = new ArrayList<>();
                double temp = i;

                stressTimes.forEach(stressTime -> {
                    String stringValueStressTime = stressTime + " ч";
                    double[] graphValuesByTime = dataGraph.get(stringValueStressTime);
                    if (graphValuesByTime != null) {
                        ordinates.add(Pair.of(stringValueStressTime, graphValuesByTime[(int) temp]));
                    }
                });
                coordinates.add(
                        GraphDto.builder().abscissa(r[(int) temp]).ordinates(ordinates).build()
                );
            }
            bodyGraphCollectionWrapper.add(Pair.of(nameGraph, GraphNameDto.builder().graphs(coordinates).build()));
        });
        return GraphNameDtoCollectionWrapper.builder().graphs(bodyGraphCollectionWrapper).build();
    }

    public GraphNameDtoCollectionWrapper getAll(Params params, Graph graph) {
        List<Pair<String, GraphNameDto>> twoDimensional = twoDimensionalMapToDto(params, graph).getGraphs();
        List<Pair<String, GraphNameDto>> oneDimensional = oneDimensionalMapToDto(graph);
        List<Pair<String, GraphNameDto>> allGraphs = new ArrayList<>(oneDimensional);
        if (twoDimensional != null) {
            allGraphs.addAll(twoDimensional);
        }
        return GraphNameDtoCollectionWrapper.builder().graphs(allGraphs).build();
    }

    private List<Pair<String, GraphNameDto>> oneDimensionalMapToDto(Graph graph) {
        List<Pair<String, GraphNameDto>> bodyGraphCollectionWrapper = new ArrayList<>();
        List<Pair<String, Map<Double, Double>>> oneDimensionalGraphWithName = graph.getOneDimensionalGraphWithName();
        oneDimensionalGraphWithName.forEach(nameCoordinates -> {
            String name = nameCoordinates.getFirst();
            Map<Double, Double> coordinates = nameCoordinates.getSecond();
            List<GraphDto> graphs = new ArrayList<>();
            List<Double> ordinates = coordinates.values().stream().toList();
            double[] r = graph.getR();
            for (double i = 0; i < r.length; i += StepUtils.getStep(r.length, pointCount)) {
                graphs.add(GraphDto.builder().abscissa(r[(int) i]).ordinates(List.of(Pair.of("time", ordinates.get((int) i)))).build());

            }
            bodyGraphCollectionWrapper.add(Pair.of(name, GraphNameDto.builder().graphs(graphs).build()));
        });
        return bodyGraphCollectionWrapper;
    }
}
