package tsvetkoff.domain.enums;

import lombok.Getter;

/**
 * @author SweetSupremum
 */
@Getter
public enum OmegaType {
    OMEGA_LOW_R1("omega_r1"),
    OMEGA_LOW_R2("omega_r2"),
    OMEGA_HIGH_R1("Omega_r1"),
    OMEGA_HIGH_R2("Omega_r2");


    private String name;

    OmegaType(String name) {
        this.name = name;
    }
}
