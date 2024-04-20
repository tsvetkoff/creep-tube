package tsvetkoff.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import tsvetkoff.creep.CalculationService;

/**
 * @author SweetSupremum
 */
@Configuration
public class CalculationConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CalculationService calculationService() {
        return new CalculationService();
    }
}
