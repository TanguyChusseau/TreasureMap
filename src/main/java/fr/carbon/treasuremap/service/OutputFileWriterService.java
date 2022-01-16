package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static fr.carbon.treasuremap.utils.TreasureMapGameUtils.*;

public final class OutputFileWriterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputFileWriterService.class);
    private static final String outputFileLocation = "src/main/resources/";
    private static final String outputFileName = "treasureMap.txt";

    /**
     * Construit le fichier de sortie en fin de jeu.
     *
     * @param treasureMap : la matrice représentant la carte aux trésors {@link TreasureMap}.
     */
    public static void writeTreasureMapLinesToOutputFile(TreasureMap treasureMap) {
        List<String> lines = new ArrayList<>();
        formatAndAddTreasureMapLine(treasureMap, lines);

        List<Mountain> mountains = new ArrayList<>();
        List<Treasure> treasures = new ArrayList<>();
        List<Adventurer> adventurers = new ArrayList<>();

        getTreasureMapContent(treasureMap, mountains, treasures, adventurers);

        for (Mountain mountain : mountains) {
            formatAndAddMountainLine(lines, mountain);
        }
        for (Treasure treasure : treasures) {
            formatAndAddTreasureLine(lines, treasure);
        }
        for (Adventurer adventurer : adventurers) {
            formatAndAddAdventurerLine(lines, adventurer);
        }

        try {
            Files.write(Paths.get(outputFileLocation + outputFileName), lines);
        } catch (IOException e) {
            LOGGER.error("Une erreur est survenue lors de l'écriture des données dans le fichier de sortie : "
                    + e.getMessage()
            );
        }
    }

    private static void formatAndAddTreasureMapLine(TreasureMap treasureMap, List<String> stringList) {
        stringList.add(TREASURE_MAP_LINE_CHAR
                + LINE_DELIMITER
                + treasureMap.getColumnCount()
                + LINE_DELIMITER
                + treasureMap.getRowCount()
        );
    }

    private static void getTreasureMapContent(TreasureMap treasureMap, List<Mountain> mountains, List<Treasure> treasures, List<Adventurer> adventurers) {
        for (TreasureMapCell[] treasureMapCells : treasureMap.getTreasureMapCells()) {
            for (TreasureMapCell treasureMapCell : treasureMapCells) {
                Adventurer adventurer = treasureMapCell.getAdventurer();
                Mountain mountain = treasureMapCell.getMountain();
                Treasure treasure = treasureMapCell.getTreasure();
                if (mountain != null) {
                    mountains.add(mountain);
                } else if (treasure != null) {
                    treasures.add(treasure);
                } else if (adventurer != null) {
                    adventurers.add(adventurer);
                }
            }
        }
    }

    private static void formatAndAddMountainLine(List<String> stringList, Mountain mountain) {
        stringList.add(MOUNTAIN_LINE_CHAR
                + LINE_DELIMITER
                + mountain.getPosition().getHorizontalPosition()
                + LINE_DELIMITER
                + mountain.getPosition().getVerticalPosition()
        );
    }

    private static void formatAndAddTreasureLine(List<String> stringList, Treasure treasure) {
        stringList.add(TREASURE_LINE_CHAR
                + LINE_DELIMITER
                + treasure.getPosition().getHorizontalPosition()
                + LINE_DELIMITER
                + treasure.getPosition().getVerticalPosition()
                + LINE_DELIMITER
                + treasure.getCount()
        );
    }

    private static void formatAndAddAdventurerLine(List<String> stringList, Adventurer adventurer) {
        stringList.add(ADVENTURER_LINE_CHAR
                + LINE_DELIMITER
                + adventurer.getName()
                + LINE_DELIMITER
                + adventurer.getPosition().getHorizontalPosition()
                + LINE_DELIMITER
                + adventurer.getPosition().getVerticalPosition()
                + LINE_DELIMITER
                + adventurer.getOrientation().getValue()
                + LINE_DELIMITER
                + adventurer.getCollectedTreasuresCount()
        );
    }
}
