package tsvetkoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author SweetSupremum
 */
@SpringBootApplication
@EnableAsync
public class CreepTubeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreepTubeApplication.class);
    }
}
