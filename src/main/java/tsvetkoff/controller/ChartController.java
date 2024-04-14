package tsvetkoff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tsvetkoff.creep.Program;
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

    private Program run;
    private Future<Graph> graphFuture;

    private final GraphMapper graphMapper;


    /**
     * кидаем запрос сюда с фронта массива который хотим вернуть. По сути имя поля {@link Graph#имя любого готового поля}
     * см. поля и класс
     *
     * @param strategyTypeValues - тип графика
     */
    @PostMapping("build")
    public ResponseEntity<Object> getSimpleGraph(@RequestBody Params params) throws ExecutionException, InterruptedException {
        if (graphFuture.isDone()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(graphMapper.twoDimensionalMapToDto(params, graphFuture.get()));
        }
        if (run != null && run.getGraph() != null && run.getR() != null) {
            return ResponseEntity.ok(graphMapper.twoDimensionalMapToDto(params, run.getGraph()));
        }
        if (run != null && run.getGraph() == null) {
            return ResponseEntity.badRequest().body("graph is not initialized");
        }
        return ResponseEntity.badRequest().body(run == null ? "Run is null" : run.getR() == null ? "Run get R null" : "unknown");
    }

    /**
     * Кидаем запрос с фронта на этот метод
     * инициализируем параметры
     */
    @PostMapping("/run")
    public ResponseEntity<?> run(@RequestBody Params params) {
        run = new Program(params);
        graphFuture = run.asyncRun();
        return ResponseEntity.ok(run);
    }

}
