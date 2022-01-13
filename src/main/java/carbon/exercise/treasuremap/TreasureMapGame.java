package carbon.exercise.treasuremap;

import carbon.exercise.treasuremap.service.InputFileReaderService;
import carbon.exercise.treasuremap.service.OutputFileWriterService;
import carbon.exercise.treasuremap.service.TreasureMapService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TreasureMapGame {

    @PostConstruct
    public static void resolveTreasureMap() throws Exception {
        List<String> inputFileLines = InputFileReaderService.getInputFileLines();
        String[][] treasureMap = TreasureMapService.createTreasureMapFromInputFile(inputFileLines);
        // AdventurerService.processAdventurerMovements(null);
        OutputFileWriterService.writeTreasureMapLinesToOutputFile(treasureMap);
    }
}
