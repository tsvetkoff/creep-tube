package tsvetkoff.domain;

import lombok.Data;
import tsvetkoff.domain.enums.GraphNames;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class Graph {
    public final double dt;
    public final Map<String, double[]> sigma_z0 = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_z = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_theta0 = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_theta = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_r0 = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_r = new LinkedHashMap<>();
    public final Map<String, double[]> tau0 = new LinkedHashMap<>();
    public final Map<String, double[]> tau = new LinkedHashMap<>();
    public final Map<String, double[]> q = new LinkedHashMap<>();
    public final Map<Double, Double> eps_z = new LinkedHashMap<>();
    public final Map<Double, Double> theta = new LinkedHashMap<>();

    private final double[] r;

    public Graph(double dt, double[] r) {
        this.dt = dt;
        this.r = r;
    }

    /**
     * добавляем копию из - за {@link java.util.ConcurrentModificationException}
     */
    public List<Pair<String, Map<String, double[]>>> getTwoDimensionalGraphWithName() {
        return List.of(
                Pair.of(GraphNames.SIGMA_Z0.getName(), new LinkedHashMap<>(sigma_z0)),
                Pair.of(GraphNames.SIGMA_Z.getName(), new LinkedHashMap<>(sigma_z)),
                Pair.of(GraphNames.SIGMA_THETA0.getName(), new LinkedHashMap<>(sigma_theta0)),
                Pair.of(GraphNames.SIGMA_THETA.getName(), new LinkedHashMap<>(sigma_theta)),
                Pair.of(GraphNames.SIGMA_R.getName(), new LinkedHashMap<>(sigma_r)),
                Pair.of(GraphNames.SIGMA_R0.getName(), new LinkedHashMap<>(sigma_r0)),
                Pair.of(GraphNames.TAU0.getName(), new LinkedHashMap<>(tau0)),
                Pair.of(GraphNames.TAU.getName(), new LinkedHashMap<>(tau))
        );
    }

    /**
     * Звонить в метод, когда до конца пересчитались коллекции
     */
    public List<Pair<String, Map<Double, Double>>> getOneDimensionalGraphWithName() {
        return List.of(
                Pair.of(GraphNames.EPS_Z.getName(), eps_z),
                Pair.of(GraphNames.THETA.getName(), theta)
        );
    }

}
