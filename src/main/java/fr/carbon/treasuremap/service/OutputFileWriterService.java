package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseLineException;
import fr.carbon.treasuremap.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static fr.carbon.treasuremap.utils.TreasureMapGameUtils.*;

@Service
public class OutputFileWriterService {

    /**
     * Construit le fichier de sortie en fin de jeu.
     *
     * @param treasureMap        : la matrice représentant la carte aux trésors {@link TreasureMap}.
     * @param outputFileLocation : emplacement du fichier de sortie désiré.
     * @param outputFileName     : nom du fichier de sortie désiré.
     */
    public void writeTreasureMapLinesToOutputFile(TreasureMap treasureMap,
                                                  String outputFileLocation,
                                                  String outputFileName) throws ParseLineException {
        List<String> lines = new ArrayList<>();
        formatAndAddTreasureMapLine(treasureMap, lines);

        List<Mountain> mountains = new ArrayList<>();
        List<Treasure> treasures = new ArrayList<>();
        List<Adventurer> adventurers = new ArrayList<>();

        getTreasureMapContent(treasureMap, mountains, treasures, adventurers);

        for (Mountain mountain : mountains) {
            formatAndAddMountainLine(mountain, lines);
        }
        for (Treasure treasure : treasures) {
            formatAndAddTreasureLine(treasure, lines);
        }
        for (Adventurer adventurer : adventurers) {
            formatAndAddAdventurerLine(adventurer, lines);
        }

        try {
            Files.write(Paths.get(outputFileLocation + outputFileName), lines);
        } catch (IOException e) {
            throw new ParseLineException(
                    "Une erreur est survenue lors de l'écriture des données dans le fichier de sortie' du fichier : "
                            + e.getMessage()
            );
        }
    }


    protected void getTreasureMapContent(TreasureMap treasureMap,
                                         List<Mountain> mountains,
                                         List<Treasure> treasures,
                                         List<Adventurer> adventurers) {

        if (treasureMap == null || treasureMap.getTreasureMapCells() == null) return;
        for (TreasureMapCell[] treasureMapCells : treasureMap.getTreasureMapCells()) {
            for (TreasureMapCell treasureMapCell : treasureMapCells) {
                if (treasureMapCell != null) {
                    Mountain mountain = treasureMapCell.getMountain();
                    Treasure treasure = treasureMapCell.getTreasure();
                    Adventurer adventurer = treasureMapCell.getAdventurer();

                    if (mountain != null) {
                        mountains.add(mountain);
                    } else if (treasure != null && treasure.getCount() >  0) {
                        treasures.add(treasure);
                    } else if (adventurer != null) {
                        adventurers.add(adventurer);
                    }
                }
            }
        }
    }

    protected void formatAndAddTreasureMapLine(TreasureMap treasureMap, List<String> lines) {
        lines.add(TREASURE_MAP_LINE_CHAR
                + LINE_DELIMITER
                + treasureMap.getColumnCount()
                + LINE_DELIMITER
                + treasureMap.getRowCount()
        );
    }

    protected void formatAndAddMountainLine(Mountain mountain, List<String> lines) {
        lines.add(MOUNTAIN_LINE_CHAR
                + LINE_DELIMITER
                + mountain.getPosition().getHorizontalPosition()
                + LINE_DELIMITER
                + mountain.getPosition().getVerticalPosition()
        );
    }

    protected void formatAndAddTreasureLine(Treasure treasure, List<String> lines) {
        lines.add(TREASURE_LINE_CHAR
                + LINE_DELIMITER
                + treasure.getPosition().getHorizontalPosition()
                + LINE_DELIMITER
                + treasure.getPosition().getVerticalPosition()
                + LINE_DELIMITER
                + treasure.getCount()
        );
    }

    protected void formatAndAddAdventurerLine(Adventurer adventurer, List<String> lines) {
        lines.add(ADVENTURER_LINE_CHAR
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
