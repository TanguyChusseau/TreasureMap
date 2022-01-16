package fr.carbon.treasuremap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class InputFileReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputFileReaderService.class);
    private static final String inputFileLocation = "src/main/resources/";
    private static final String inputFileName = "data.txt";

    public static List<String> getInputFileLines() {
        List<String> inputFileLines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(inputFileLocation + inputFileName))) {
            inputFileLines = stream.map(String::strip)
                    .filter(line -> !line.startsWith("#"))
                    .toList();
        } catch (IOException e) {
            LOGGER.error("Une erreur est survenue lors de la lecture du fichier : " + e.getMessage());
        }

        if (inputFileLines.size() == 0) {
            LOGGER.warn("Le fichier fourni en entrée ne contient aucune donnée.");
        }
        return inputFileLines;
    }
}
