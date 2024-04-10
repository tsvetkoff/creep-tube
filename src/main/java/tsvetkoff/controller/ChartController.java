package tsvetkoff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tsvetkoff.creep.Program;
import tsvetkoff.creep.strategy.factory.StrategyTypeValues;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.GraphDto;
import tsvetkoff.domain.Params;

/**
 * @author SweetSupremum
 */
@RestController
@RequiredArgsConstructor
public class ChartController {

    private Graph run;

    /**
     * кидаем запрос сюда с фронта массива который хотим вернуть. По сути имя поля {@link Graph#имя любого готового поля}
     * см. поля и класс
     * @param strategyTypeValues - тип графика
     */
    @PostMapping("build")
    public ResponseEntity<Object> getSimpleGraph(@RequestBody Params params) throws NoSuchFieldException, IllegalAccessException {
        run = new Program(params).run();
        if(run != null && run.getR() != null) {
            return ResponseEntity.ok(GraphDto.builder().abscissa(run.getR()).graphDetails(params.getGraphType().getGraphCoordinates(run)).build());
        }
        return ResponseEntity.badRequest().body(run == null? "Run is null": run.getR() == null ? "Run get R null": "unknown");
    }

    /**
     * Кидаем запрос с фронта на этот метод
     * инициализируем параметры
     */
    @PostMapping("/run")
    public ResponseEntity<?> run(@RequestBody Params params){
        run = new Program(params).run();
        return ResponseEntity.ok(run);
    }

}
