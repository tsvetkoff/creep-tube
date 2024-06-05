package tsvetkoff.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tsvetkoff.domain.enums.GraphName;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static tsvetkoff.domain.enums.OmegaRadialName.OMEGA_HIGH_R1;
import static tsvetkoff.domain.enums.OmegaRadialName.OMEGA_LOW_R1;
import static tsvetkoff.domain.enums.OneDimensionalGraphs.EPS_Z;
import static tsvetkoff.domain.enums.OneDimensionalGraphs.THETA;

@Data
@Slf4j
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
                        GraphName.SIGMA_Z0.getName(), new LinkedHashMap<>(sigma_z0),
                        GraphName.SIGMA_Z.getName(), new LinkedHashMap<>(sigma_z),
                        GraphName.SIGMA_THETA0.getName(), new LinkedHashMap<>(sigma_theta0),
                        GraphName.SIGMA_THETA.getName(), new LinkedHashMap<>(sigma_theta),
                        GraphName.SIGMA_R.getName(), new LinkedHashMap<>(sigma_r),
                        GraphName.SIGMA_R0.getName(), new LinkedHashMap<>(sigma_r0),
                        GraphName.TAU0.getName(), new LinkedHashMap<>(tau0),
                        GraphName.TAU.getName(), new LinkedHashMap<>(tau)
                );
    }

    /**
     * Графики строятся по одному дожидаемся фьючу до конца
     */
    public Map<String, Map<String, double[]>> getOnlyLastCallGraphs() {
        log.error("times {}", times.size());
        log.error("eps_z {}", eps_z.get(EPS_Z.getOrdinateName()).size());
        log.error("eps_z {}", theta.get(THETA.getOrdinateName()).size());
        log.error("omega {}", highOmegasGraphDto.get(OMEGA_HIGH_R1.getRadialName()).size());
        log.error("omega {}", lowOmegasGraphDto.get(OMEGA_LOW_R1.getRadialName()).size());

        return Map.of(
                EPS_Z.getName(), eps_z.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray())),
                THETA.getName(), theta.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray())),
                OMEGA_LOW_R1.getName(), lowOmegasGraphDto.entrySet()
                        .stream()
                        .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray(), (v1, v2) -> v1, LinkedHashMap::new)
                        ),
                OMEGA_HIGH_R1.getName(), highOmegasGraphDto.entrySet()
                        .stream()
                        .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray(), (v1, v2) -> v1, LinkedHashMap::new)
                        )
        );
    }

}
