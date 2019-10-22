package guru.springframework.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private Random random = new Random();

    @Override
    public Health health() {

        if (random.nextBoolean())
            return Health.down().withDetail("ERR-001", "Random Failure").build();

        return Health.up().build();
    }
}
