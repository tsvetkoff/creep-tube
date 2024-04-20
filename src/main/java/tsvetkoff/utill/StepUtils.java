package tsvetkoff.utill;

import lombok.experimental.UtilityClass;

/**
 * @author SweetSupremum
 */
@UtilityClass
public class StepUtils {
    public double getStep(int streamLength, int pointCount) {

        if (streamLength <= 2 * pointCount) {
            return 1.;
        }
        return ((double) (streamLength - 1) / (double) pointCount);
    }
}
