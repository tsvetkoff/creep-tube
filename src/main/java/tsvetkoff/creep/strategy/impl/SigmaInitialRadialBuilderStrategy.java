package tsvetkoff.creep.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tsvetkoff.creep.strategy.CreepBuilderStrategy;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.Params;

import tsvetkoff.mapper.GraphMapper;

/**
 * @author SweetSupremum
 */
@Service
@RequiredArgsConstructor
public class SigmaInitialRadialBuilderStrategy implements CreepBuilderStrategy {
    private final GraphMapper graphMapper;


    @Override
    public Graph processParams(Params params) {
       return null;
    }


}



