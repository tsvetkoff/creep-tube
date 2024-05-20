package tsvetkoff.utill;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

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

    public double[] getArrayWithSlice(double[] list, int pointCount) {
        int length = list.length;
        double step = getStep(length, pointCount);
        double[] slizeList = new double[pointCount + 1];
        int counter = 0;
        for (double i = 0; i < length; i += step) {
            slizeList[counter] = list[(int) i];
            counter++;
        }
        return slizeList;
    }

    public List<Double> getArrayWithSlice(List<Double> list, int pointCount) {
        int length = list.size();
        double step = getStep(length, pointCount);
        List<Double> slizeList = new ArrayList<>();
        for (double i = 0; i < length; i += step) {
            slizeList.add(list.get((int) i));
        }
        return slizeList;
    }
}
