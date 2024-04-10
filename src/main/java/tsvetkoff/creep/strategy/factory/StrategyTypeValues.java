package tsvetkoff.creep.strategy.factory;

import org.springframework.util.ReflectionUtils;
import tsvetkoff.domain.Graph;

import java.util.Map;

/**
 * @author SweetSupremum
 */
public enum StrategyTypeValues {
    sigma_z0,
    sigma_z,
    sigma_theta0,
    sigma_theta,
    sigma_r0,
    sigma_r,
    tau0,
    tau,
    q,
    eps_z,
    theta;


    public Map<Object, Object> getGraphCoordinates(Graph graph) throws NoSuchFieldException, IllegalAccessException {
        return (Map<Object, Object>) graph.getClass().getField(this.name()).get(graph);
    }
}
