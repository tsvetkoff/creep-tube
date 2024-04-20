package tsvetkoff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tsvetkoff.creep.CalculationService;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.Params;
import tsvetkoff.mapper.GraphMapper;

import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toCollection;

/**
 * @author SweetSupremum
 */
@RestController
@RequiredArgsConstructor
public class ChartController {

    private final CalculationService calculationService;
    private Future<Graph> graphFuture;

    private final GraphMapper graphMapper;


    /**
     * Строит графики по таймаут запросу.
     */
    @PostMapping("/build")
    public ResponseEntity<Object> getSimpleGraph(@RequestBody Params params) throws ExecutionException, InterruptedException {
        if (graphFuture.isDone()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(graphMapper.getAll(params, graphFuture.get()));
        }
        if (calculationService.getGraph() != null) {
            return ResponseEntity.ok(graphMapper.twoDimensionalMapToDtoWithStressTimesCheck(params, calculationService.getGraph()));
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Кидаем запрос с фронта на этот метод
     * инициализируем параметры
     */
    @PostMapping("/run")
    public ResponseEntity<?> run(@RequestBody Params params) {
        graphFuture = calculationService.asyncCalculation(params);
        return ResponseEntity.accepted().body(
                params.getStressTimes().stream().map(Object::toString).collect(toCollection(LinkedHashSet::new))
        );
    }

}
