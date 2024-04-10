package tsvetkoff.creep.strategy.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tsvetkoff.creep.strategy.CreepBuilderStrategy;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SweetSupremum
 */
@Component
@RequiredArgsConstructor
//TODO: annotation
public class CreepStrategyFactory {
    private final List<CreepBuilderStrategy> strategies;

    // note: this is thread - safe
    private Map<String, CreepBuilderStrategy> typeStrategyMap = new HashMap<>();

    @PostConstruct
    public void initFactory() {
        strategies.forEach(strategy -> typeStrategyMap.put(strategy.getType(), strategy));
    }

    public CreepBuilderStrategy getStrategy(String type) {
        return typeStrategyMap.get(type);
    }
}
