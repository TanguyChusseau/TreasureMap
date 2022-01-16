package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public final class InputFileReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputFileReaderService.class);

    public List<String> getInputFileLines(String inputFileLocation, String inputFileName) throws ParseLineException {
        List<String> inputFileLines;
        try (Stream<String> stream = Files.lines(Paths.get(inputFileLocation + inputFileName))) {
            inputFileLines = stream
                    .map(String::strip)
                    .filter(line -> !line.startsWith("#"))
                    .toList();
        } catch (IOException e) {
            throw new ParseLineException("Une erreur est survenue lors de la lecture du fichier : ");
        }

        if (inputFileLines.size() == 0) {
            LOGGER.warn("Le fichier fourni en entrée ne contient aucune donnée.");
        }
        return inputFileLines;
    }
}
