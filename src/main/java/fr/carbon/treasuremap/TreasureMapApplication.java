package fr.carbon.treasuremap;

import fr.carbon.treasuremap.model.TreasureMap;
import fr.carbon.treasuremap.service.AdventurerService;
import fr.carbon.treasuremap.service.InputFileReaderService;
import fr.carbon.treasuremap.service.OutputFileWriterService;
import fr.carbon.treasuremap.service.TreasureMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TreasureMapApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreasureMapApplication.class);

    public static void main(String[] args) {
        LOGGER.info("****** DEMARRAGE DU JEU  : LA CARTE AUX TRESORS ******");
        SpringApplication.run(TreasureMapApplication.class, args);
        LOGGER.info("****** FIN DU JEU  : LA CARTE AUX TRESORS ******");
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> inputFileLines = InputFileReaderService.getInputFileLines();
        TreasureMap treasureMap = TreasureMapService.createTreasureMapFromInputFile(inputFileLines);
        AdventurerService.processAdventurersMovementsOnTreasureMap(treasureMap);
        OutputFileWriterService.writeTreasureMapLinesToOutputFile(treasureMap);
    }
}
