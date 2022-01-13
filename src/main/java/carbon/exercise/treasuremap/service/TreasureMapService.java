package carbon.exercise.treasuremap.service;

import carbon.exercise.treasuremap.exception.ParseLineException;
import carbon.exercise.treasuremap.model.*;
import carbon.exercise.treasuremap.utils.TreasureMapGameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static carbon.exercise.treasuremap.utils.TreasureMapGameUtils.ERROR_WHEN_READING_DETAILS;

@Service
public class TreasureMapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreasureMapService.class);

    public static String[][] createTreasureMapFromInputFile(List<String> inputFileLines) throws ParseLineException {
        if (inputFileLines.get(0).charAt(0) != 'C') {
            throw new ParseLineException("Erreur lors de l'initialisation de la carte aux trésors : " +
                    "la première ligne du fichier d'entrée doit commencer par la lettre C.");
        }

        TreasureMap treasureMapDetails = getTreasureMapDetailsFromInputFileLine(inputFileLines.get(0));
        String[][] treasureMapMatrix =
                setupTreasureMapMatrix(treasureMapDetails.getColumnCount(), treasureMapDetails.getRowCount());

        for (String line : inputFileLines.stream().skip(1).toList()) {
            char treasureMapObject = line.charAt(0);

            if (treasureMapObject == TreasureMapGameUtils.MOUNTAIN_LINE_CHAR) {
                Mountain mountain = MountainService.createMountainFromInputFileLine(line);
                Position mountainPosition = mountain.getPosition();
                logWhenPositionIsTaken(treasureMapMatrix, mountainPosition, "Montagne");

                treasureMapMatrix
                        [mountainPosition.getHorizontalPosition()][mountainPosition.getVerticalPosition()] = "M";

            } else if (treasureMapObject == TreasureMapGameUtils.TREASURE_LINE_CHAR) {
                Treasure treasure = TreasureService.createTreasureFromInputFileLine(line);
                Position treasurePosition = treasure.getPosition();
                logWhenPositionIsTaken(treasureMapMatrix, treasurePosition, "Trésor");

                treasureMapMatrix[treasurePosition.getHorizontalPosition()][treasurePosition.getVerticalPosition()]
                        = "T(" + treasure.getCount() + ")";

            } else if (treasureMapObject == TreasureMapGameUtils.ADVENTURER_LINE_CHAR) {
                Adventurer adventurer = AdventurerService.createAdventurerFromInputFileLine(line);
                Position adventurerPosition = adventurer.getPosition();
                logWhenPositionIsTaken(treasureMapMatrix, adventurerPosition, "Aventurier");

                treasureMapMatrix[adventurerPosition.getHorizontalPosition()][adventurerPosition.getVerticalPosition()]
                        = "A" + "(" + adventurer.getName() + ")";

            } else {
                throw new ParseLineException("Erreur lors du remplissage de la carte aux trésors : " +
                        "chaque ligne du fichier en entrée après la première doit commencer par la lettre A, M, ou T."
                );
            }
        }
        return treasureMapMatrix;
    }

    protected static List<Adventurer> getAdventurersFromTreasureMap(String[][] treasureMap) {
        return Collections.emptyList();
    }

    /**
     * Récupère les informations de la carte aux trésors depuis la ligne correspondante dans le fichier en entrée.
     *
     * @param line : ligne du fichier.
     * @return un objet carte aux Trésors {@link TreasureMap}.
     * @throws ParseLineException .
     */
    private static TreasureMap getTreasureMapDetailsFromInputFileLine(String line) throws ParseLineException {
        String[] treasureMapDetails = TreasureMapGameUtils.splitLine(line);
        if (treasureMapDetails.length != 3) {
            throw new ParseLineException(ERROR_WHEN_READING_DETAILS +
                    "de la carte aux trésors : des informations sont manquantes.");
        }
        String columnCount = treasureMapDetails[1];
        String rowCount = treasureMapDetails[2];
        TreasureMapGameUtils.checkPositionFromInputFileType(columnCount, rowCount);

        return new TreasureMap(Integer.parseInt(columnCount), Integer.parseInt(rowCount));
    }

    /**
     * Permet de s'assurer qu'une case de la carte aux trésors est libre.
     *
     * @param position    : la position de la case dans la carte.
     * @param treasureMap : La carte aux trésors {@link TreasureMap} sous forme de matrice.
     * @return true si la case est libre.
     */
    private static boolean isPositionTakenOnTreasureMap(Position position, String[][] treasureMap) {
        return treasureMap[position.getHorizontalPosition()][position.getVerticalPosition()] != null
                && !treasureMap[position.getHorizontalPosition()][position.getVerticalPosition()].equals(".");
    }

    private static void logWhenPositionIsTaken(String[][] treasureMap,
                                               Position position,
                                               String objectToAdd) {
        if (isPositionTakenOnTreasureMap(position, treasureMap)) {
            LOGGER.warn("L'emplacement ("
                    + position.getHorizontalPosition()
                    + ","
                    + position.getVerticalPosition()
                    + ") n'est pas disponible. L'ajout de l'objet " + objectToAdd + " à cette position est donc ignoré."
            );
        }
    }

    private static String[][] setupTreasureMapMatrix(int columnCount, int rowCount) {
        String[][] treasureMapMatrix = new String[columnCount][rowCount];
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                treasureMapMatrix[i][j] = ".";
            }
        }
        return treasureMapMatrix;
    }
}
