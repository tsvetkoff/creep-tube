package tsvetkoff.domain;

import lombok.Data;
import tsvetkoff.creep.MathUtils;
import tsvetkoff.domain.enums.StrategyTypeValues;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Graph {

    private static final Path OUTPUT = Paths.get("output");
    private static final char SEPARATOR = '\t';
    private static final int MAX_POINTS = 100;

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

    public void output(Path basePath) throws IOException {
        prepareToOutput();

        Path path = OUTPUT.resolve(basePath);
        Files.createDirectories(path);

        printStrainToFile("ε_z = ε_z(t)", eps_z, path.resolve("eps_z.tsv"));
        printStrainToFile("θ = θ(t)", theta, path.resolve("theta.tsv"));

        printStressToFile(q, path.resolve("q.tsv"));
        printStressToFile(sigma_z0, path.resolve("sigma_z0.tsv"));
        printStressToFile(sigma_z, path.resolve("sigma_z.tsv"));
        printStressToFile(sigma_theta0, path.resolve("sigma_theta0.tsv"));
        printStressToFile(sigma_theta, path.resolve("sigma_theta.tsv"));
        printStressToFile(sigma_r0, path.resolve("sigma_r0.tsv"));
        printStressToFile(sigma_r, path.resolve("sigma_r.tsv"));
        printStressToFile(tau0, path.resolve("tau0.tsv"));
        printStressToFile(tau, path.resolve("tau.tsv"));
    }

    private void printStressToFile(Map<String, double[]> function, Path path) throws IOException {
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
            out.print("rad/t");
            out.print(SEPARATOR);
            for (String title : function.keySet()) {
                out.print(title);
                out.print(SEPARATOR);
            }
            out.println();

            for (int j = 0; j < r.length; j++) {
                out.print(r[j]);
                out.print(SEPARATOR);
                for (double[] func : function.values()) {
                    out.print(func[j]);
                    out.print(SEPARATOR);
                }
                out.println();
            }
        }
    }

    private void printStrainToFile(String title, Map<Double, Double> function, Path path) throws IOException {
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
            out.print("t");
            out.print(SEPARATOR);
            out.println(title);
            for (double time : function.keySet()) {
                out.print(time);
                out.print(SEPARATOR);
                out.println(function.get(time));
            }
        }
    }

    private void prepareToOutput() {
        if (eps_z.size() > MAX_POINTS) {
            final double finishTime = Collections.max(eps_z.keySet());
            double newTimeStep = MathUtils.round(eps_z.size() / MAX_POINTS * dt, 7);
            Set<Double> newTimes = new HashSet<>(MAX_POINTS + 1);
            for (int i = 0; i < MAX_POINTS; i++) {
                newTimes.add(MathUtils.round(i * newTimeStep, 7));
            }
            newTimes.add(finishTime);
            eps_z.keySet().retainAll(newTimes);
            theta.keySet().retainAll(newTimes);
        }
    }


    public List<Pair<String, Map<String, double[]>>> getTwoDimensionalGraphWithName() {
        return List.of(
                Pair.of(StrategyTypeValues.sigma_z0.name(), new LinkedHashMap<>(sigma_z0)),
                Pair.of(StrategyTypeValues.sigma_z.name(), new LinkedHashMap<>(sigma_z)),
                Pair.of(StrategyTypeValues.sigma_theta0.name(), new LinkedHashMap<>(sigma_theta0)),
                Pair.of(StrategyTypeValues.sigma_theta.name(), new LinkedHashMap<>(sigma_theta)),
                Pair.of(StrategyTypeValues.sigma_r.name(), new LinkedHashMap<>(sigma_r)),
                Pair.of(StrategyTypeValues.sigma_r0.name(), new LinkedHashMap<>(sigma_r0)),
                Pair.of(StrategyTypeValues.tau0.name(), new LinkedHashMap<>(tau0)),
                Pair.of(StrategyTypeValues.tau.name(), new LinkedHashMap<>(tau))
        );
    }

    public List<Pair<String, Map<Double, Double>>> getOneDimensionalGraphWithName() {
        return List.of(
                Pair.of(StrategyTypeValues.eps_z.name(), eps_z),
                Pair.of(StrategyTypeValues.theta.name(), theta)
        );
    }

}
