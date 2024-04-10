package tsvetkoff.creep.strategy;

import tsvetkoff.domain.Graph;
import tsvetkoff.domain.Params;

/**
 * @author SweetSupremum
 */
public interface CreepBuilderStrategy {
    Graph processParams(Params params);

    default String getType() {
        return "type";
    }

}
