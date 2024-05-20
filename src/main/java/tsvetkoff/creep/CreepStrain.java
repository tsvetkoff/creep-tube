package tsvetkoff.creep;

import tsvetkoff.domain.Params;

import static java.lang.Math.pow;

public class CreepStrain {

    // граничное значение интенсивности напряжений в формулах для работы и параметра разупрочнения
    // необходимо, для ограничения степенной зависимисти при отрицательном показателе степени
    private static final double S0_MIN = 1;

    public double[][] p_z, p_theta, p_r, gamma_p;
    public double v_theta, v_r, v_z, v_gamma;
    public double brackets_I, brackets_II, brackets_III;
    public double[] beta_I, beta_II, beta_III;
    public double[][] w_theta, w_r, w_z, w_gamma;
    public double[] omega;
    public double[] Omega;
    private Params params;

    public CreepStrain(int N, Params params) {
        this.params = params;
        p_z = new double[2][N];
        p_theta = new double[2][N];
        p_r = new double[2][N];
        gamma_p = new double[2][N];
        beta_I = new double[N];
        beta_II = new double[N];
        beta_III = new double[N];
        w_r = new double[2][N];
        w_theta = new double[2][N];
        w_z = new double[2][N];
        w_gamma = new double[2][N];
        omega = new double[N];
        Omega = new double[N];
    }

    public double getAlpha_p(double s0) {
        if (params.mAlpha_p < 0 && s0 < S0_MIN) {
            return params.alpha1_p * pow(S0_MIN, params.mAlpha_p);
        } else {
            return params.alpha1_p * pow(s0, params.mAlpha_p);
        }
    }

    public double getAlpha_gamma(double s0) {
        if (params.mAlpha_gamma < 0 && s0 < S0_MIN) {
            return params.alpha1_gamma * pow(S0_MIN, params.mAlpha_gamma);
        } else {
            return params.alpha1_gamma * pow(s0, params.mAlpha_gamma);
        }
    }

    public double getA_p(double s0) {
        if (params.mA_p < 0 && s0 < S0_MIN) {
            return params.A1_p * pow(S0_MIN, params.mA_p);
        } else {
            return params.A1_p * pow(s0, params.mA_p);
        }
    }

    public double getA_gamma(double s0) {
        if (params.mA_gamma < 0 && s0 < S0_MIN) {
            return params.A1_gamma * pow(S0_MIN, params.mA_gamma);
        } else {
            return params.A1_gamma * pow(s0, params.mA_gamma);
        }
    }

}
