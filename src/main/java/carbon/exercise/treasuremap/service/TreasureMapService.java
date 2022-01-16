package carbon.exercise.treasuremap.service;

import carbon.exercise.treasuremap.exception.ParseAdventurerLineException;
import carbon.exercise.treasuremap.exception.ParseLineException;
import carbon.exercise.treasuremap.exception.ParseMountainLineException;
import carbon.exercise.treasuremap.exception.ParseTreasureLineException;
import carbon.exercise.treasuremap.model.*;
import carbon.exercise.treasuremap.utils.TreasureMapGameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static carbon.exercise.treasuremap.utils.TreasureMapGameUtils.*;

public class TreasureMapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreasureMapService.class);

    /**
     * Créé et remplit la carte aux trésors à partir des informations fournies dans le fichier en entrée.
     *
     * @param inputFileLines : les lignes du fichier.
     * @return la carte aux trésors {@link TreasureMap}.
     * @throws ParseLineException en cas de problème de lecture des informations contenues dans le fichier.
     */
    public static TreasureMap createTreasureMapFromInputFile(List<String> inputFileLines) throws ParseLineException {
        if (inputFileLines.get(0).charAt(0) != TREASURE_LINE_CHAR) {
            throw new ParseLineException("Erreur lors de l'initialisation de la carte aux trésors : " +
                    "la première ligne du fichier d'entrée doit commencer par la lettre C.");
        }

        TreasureMap treasureMap = getTreasureMapDetailsFromInputFileLine(inputFileLines.get(0));
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        for (String line : inputFileLines.stream().skip(1).toList()) {
            char treasureMapObject = line.charAt(0);
            switch (treasureMapObject) {
                case MOUNTAIN_LINE_CHAR -> putMountainOnTreasureMapCell(line, treasureMapCells);
                case TREASURE_LINE_CHAR -> putTreasureOnTreasureMapCell(line, treasureMapCells);
                case ADVENTURER_LINE_CHAR -> putAdventurerOnTreasureMapCell(line, treasureMapCells);
                default -> throw new ParseLineException("Erreur lors du remplissage de la carte aux trésors : " +
                        "chaque ligne du fichier en entrée après la première doit commencer par la lettre A, M, ou T."
                );
            }
        }
        treasureMap.setTreasureMapCells(treasureMapCells); //Utile ?
        return treasureMap;
    }

    /**
     * Récupère les informations de la carte aux trésors depuis la ligne correspondante dans le fichier en entrée.
     *
     * @param line : ligne du fichier.
     * @return un objet carte aux Trésors {@link TreasureMapCell}.
     * @throws ParseLineException .
     */
    private static TreasureMap getTreasureMapDetailsFromInputFileLine(String line) throws ParseLineException {
        String[] treasureMapDetails = splitLine(line);
        if (treasureMapDetails.length != 3) {
            throw new ParseLineException(ERROR_WHEN_READING_DETAILS +
                    "de la carte aux trésors : des informations sont manquantes.");
        }

        String columnCount = treasureMapDetails[1];
        String rowCount = treasureMapDetails[2];
        TreasureMapGameUtils.checkPositionFromInputFileType(columnCount, rowCount);

        TreasureMapCell[][] treasureMapCells =
                setupTreasureMapMatrix(Integer.parseInt(columnCount), Integer.parseInt(rowCount));

        return new TreasureMap(Integer.parseInt(columnCount), Integer.parseInt(rowCount), treasureMapCells);
    }

    /**
     * Créé la matrice de {@link TreasureMapCell} représentant la carte aux trésors {@link TreasureMap}.
     *
     * @param columnCount : le nombre de colonnes.
     * @param rowCount    : le nombre de lignes.
     * @return : la matrice construite (array à deux dimensions).
     */
    private static TreasureMapCell[][] setupTreasureMapMatrix(int columnCount, int rowCount) {
        TreasureMapCell[][] treasureMapMatrix = new TreasureMapCell[columnCount][rowCount];
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                treasureMapMatrix[i][j] = new TreasureMapCell();
            }
        }
        return treasureMapMatrix;
    }

    /**
     * Permet de s'assurer qu'une case de la carte aux trésors est libre.
     *
     * @param position    : la position de la case dans la carte.
     * @param treasureMap : La carte aux trésors {@link TreasureMapCell} sous forme de matrice.
     * @return true si la case est libre.
     */
    private static boolean isPositionTakenOnTreasureMap(Position position, TreasureMapCell[][] treasureMap) {
        return treasureMap[position.getHorizontalPosition()][position.getVerticalPosition()] != null;
    }

    private static void logWhenPositionIsTaken(TreasureMapCell[][] treasureMap,
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

    /**
     * Ajoute une {@link Mountain} dans un emplacement de la carte aux trésors {@link TreasureMap}.
     *
     * @param line             ligne du fichier en entrée contenant les informations de la montagne.
     * @param treasureMapCells : matrice de {@link TreasureMapCell} représentant la carte aux trésors.
     * @throws ParseMountainLineException en cas d'erreur de lecture des informations.
     */
    private static void putMountainOnTreasureMapCell(String line, TreasureMapCell[][] treasureMapCells)
            throws ParseMountainLineException {
        Mountain mountain = MountainService.createMountainFromInputFileLine(line);
        Position mountainPosition = mountain.getPosition();
        logWhenPositionIsTaken(treasureMapCells, mountainPosition, "Montagne");

        TreasureMapCell treasureMapCell = new TreasureMapCell();
        treasureMapCell.setMountain(mountain);

        treasureMapCells[mountainPosition.getHorizontalPosition()][mountainPosition.getVerticalPosition()]
                = treasureMapCell;
    }

    /**
     * Ajoute un {@link Treasure} dans un emplacement de la carte aux trésors {@link TreasureMap}.
     *
     * @param line             ligne du fichier en entrée contenant les informations du trésor.
     * @param treasureMapCells : matrice de {@link TreasureMapCell} représentant la carte aux trésors.
     * @throws ParseTreasureLineException en cas d'erreur de lecture des informations.
     */
    private static void putTreasureOnTreasureMapCell(String line, TreasureMapCell[][] treasureMapCells)
            throws ParseTreasureLineException {
        Treasure treasure = TreasureService.createTreasureFromInputFileLine(line);
        Position treasurePosition = treasure.getPosition();
        logWhenPositionIsTaken(treasureMapCells, treasurePosition, "Trésor");

        TreasureMapCell treasureMapCell = new TreasureMapCell();
        treasureMapCell.setTreasure(treasure);

        treasureMapCells[treasurePosition.getHorizontalPosition()][treasurePosition.getVerticalPosition()]
                = treasureMapCell;
    }

    /**
     * Ajoute un {@link Adventurer} dans un emplacement de la carte aux trésors {@link TreasureMap}.
     *
     * @param line             ligne du fichier en entrée contenant les informations de l'aventurier.
     * @param treasureMapCells : matrice de {@link TreasureMapCell} représentant la carte aux trésors.
     * @throws ParseAdventurerLineException en cas d'erreur de lecture des informations.
     */
    private static void putAdventurerOnTreasureMapCell(String line, TreasureMapCell[][] treasureMapCells)
            throws ParseAdventurerLineException {
        Adventurer adventurer = AdventurerService.createAdventurerFromInputFileLine(line);
        Position adventurerPosition = adventurer.getPosition();
        logWhenPositionIsTaken(treasureMapCells, adventurerPosition, "Aventurier");

        TreasureMapCell treasureMapCell = new TreasureMapCell();
        treasureMapCell.setAdventurer(adventurer);

        treasureMapCells[adventurerPosition.getHorizontalPosition()][adventurerPosition.getVerticalPosition()]
                = treasureMapCell;
    }


}
