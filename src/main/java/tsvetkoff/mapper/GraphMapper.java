package tsvetkoff.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.GraphDto;
import tsvetkoff.domain.GraphNameDto;
import tsvetkoff.domain.Pair;
import tsvetkoff.domain.Params;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author SweetSupremum
 */
@Component
@Slf4j(topic = "GRAPH_MAPPER")
public class GraphMapper {

    public List<GraphNameDto> twoDimensionalMapToDto(Params params, Graph graph) {
        List<GraphNameDto> nameGraphs = new ArrayList<>();
        TreeSet<Double> stressTimes = params.getStressTimes();
        double[] r = graph.getR();
        log.error(Thread.currentThread().toString());
        graph.getTwoDimensionalGraphWithName().forEach(nameDataGraph -> {
            GraphNameDto graphNameDto = new GraphNameDto();
            String nameGraph = nameDataGraph.getFirst();
            Map<String, List<GraphDto>> graphs = new LinkedHashMap<>();
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
            graphs.put(nameGraph, coordinates);
            graphNameDto.setGraphs(graphs);
            nameGraphs.add(graphNameDto);
        });
        return nameGraphs;
    }

    private List<GraphNameDto> oneDimensionalMapToDto(Params params, Graph graph) {
        return null;
    }
}
