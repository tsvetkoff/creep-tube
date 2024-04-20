package tsvetkoff.domain.enums;

import lombok.Getter;

/**
 * @author SweetSupremum
 */
public enum GraphNames {
    SIGMA_Z0("sigma_z0"),
    SIGMA_Z("sigma_z"),
    SIGMA_THETA0("sigma_theta0"),
    SIGMA_THETA("sigma_theta"),
    SIGMA_R0("sigma_r0"),
    SIGMA_R("sigma_r"),
    TAU0("tau0"),
    TAU("tau"),
    Q("q"),
    EPS_Z("eps_z"),
    THETA("theta"),
    OMEGAS("omegas");

    @Getter
    private final String name;

    GraphNames(String name) {
        this.name = name;
    }

}
