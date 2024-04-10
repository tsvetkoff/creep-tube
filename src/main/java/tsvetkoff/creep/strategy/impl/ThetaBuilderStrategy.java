package tsvetkoff.creep.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tsvetkoff.creep.strategy.CreepBuilderStrategy;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.Params;

/**
 * @author SweetSupremum
 */
@Service
@RequiredArgsConstructor
public class ThetaBuilderStrategy implements CreepBuilderStrategy {
    @Override
    public Graph processParams(Params params) {
        return null;
    }
}
