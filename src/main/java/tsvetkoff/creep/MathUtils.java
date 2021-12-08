package tsvetkoff.creep;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    public double[] integralValue;

    public MathUtils(int N) {
        this.integralValue = new double[N];
    }

    /**
     * округление числа value до scale знаков после запятой
     */
    public static double round(double value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public void varIntegral(double[] function, double step) {
        integralValue[0] = 0;
        for (int k = 1; k < function.length; k++) {
            integralValue[k] = integralValue[k - 1] + step / 2 * (function[k - 1] + function[k]);
        }
    }

    public double defIntegral(double[] function, double step) {
        double sum = 0;
        for (int k = 1; k < function.length - 1; k = k + 2) {
            sum += function[k - 1] + 4 * function[k] + function[k + 1];
        }
        return step / 3 * sum;
    }

}
