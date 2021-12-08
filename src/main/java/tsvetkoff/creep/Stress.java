package tsvetkoff.creep;

public class Stress {

    public double[] sigma_z0, sigma_theta0, sigma_r0, tau0;
    public double[] sigma_z, sigma_theta, sigma_r, tau;
    public double[] s0, s;

    public Stress(int N) {
        sigma_z0 = new double[N];
        sigma_z = new double[N];
        sigma_r0 = new double[N];
        sigma_r = new double[N];
        sigma_theta0 = new double[N];
        sigma_theta = new double[N];
        tau0 = new double[N];
        tau = new double[N];
        s0 = new double[N];
        s = new double[N];
    }

}
