package tsvetkoff.utill;

import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.Map;

/**
 * @author SweetSupremum
 */
@UtilityClass
public class StepUtils {
    public Double getStep(int streamLength, int pointCount) {

        if (streamLength <= 2 * pointCount) {
            return 1.;
        }
        return ((double) (streamLength - 1) / (double) pointCount);
    }
}
