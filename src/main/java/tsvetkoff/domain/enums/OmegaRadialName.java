package tsvetkoff.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author SweetSupremum
 */
@Getter
@RequiredArgsConstructor
public enum OmegaRadialName {
    OMEGA_LOW_R1("omega", "R1"),
    OMEGA_LOW_R2("omega", "R2"),
    OMEGA_HIGH_R1("Omega", "R1"),
    OMEGA_HIGH_R2("Omega", "R2");


    private final String name;
    private final String radialName;

}
