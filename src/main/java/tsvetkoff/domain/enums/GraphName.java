package tsvetkoff.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author SweetSupremum
 */
@Getter
@RequiredArgsConstructor
public enum GraphName {
    SIGMA_Z0("sigma_z0"),
    SIGMA_Z("sigma_z"),
    SIGMA_THETA0("sigma_theta0"),
    SIGMA_THETA("sigma_theta"),
    SIGMA_R0("sigma_r0"),
    SIGMA_R("sigma_r"),
    TAU0("tau0"),
    TAU("tau"),
    Q("q"),
    S("s"),
    S0("s0"),

    OMEGAS("omegas");

    private final String name;
    }
