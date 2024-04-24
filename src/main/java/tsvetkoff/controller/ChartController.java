package tsvetkoff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tsvetkoff.creep.CalculationService;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.Params;
import tsvetkoff.mapper.GraphMapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author SweetSupremum
 */
@RestController
@RequiredArgsConstructor
public class ChartController {

    private final CalculationService calculationService;
    private Future<Graph> graphFuture;
    private final GraphMapper graphMapper;


    @GetMapping("/stop")
    public ResponseEntity<Object> stop() {
        if (graphFuture != null && !graphFuture.isCancelled()) {
            graphFuture.cancel(true);
            graphFuture = null;
        }
        return ResponseEntity.accepted().build();
    }

    /**
     * Строит графики по таймаут запросу.
     */
    @GetMapping("/build")
    public ResponseEntity<Object> getSimpleGraph() throws ExecutionException, InterruptedException {
        if (graphFuture != null && graphFuture.isDone()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(graphMapper.getAll(graphFuture.get()));
        }
        if (calculationService.getGraph() != null) {
            return ResponseEntity.ok(graphMapper.mapTempCallsGraphsToDto(calculationService.getGraph()));
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Кидаем запрос с фронта на этот метод
     * инициализируем параметры
     */
    @PostMapping("/run")
    public synchronized ResponseEntity<?> run(@RequestBody Params params) {
        if (graphFuture != null && !graphFuture.isCancelled()) {
            graphFuture.cancel(true);
            graphFuture = null;
        } else {
            graphFuture = calculationService.asyncCalculation(params);
        }
        return ResponseEntity.accepted().build();
    }

}
