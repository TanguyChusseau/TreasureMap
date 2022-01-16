package carbon.exercise.treasuremap.service;

import carbon.exercise.treasuremap.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static carbon.exercise.treasuremap.utils.TreasureMapGameUtils.*;

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
        List<String> stringList = new ArrayList<>();
        stringList.add("C"
                + LINE_DELIMITER
                + treasureMap.getColumnCount()
                + LINE_DELIMITER
                + treasureMap.getRowCount()
        );
        for (TreasureMapCell[] treasureMapCells : treasureMap.getTreasureMapCells()) {
            for (TreasureMapCell treasureMapCell : treasureMapCells) {
                Adventurer adventurer = treasureMapCell.getAdventurer();
                Mountain mountain = treasureMapCell.getMountain();
                Treasure treasure = treasureMapCell.getTreasure();
                if (mountain != null) {
                    stringList.add(
                            NEW_LINE
                                    + MOUNTAIN_LINE_CHAR
                                    + LINE_DELIMITER
                                    + mountain.getPosition().getHorizontalPosition()
                                    + LINE_DELIMITER
                                    + mountain.getPosition().getVerticalPosition()
                    );
                } else if (treasure != null) {
                    stringList.add(
                            NEW_LINE
                                    + TREASURE_LINE_CHAR
                                    + LINE_DELIMITER
                                    + treasure.getPosition().getHorizontalPosition()
                                    + LINE_DELIMITER
                                    + treasure.getPosition().getVerticalPosition()
                                    + LINE_DELIMITER
                                    + treasure.getCount()
                    );
                } else if (adventurer != null) {
                    stringList.add(
                            NEW_LINE
                                    + ADVENTURER_LINE_CHAR
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
            stringList.add(Arrays.toString(treasureMapCells));
        }
        try {
            Files.write(Paths.get(outputFileLocation + outputFileName), stringList);
        } catch (IOException e) {
            LOGGER.error("Une erreur est survenue lors de l'écriture des données dans le fichier de sortie : "
                    + e.getMessage()
            );
        }
    }
}
