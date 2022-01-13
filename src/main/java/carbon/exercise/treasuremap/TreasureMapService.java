package carbon.exercise.treasuremap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

@Service
public class TreasureMapService implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreasureMapService.class);

    @Override
    public void run(String... args) throws Exception {
        File file = ResourceUtils.getFile("classpath:data.txt");
        if (file.exists()) {
            byte[] fileData = Files.readAllBytes(file.toPath());
            String fileContent = new String(fileData);

            LOGGER.info("data.txt file content:");
            LOGGER.info(fileContent);
        }
    }
}
