package tsvetkoff.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@ToString
public class Params {

    public double dr, dt, t_max;
    public double R1, R2;
    public double mu, E;
    public double c_gamma, c_p, m, b, n, lambda, mu_1;
    public double A1_p, mA_p, A1_gamma, mA_gamma;
    public double alpha1_gamma, mAlpha_gamma, alpha1_p, mAlpha_p;
    public double sigma_0, tau_max, q;
    // моменты времени для отображения
    public Set<Double> stressTimes;

    public void initGammaConstants() {
        c_gamma = c_p;
        mAlpha_gamma = mAlpha_p;
        alpha1_gamma = alpha1_p;
        mA_gamma = mA_p;
        A1_gamma = A1_p;
    }
}
