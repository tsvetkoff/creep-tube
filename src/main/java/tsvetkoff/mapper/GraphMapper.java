package tsvetkoff.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.GraphDto;
import tsvetkoff.domain.GraphNameDto;
import tsvetkoff.domain.GraphNameDtoCollectionWrapper;
import tsvetkoff.domain.Pair;
import tsvetkoff.domain.Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author SweetSupremum
 */
@Component
@Slf4j(topic = "GRAPH_MAPPER")
public class GraphMapper {

    public GraphNameDtoCollectionWrapper twoDimensionalMapToDto(Params params, Graph graph) {
        TreeSet<Double> stressTimes = params.getStressTimes();
        double[] r = graph.getR();
        log.error(Thread.currentThread().toString());
        List<Pair<String, GraphNameDto>> bodyGraphCollectionWrapper = new ArrayList<>();
        AtomicBoolean isNotReady = new AtomicBoolean(true);
        List<Pair<String, Map<String, double[]>>> twoDimensionalGraphWithName = graph.getTwoDimensionalGraphWithName();
        twoDimensionalGraphWithName.forEach(nameDataGraph -> {
            Map<String, double[]> timeCoordinates = nameDataGraph.getSecond();
            if (timeCoordinates.keySet().isEmpty()) {
                isNotReady.set(false);
            }
        });
        if (isNotReady.get()) {
            return null;
        }
        twoDimensionalGraphWithName.forEach(nameDataGraph -> {
            String nameGraph = nameDataGraph.getFirst();
            log.error(nameGraph);
            Map<String, double[]> dataGraph = nameDataGraph.getSecond();
            List<GraphDto> coordinates = new ArrayList<>();
            for (int i = 0; i < r.length; i++) {
                List<Pair<String, Double>> ordinates = new ArrayList<>();
                int temp = i;

                stressTimes.forEach(stressTime -> {
                    String stringValueStressTime = stressTime + " ч";
                    double[] graphValuesByTime = dataGraph.get(stringValueStressTime);
                    if (graphValuesByTime != null) {
                        ordinates.add(Pair.of(stringValueStressTime, graphValuesByTime[temp]));
                    }
                });
                coordinates.add(
                        GraphDto.builder().abscissa(r[temp]).ordinates(ordinates).build()
                );
            }
            bodyGraphCollectionWrapper.add(Pair.of(nameGraph, GraphNameDto.builder().graphs(coordinates).build()));
        });
        return GraphNameDtoCollectionWrapper.builder().graphs(bodyGraphCollectionWrapper).build();
    }

    private GraphNameDtoCollectionWrapper oneDimensionalMapToDto(Params params, Graph graph) {
        return null;
    }
}
