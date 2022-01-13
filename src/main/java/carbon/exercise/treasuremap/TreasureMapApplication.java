package carbon.exercise.treasuremap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TreasureMapApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TreasureMapApplication.class);
        app.run(args);
    }
}
