package tsvetkoff.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author SweetSupremum
 */
@Getter
@RequiredArgsConstructor
public enum OneDimensionalGraphs {
    EPS_Z("eps_z"),
    THETA("theta");

    private final String name;
    private final String ordinateName = "value";
}
