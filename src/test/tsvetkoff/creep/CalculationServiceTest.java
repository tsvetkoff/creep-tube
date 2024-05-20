package tsvetkoff.creep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tsvetkoff.domain.Params;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author SweetSupremum
 */
@SpringBootTest
class CalculationServiceTest {

    @Autowired
    private CalculationService program;
    private Params params;


    @BeforeEach
    public void before() {
        params = Params
                .builder()
                .dt(0.001)
                .dr(0.001)
                .t_max(1000)
                .sigma_0(78.48)
                .tau_max(32.02965)
                .q(0)
                .R1(5)
                .R2(10)
                .E(56000)
                .mu(0.3)
                .b(0)
                .n(1)
                .lambda(1)
                .mu_1(0.42)
                .m(5.40536)
                .c_p(2.52316E-15)
                .alpha1_p(0.931517198)
                .mAlpha_p(-0.4279)
                .A1_p(19.72)
                .mA_p(0)
                .stressTimes(Set.of(5.))
                .build();
    }

    /*
     * solid cylinders START
     */
    @Test
    public void test_sigma0_10_solid() {

        params.R1 = 0;
        params.R2 = 10;
        params.sigma_0 = 10 * 9.81;
        program.calculation(params);
        assertEquals(9.998, program.getR_damaged());
        assertEquals(62.383, program.getT());
        assertEquals(0.0017517857142857144, getEpsZFirst());
    }

    private Double getEpsZFirst() {
        return program.getEps_z().stream().findFirst().orElseThrow(AssertionError::new);
    }

    @Test
    public void test_sigma0_9_solid() {
        params.R1 = 0;
        params.R2 = 10;
        params.sigma_0 = 9 * 9.81;
        program.calculation(params);
        assertEquals(9.976, program.getR_damaged());
        assertEquals(106.212, program.getT());
        assertEquals(0.001576607142857143, getEpsZFirst());
    }

    @Test
    public void test_sigma0_8_solid() {
        params.R1 = 0;
        params.R2 = 10;
        params.sigma_0 = 8 * 9.81;
        program.calculation(params);
        assertEquals(9.991, program.getR_damaged());
        assertEquals(188.163, program.getT());
        assertEquals(0.00140142857142857151, getEpsZFirst());
    }

    @Test
    public void test_sigma0_7_5_solid() {
        params.R1 = 0;
        params.R2 = 10;
        params.sigma_0 = 7.5 * 9.81;
        program.calculation(params);
        assertEquals(9.991, program.getR_damaged());
        assertEquals(254.41, program.getT());
        assertEquals(0.0013138392857142858, getEpsZFirst());
    }

    @Test
    public void test_sigma0_7_solid() {
        params.R1 = 0;
        params.R2 = 10;
        params.sigma_0 = 7 * 9.81;
        program.calculation(params);
        assertEquals(9.999, program.getR_damaged());
        assertEquals(347.662, program.getT());
        assertEquals(0.0012262500000000001, getEpsZFirst());
    }
}