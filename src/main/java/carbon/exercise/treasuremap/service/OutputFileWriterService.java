package carbon.exercise.treasuremap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class OutputFileWriterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputFileWriterService.class);
    private static final String outputFileLocation = "src/main/resources/";
    private static final String outputFileName = "treasureMap.txt";

    public static void writeTreasureMapLinesToOutputFile(String[][] treasureMapLines) {
        List<String> stringList = new ArrayList<>();
        for (String[] string : treasureMapLines) {
            stringList.add(Arrays.toString(string));
        }
        try {
            Files.write(Paths.get(outputFileLocation + outputFileName), stringList);
        } catch (IOException e) {
            LOGGER.error("Error reading specified input file : " + e.getMessage());
        }
    }
}
