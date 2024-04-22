package tsvetkoff.domain;

import lombok.Data;
import tsvetkoff.domain.enums.GraphNames;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tsvetkoff.domain.enums.OmegaRadialName.OMEGA_HIGH_R1;
import static tsvetkoff.domain.enums.OmegaRadialName.OMEGA_LOW_R1;
import static tsvetkoff.domain.enums.OneDimensionalGraphs.EPS_Z;
import static tsvetkoff.domain.enums.OneDimensionalGraphs.THETA;

@Data
public class Graph {
    public final double dt;
    /**
     * Везде имя абсциссы и массив координат
     */
    public final Map<String, double[]> sigma_z0 = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_z = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_theta0 = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_theta = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_r0 = new LinkedHashMap<>();
    public final Map<String, double[]> sigma_r = new LinkedHashMap<>();
    public final Map<String, double[]> tau0 = new LinkedHashMap<>();
    public final Map<String, double[]> tau = new LinkedHashMap<>();
    public final Map<String, double[]> q = new LinkedHashMap<>();
    private Map<String, List<Double>> lowOmegasGraphDto = new LinkedHashMap<>();
    private Map<String, List<Double>> highOmegasGraphDto = new LinkedHashMap<>();

    public final Map<String, List<Double>> eps_z = new LinkedHashMap<>();
    public final Map<String, List<Double>> theta = new LinkedHashMap<>();

    private final double[] r;
    private final List<Double> times;

    public Graph(double dt, double[] r, List<Double> times) {
        this.dt = dt;
        this.r = r;
        this.times = times;
    }

    /**
     * добавляем копию из - за {@link java.util.ConcurrentModificationException}
     */
    public Map<String, Map<String, double[]>> getTwoDimensionalGraphWithName() {
        return
                Map.of(
                        GraphNames.SIGMA_Z0.getName(), new LinkedHashMap<>(sigma_z0),
                        GraphNames.SIGMA_Z.getName(), new LinkedHashMap<>(sigma_z),
                        GraphNames.SIGMA_THETA0.getName(), new LinkedHashMap<>(sigma_theta0),
                        GraphNames.SIGMA_THETA.getName(), new LinkedHashMap<>(sigma_theta),
                        GraphNames.SIGMA_R.getName(), new LinkedHashMap<>(sigma_r),
                        GraphNames.SIGMA_R0.getName(), new LinkedHashMap<>(sigma_r0),
                        GraphNames.TAU0.getName(), new LinkedHashMap<>(tau0),
                        GraphNames.TAU.getName(), new LinkedHashMap<>(tau)
                );
    }

    /**
     * Графики строятся по одному дожидаемся фьючу до конца
     */
    public Map<String, Map<String, double[]>> getOnlyLastCallGraphs() {

        return Map.of(
                EPS_Z.getName(), eps_z.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray())),
                THETA.getName(), theta.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray())),
                OMEGA_LOW_R1.getName(), lowOmegasGraphDto.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray())),
                OMEGA_HIGH_R1.getName(), highOmegasGraphDto.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray()))
        );
    }

}
