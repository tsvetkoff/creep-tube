package tsvetkoff.creep;

import lombok.Getter;
import org.springframework.stereotype.Service;
import tsvetkoff.domain.Graph;
import tsvetkoff.domain.Params;
import tsvetkoff.domain.enums.OmegaRadialName;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static tsvetkoff.domain.enums.OneDimensionalGraphs.EPS_Z;
import static tsvetkoff.domain.enums.OneDimensionalGraphs.THETA;

@Service
@Getter
public class CalculationService {
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private double[] r;
    private double t, r_damaged;
    private Graph graph;
    private Stress sigma;
    private CreepStrain p;
    private List<Double> eps_z, theta;

    private List<Double> omegaR1, omegaR2, OmegaR1, OmegaR2;

    private List<Double> times;
    private double G, F, M, Jr, Q;
    private MathUtils mathUtils;
    private double[] temp1, temp2, g;
    private Params params;

    public void init(Params params) {
        this.params = params.initGammaConstants();
        double N = MathUtils.round((params.R2 - params.R1) / params.dr, 7) + 1;
        if (N % 1 != 0) {
            throw new IllegalArgumentException("Число точек разбиения по радиусу должно быть целым, но равно " + N + ", уменьшите шаг");
        }
        if (N % 2 == 0) {
            throw new IllegalArgumentException("Число точек разбиения по радиусу " + N + " должно быть нечётным (поскольку интегралы вычисляются методом Симпсона)");
        }

        r = new double[(int) MathUtils.round(N, 7)];
        for (int j = 0; j < r.length; j++) {
            r[j] = MathUtils.round(params.R1 + j * params.dr, 7);
        }
        System.out.printf("Выполнена дискретизация по радиусу: [%s, %s, %s, ... , %s, %s] с шагом dr=%s (%d точек)%n",
                r[0], r[1], r[2], r[r.length - 2], r[r.length - 1], params.dr, r.length);

        t = 0.0;
        r_damaged = 0.0;
        final List<Double> initialValue = List.of(0.0);
        eps_z = new ArrayList<>(initialValue);
        theta = new ArrayList<>(initialValue);
        omegaR1 = new ArrayList<>(initialValue);
        omegaR2 = new ArrayList<>(initialValue);
        OmegaR1 = new ArrayList<>(initialValue);
        OmegaR2 = new ArrayList<>(initialValue);
        times = new ArrayList<>(initialValue);
        sigma = new Stress(r.length);
        p = new CreepStrain(r.length, params);
        graph = new Graph(params.dt, r, times);
        temp1 = new double[r.length];
        temp2 = new double[r.length];
        g = new double[r.length];
        mathUtils = new MathUtils(r.length);
    }

    public Graph calculation(Params params) {
        init(params);
        System.out.println("Начало расчёта с параметрами " + params);
        long start = System.currentTimeMillis();
        raiseForces(params);
        while (t < params.t_max) {
            t = MathUtils.round(t + params.dt, 7);
            times.add(t);
            creep(params);
            boolean isFinish = checkFinish();
            if (isFinish) {
                break;
            }
        }
        addStressToOutput(t + " ч");
        addOmegasToOutput();
        addStrainToOutput();
        System.out.println("Программа выполнилась за " + (System.currentTimeMillis() - start) / 1000.0 + " с");
        return graph;
    }

    public Future<Graph> asyncCalculation(Params params) {
        return executorService.submit(() -> calculation(params));
    }

    private void raiseForces(Params params) {
        G = params.E / (2 * (1 + params.mu));
        F = params.sigma_0 * PI * (pow(params.R2, 2) - pow(params.R1, 2));
        Jr = (PI / 2) * (pow(params.R2, 4) - pow(params.R1, 4));
        M = Jr * params.tau_max / params.R2;
        Q = params.q;
        System.out.println("F=" + F + " Н, M=" + M + " Н*мм");
        for (int j = 0; j < r.length; j++) {
            sigma.sigma_theta[j] = sigma.sigma_theta0[j] = sigma.sigma_theta[j];
            sigma.sigma_r[j] = sigma.sigma_r0[j] = sigma.sigma_r[j];
            if (params.R1 > 0) {
                sigma.sigma_theta[j] = sigma.sigma_theta0[j] = sigma.sigma_theta[j] + Q * pow(params.R1, 2) / (pow(params.R2, 2) - pow(params.R1, 2)) * (1 + pow(params.R2 / r[j], 2));
                sigma.sigma_r[j] = sigma.sigma_r0[j] = sigma.sigma_r[j] + Q * pow(params.R1, 2) / (pow(params.R2, 2) - pow(params.R1, 2)) * (1 - pow(params.R2 / r[j], 2));
            }
            sigma.sigma_z[j] = sigma.sigma_z0[j] = sigma.sigma_z[j] + F / (PI * (pow(params.R2, 2) - pow(params.R1, 2)));
            sigma.tau[j] = sigma.tau0[j] = (M / Jr) * r[j];
            sigma.s[j] = sigma.s0[j] = 1 / sqrt(2) * sqrt(pow(sigma.sigma_z0[j] - sigma.sigma_theta0[j], 2) + pow(sigma.sigma_z0[j] - sigma.sigma_r0[j], 2) + pow(sigma.sigma_theta0[j] - sigma.sigma_r0[j], 2) + 6 * pow(sigma.tau0[j], 2));
        }
        eps_z.add((sigma.sigma_z0[0] - params.mu * (sigma.sigma_theta0[0] + sigma.sigma_r0[0])) / params.E);
        theta.add(M / (G * Jr));
        addStressToOutput("0.0 ч");
    }

    private void addStressToOutput(String time) {
        graph.sigma_z0.put(time, sigma.sigma_z0.clone());
        graph.sigma_z.put(time, sigma.sigma_z.clone());
        graph.sigma_theta0.put(time, sigma.sigma_theta0.clone());
        graph.sigma_theta.put(time, sigma.sigma_theta.clone());
        graph.sigma_r0.put(time, sigma.sigma_r0.clone());
        graph.sigma_r.put(time, sigma.sigma_r.clone());
        graph.tau0.put(time, sigma.tau0.clone());
        graph.tau.put(time, sigma.tau.clone());
    }

    private void addStrainToOutput() {
        graph.eps_z.put(EPS_Z.getOrdinateName(), eps_z);
        graph.theta.put(THETA.getOrdinateName(), theta);
    }

    private void addOmegasToOutput() {
        graph.getLowOmegasGraphDto().put(OmegaRadialName.OMEGA_LOW_R1.getRadialName(), omegaR1);
        graph.getLowOmegasGraphDto().put(OmegaRadialName.OMEGA_LOW_R2.getRadialName(), omegaR2);
        graph.getHighOmegasGraphDto().put(OmegaRadialName.OMEGA_HIGH_R1.getRadialName(), OmegaR1);
        graph.getHighOmegasGraphDto().put(OmegaRadialName.OMEGA_HIGH_R2.getRadialName(), OmegaR2);
    }

    public void creep(Params params) {
        resolve_creep(params);
        resolve_sigma_r0(params);
        resolve_sigma_theta0(params);
        resolve_sigma_z0(params);
        resolve_tau0(params);

        for (int j = 0; j < r.length; j++) {
            sigma.s0[j] = 1 / sqrt(2) * sqrt(pow(sigma.sigma_z0[j] - sigma.sigma_theta0[j], 2) + pow(sigma.sigma_z0[j] - sigma.sigma_r0[j], 2) + pow(sigma.sigma_theta0[j] - sigma.sigma_r0[j], 2) + 6 * pow(sigma.tau0[j], 2));
            sigma.sigma_z[j] = sigma.sigma_z0[j] * (1 + p.omega[j]);
            sigma.sigma_r[j] = sigma.sigma_r0[j] * (1 + p.omega[j]);
            sigma.sigma_theta[j] = sigma.sigma_theta0[j] * (1 + p.omega[j]);
            sigma.tau[j] = sigma.tau0[j] * (1 + p.omega[j]);
            sigma.s[j] = sigma.s0[j] * (1 + p.omega[j]);
        }

        if (t % 10 == 0) {
            System.out.println("t=" + t + " ч");
        }
        if (params.stressTimes.contains(t)) {
            addStressToOutput(t + " ч");
        }
    }

    private boolean checkFinish() {
        boolean damaged = false;
        for (int j = 0; j < r.length; j++) {
            p.Omega[j] += (sigma.sigma_z[j] * (p.p_z[1][j] - p.p_z[0][j])
                    + sigma.sigma_theta[j] * (p.p_theta[1][j] - p.p_theta[0][j])
                    + sigma.sigma_r[j] * (p.p_r[1][j] - p.p_r[0][j])) / p.getA_p(sigma.s0[j])
                    + sigma.tau[j] * (p.gamma_p[1][j] - p.gamma_p[0][j]) / p.getA_gamma(sigma.s0[j]);
            if (p.Omega[j] >= 1 && !damaged) {
                System.out.println("Достигнут критерий выхода при t=" + t + " ч и r=" + r[j] + " мм, Omega=" + p.Omega[j]);
                r_damaged = r[j];
                damaged = true;
            }

            p.w_z[0][j] = p.w_z[1][j];
            p.w_r[0][j] = p.w_r[1][j];
            p.w_theta[0][j] = p.w_theta[1][j];
            p.w_gamma[0][j] = p.w_gamma[1][j];

            p.p_z[0][j] = p.p_z[1][j];
            p.p_r[0][j] = p.p_r[1][j];
            p.p_theta[0][j] = p.p_theta[1][j];
            p.gamma_p[0][j] = p.gamma_p[1][j];
        }

        OmegaR1.add(p.Omega[0]);
        OmegaR2.add(p.Omega[p.Omega.length - 1]);
        return damaged;
    }

    private void resolve_creep(Params params) {
        for (int j = 0; j < r.length; j++) {
            resolveV(sigma, j, params);

            p.w_r[1][j] += 3.0 / 2.0 * params.c_p * pow(sigma.s[j], params.m - 1) * (sigma.sigma_r[j] - 1.0 / 3.0 * (sigma.sigma_z[j] + sigma.sigma_theta[j] + sigma.sigma_r[j])) * params.dt;
            p.w_theta[1][j] += 3.0 / 2.0 * params.c_p * pow(sigma.s[j], params.m - 1) * (sigma.sigma_theta[j] - 1.0 / 3.0 * (sigma.sigma_z[j] + sigma.sigma_theta[j] + sigma.sigma_r[j])) * params.dt;
            p.w_z[1][j] += 3.0 / 2.0 * params.c_p * pow(sigma.s[j], params.m - 1) * (sigma.sigma_z[j] - 1.0 / 3.0 * (sigma.sigma_z[j] + sigma.sigma_theta[j] + sigma.sigma_r[j])) * params.dt;
            p.w_gamma[1][j] += 3 * params.c_gamma * pow(sigma.s[j], params.m - 1) * sigma.tau[j] * params.dt;

            p.p_z[1][j] = p.w_z[1][j] + p.v_z;
            p.p_r[1][j] = p.w_r[1][j] + p.v_r;
            p.p_theta[1][j] = p.w_theta[1][j] + p.v_theta;
            p.gamma_p[1][j] = p.w_gamma[1][j] + p.v_gamma;

            p.omega[j] += p.getAlpha_p(sigma.s0[j]) * (sigma.sigma_z[j] * (p.w_z[1][j] - p.w_z[0][j]) +
                    sigma.sigma_theta[j] * (p.w_theta[1][j] - p.w_theta[0][j]) +
                    sigma.sigma_r[j] * (p.w_r[1][j] - p.w_r[0][j])) +
                    p.getAlpha_gamma(sigma.s0[j]) * sigma.tau[j] * (p.w_gamma[1][j] - p.w_gamma[0][j]);
        }
        omegaR1.add(p.omega[0]);
        omegaR2.add(p.omega[p.omega.length - 1]);
    }

    private void resolveV(Stress sigma, int j, Params params) {
        if (params.b == 0) {
            return;
        }

        double sigma_I, sigma_II, sigma_III;
        if (params.tau_max == 0) {
            // если нет кручения, то тензор напряжений уже в главных осях
            sigma_I = sigma.sigma_z0[j];
            sigma_II = sigma.sigma_theta0[j];
            sigma_III = sigma.sigma_r0[j];
        } else {
            // для кручения вязкопластическая компонента считается значительно сложнее
            // необходимо осуществить переход к главным осям (в криволинейной системе координат, а не в декартовой!),
            // посчитать в главных осях v_I, v_II, v_III и затем пересчитать их в исходной системе координат
            // TODO сейчас это не реализовано
            throw new UnsupportedOperationException("The first stage of creep is not supported for torsion now");
        }

        p.brackets_I = params.b * pow(sigma.s0[j], params.n - 1) * sigma_I - p.beta_I[j];
        p.brackets_II = params.b * pow(sigma.s0[j], params.n - 1) * sigma_II - p.beta_II[j];
        p.brackets_III = params.b * pow(sigma.s0[j], params.n - 1) * sigma_III - p.beta_III[j];
        if (p.brackets_I * sigma_I > 0) {
            p.beta_I[j] += params.lambda * p.brackets_I * params.dt;
        }
        if (p.brackets_II * sigma_II > 0) {
            p.beta_II[j] += params.lambda * p.brackets_II * params.dt;
        }
        if (p.brackets_III * sigma_III > 0) {
            p.beta_III[j] += params.lambda * p.brackets_III * params.dt;
        }

        double v_I = p.beta_I[j] - params.mu_1 * (p.beta_II[j] + p.beta_III[j]);
        double v_II = p.beta_II[j] - params.mu_1 * (p.beta_I[j] + p.beta_III[j]);
        double v_III = p.beta_III[j] - params.mu_1 * (p.beta_I[j] + p.beta_II[j]);

        if (params.tau_max == 0) {
            p.v_z = v_I;
            p.v_theta = v_II;
            p.v_r = v_III;
        } else {
            // TODO реализовать пересчет в случае кручения
        }
    }

    private void resolve_sigma_r0(Params params) {
        resolve_g(params);
        if (params.R1 > 0) {
            resolve_sigma_r0_hollow(params);
        } else {
            resolve_sigma_r0_solid(params);
        }
    }

    private void resolve_sigma_r0_solid(Params params) {
        for (int j = 0; j < r.length; j++) {
            temp1[j] = g[j] * r[j];
        }

        mathUtils.varIntegral(temp1, params.dr);
        temp1[0] = 1.0 / 3.0 * (-3 * g[0] + 4 * g[1] - g[2]) / (2 * params.dr);
        for (int j = 1; j < r.length; j++) {
            temp1[j] = mathUtils.integralValue[j] / pow(r[j], 3);
        }

        mathUtils.varIntegral(temp1, params.dr);
        for (int j = 1; j < r.length; j++) {
            sigma.sigma_r0[j] = mathUtils.integralValue[j] - mathUtils.integralValue[r.length - 1];
        }
        sigma.sigma_r0[0] = sigma.sigma_r0[1];
    }

    private void resolve_sigma_r0_hollow(Params params) {
        for (int j = 0; j < r.length; j++) {
            temp1[j] = g[j] * r[j];
            temp2[j] = g[j] / r[j];
        }

        for (int j = 0; j < r.length; j++) {
            sigma.sigma_r0[j] = Q * pow(params.R1, 2) / (pow(params.R2, 2) - pow(params.R1, 2)) * (1 - pow(params.R2 / r[j], 2));
        }

        mathUtils.varIntegral(temp2, params.dr);
        double integral_value2 = mathUtils.integralValue[r.length - 1];
        for (int j = 0; j < r.length; j++) {
            sigma.sigma_r0[j] += 1.0 / 2.0 * mathUtils.integralValue[j];
        }

        mathUtils.varIntegral(temp1, params.dr);
        double integral_value1 = mathUtils.integralValue[r.length - 1];
        for (int j = 0; j < r.length; j++) {
            sigma.sigma_r0[j] += 1.0 / (2.0 * pow(r[j], 2)) * (-mathUtils.integralValue[j] +
                    (pow(r[j], 2) - pow(params.R1, 2)) / (pow(params.R2, 2) - pow(params.R1, 2)) * (integral_value1 - pow(params.R2, 2) * integral_value2));
        }
    }

    private void resolve_g(Params params) {
        g[0] = params.E / (1 - pow(params.mu, 2)) * (p.p_r[1][0] - p.p_theta[1][0]
                - params.R1 * ((p.p_theta[1][1] - p.p_theta[1][0]) / params.dr
                + params.mu * (p.p_z[1][1] - p.p_z[1][0]) / params.dr)
        );
        for (int j = 1; j < r.length - 1; j++) {
            g[j] = params.E / (1 - pow(params.mu, 2)) * (p.p_r[1][j] - p.p_theta[1][j]
                    - r[j] * ((p.p_theta[1][j + 1] - p.p_theta[1][j - 1]) / (2 * params.dr)
                    + params.mu * (p.p_z[1][j + 1] - p.p_z[1][j - 1]) / (2 * params.dr))
            );
        }
        g[r.length - 1] = params.E / (1 - pow(params.mu, 2)) * (p.p_r[1][r.length - 1] - p.p_theta[1][r.length - 1]
                - params.R2 * ((p.p_theta[1][r.length - 1] - p.p_theta[1][r.length - 2]) / params.dr
                + params.mu * (p.p_z[1][r.length - 1] - p.p_z[1][r.length - 2]) / params.dr)
        );
    }

    private void resolve_sigma_theta0(Params params) {
        sigma.sigma_theta0[0] = sigma.sigma_r0[0] + params.R1 * (-3 * sigma.sigma_r0[0] + 4 * sigma.sigma_r0[1] - sigma.sigma_r0[2]) / (2 * params.dr);
        for (int j = 1; j < r.length - 1; j++) {
            sigma.sigma_theta0[j] = sigma.sigma_r0[j] + r[j] * (sigma.sigma_r0[j + 1] - sigma.sigma_r0[j - 1]) / (2 * params.dr);
        }
        sigma.sigma_theta0[r.length - 1] = sigma.sigma_r0[r.length - 1] + params.R2 * (3 * sigma.sigma_r0[r.length - 1] - 4 * sigma.sigma_r0[r.length - 2] + sigma.sigma_r0[r.length - 3]) / (2 * params.dr);
    }

    private void resolve_sigma_z0(Params params) {
        for (int j = 0; j < r.length; j++) {
            temp1[j] = (p.p_z[1][j] - params.mu / params.E * (sigma.sigma_r0[j] + sigma.sigma_theta0[j])) * r[j];
        }
        double eps_z = F / (PI * (pow(params.R2, 2) - pow(params.R1, 2)) * params.E) + 2 / (pow(params.R2, 2) - pow(params.R1, 2)) * mathUtils.defIntegral(temp1, params.dr);
        this.eps_z.add(eps_z);
        for (int j = 0; j < r.length; j++) {
            sigma.sigma_z0[j] = params.E * (eps_z - p.p_z[1][j]) + params.mu * (sigma.sigma_r0[j] + sigma.sigma_theta0[j]);
        }
    }

    private void resolve_tau0(Params params) {
        for (int j = 0; j < r.length; j++) {
            temp1[j] = p.gamma_p[1][j] * pow(r[j], 2);
        }
        double theta = M / (G * Jr) + 2 * PI * mathUtils.defIntegral(temp1, params.dr) / Jr;
        this.theta.add(theta);
        for (int j = 0; j < r.length; j++) {
            sigma.tau0[j] = G * (theta * r[j] - p.gamma_p[1][j]);
        }
    }

}
