package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.*;
import fr.carbon.treasuremap.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static fr.carbon.treasuremap.utils.TreasureMapGameUtils.*;

@Service
public class TreasureMapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreasureMapService.class);

    private final AdventurerService adventurerService;
    private final MountainService mountainService;
    private final TreasureService treasureService;

    public TreasureMapService(AdventurerService adventurerService,
                              MountainService mountainService,
                              TreasureService treasureService) {
        this.adventurerService = adventurerService;
        this.mountainService = mountainService;
        this.treasureService = treasureService;
    }

    /**
     * Créé et remplit la carte aux trésors à partir des informations fournies dans le fichier en entrée.
     *
     * @param inputFileLines : les lignes du fichier.
     * @return la carte aux trésors {@link TreasureMap}.
     * @throws ParseLineException en cas de problème de lecture des informations contenues dans le fichier.
     */
    public TreasureMap createTreasureMapFromInputFile(List<String> inputFileLines) throws ParseLineException {
        if (inputFileLines.get(0).toUpperCase().charAt(0) != TREASURE_MAP_LINE_CHAR) {
            throw new ParseLineException("Erreur lors de l'initialisation de la carte aux trésors : " +
                    "la première ligne du fichier d'entrée doit commencer par la lettre C.");
        }

        TreasureMap treasureMap = getTreasureMapDetailsFromInputFileLine(inputFileLines.get(0));
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        int apparitionOrder = 1;
        for (String line : inputFileLines.stream().skip(1).toList()) {
            char treasureMapObject = line.toUpperCase().charAt(0);
            switch (treasureMapObject) {
                case MOUNTAIN_LINE_CHAR -> putMountainOnTreasureMapCell(line, treasureMap, treasureMapCells);
                case TREASURE_LINE_CHAR -> putTreasureOnTreasureMapCell(line, treasureMap, treasureMapCells);
                case ADVENTURER_LINE_CHAR -> {
                    putAdventurerOnTreasureMapCell(line, treasureMap, treasureMapCells, apparitionOrder);
                    apparitionOrder++;
                }
                default -> throw new ParseLineException("Erreur lors du remplissage de la carte aux trésors : " +
                        "chaque ligne du fichier en entrée après la première doit commencer par la lettre A, M, ou T."
                );
            }
        }
        return treasureMap;
    }

    /**
     * Récupère les informations de la carte aux trésors depuis la ligne correspondante dans le fichier en entrée.
     *
     * @param line : ligne du fichier.
     * @return un objet carte aux Trésors {@link TreasureMapCell}.
     * @throws ParseTreasureMapLineException en cas d'erreur lors de la lecture des informations du fichier.
     */
    protected TreasureMap getTreasureMapDetailsFromInputFileLine(String line) throws ParseTreasureMapLineException {
        String[] treasureMapDetails = splitLine(line);
        if (treasureMapDetails.length < 3) {
            throw new ParseTreasureMapLineException(ERROR_WHEN_READING_DETAILS +
                    "de la carte aux trésors : des informations sont manquantes.");
        }

        String columnCount = treasureMapDetails[1];
        String rowCount = treasureMapDetails[2];
        try {
            checkPositionFromInputFileType(columnCount, rowCount);
        } catch (ParseLineException ple) {
            throw new ParseTreasureMapLineException(ple.getMessage());
        }

        return new TreasureMap(Integer.parseInt(columnCount), Integer.parseInt(rowCount));
    }

    protected boolean isPositionNegativeOrOutOfBounds(Position position, TreasureMap treasureMap) {
        return position.getHorizontalPosition() < 0
                || position.getHorizontalPosition() >= treasureMap.getColumnCount()
                || position.getVerticalPosition() < 0
                || position.getVerticalPosition() >= treasureMap.getRowCount();
    }

    /**
     * Permet de s'assurer qu'une case de la carte aux trésors est libre.
     *
     * @param position         : la position de la case dans la carte.
     * @param treasureMapCells : La carte aux trésors {@link TreasureMapCell} sous forme de matrice.
     * @return true si la case est libre.
     */
    protected boolean isPositionNullOrEmptyTakenOnTreasureMap(Position position,
                                                              TreasureMapCell[][] treasureMapCells) {
        return position == null
                || treasureMapCells[position.getHorizontalPosition()][position.getVerticalPosition()] == null
                || treasureMapCells[position.getHorizontalPosition()][position.getVerticalPosition()].getMountain() != null
                || treasureMapCells[position.getHorizontalPosition()][position.getVerticalPosition()].getTreasure() != null
                || treasureMapCells[position.getHorizontalPosition()][position.getVerticalPosition()].getAdventurer() != null;
    }

    private void logWhenPositionIsNegativeOrOutOfBounds(Position position,
                                                        TreasureMap treasureMap,
                                                        String objectToAdd) {
        if (isPositionNegativeOrOutOfBounds(position, treasureMap)) {
            LOGGER.warn("L'emplacement ("
                    + position.getHorizontalPosition()
                    + ","
                    + position.getVerticalPosition()
                    + ") est hors limites de la carte ou de valeur négative. L'ajout de l'objet "
                    + objectToAdd
                    + " à cette position est donc ignoré."
            );
        }
    }

    private void logWhenPositionIsTaken(Position position,
                                        TreasureMapCell[][] treasureMapCells,
                                        String objectToAdd) {
        if (isPositionNullOrEmptyTakenOnTreasureMap(position, treasureMapCells)) {
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
     * @param treasureMap      : la carte aux trésors.
     * @param treasureMapCells : matrice de {@link TreasureMapCell} représentant la carte aux trésors.
     * @throws ParseMountainLineException en cas d'erreur de lecture des informations.
     */
    protected void putMountainOnTreasureMapCell(String line,
                                                TreasureMap treasureMap,
                                                TreasureMapCell[][] treasureMapCells)
            throws ParseMountainLineException {
        Mountain mountain = mountainService.createMountainFromInputFileLine(line);
        if (mountain == null) return;

        Position mountainPosition = mountain.getPosition();
        if (isPositionNegativeOrOutOfBounds(mountainPosition, treasureMap)) {
            logWhenPositionIsNegativeOrOutOfBounds(mountainPosition, treasureMap, "Montagne");
            return;
        }
        if (isPositionNullOrEmptyTakenOnTreasureMap(mountainPosition, treasureMapCells)) {
            logWhenPositionIsTaken(mountainPosition, treasureMapCells, "Montagne");
            return;
        }

        TreasureMapCell treasureMapCell = new TreasureMapCell();
        treasureMapCell.setMountain(mountain);

        treasureMapCells[mountainPosition.getHorizontalPosition()][mountainPosition.getVerticalPosition()]
                = treasureMapCell;
    }

    /**
     * Ajoute un {@link Treasure} dans un emplacement de la carte aux trésors {@link TreasureMap}.
     *
     * @param line             ligne du fichier en entrée contenant les informations du trésor.
     * @param treasureMap      : la carte aux trésors.
     * @param treasureMapCells : matrice de {@link TreasureMapCell} représentant la carte aux trésors.
     * @throws ParseTreasureLineException en cas d'erreur de lecture des informations.
     */
    protected void putTreasureOnTreasureMapCell(String line,
                                                TreasureMap treasureMap,
                                                TreasureMapCell[][] treasureMapCells) throws ParseTreasureLineException {
        Treasure treasure = treasureService.createTreasureFromInputFileLine(line);
        if (treasure == null) return;

        Position treasurePosition = treasure.getPosition();
        if (isPositionNegativeOrOutOfBounds(treasurePosition, treasureMap)) {
            logWhenPositionIsNegativeOrOutOfBounds(treasurePosition, treasureMap, "Trésor");
            return;
        }
        if (isPositionNullOrEmptyTakenOnTreasureMap(treasurePosition, treasureMapCells)) {
            logWhenPositionIsTaken(treasurePosition, treasureMapCells, "Trésor");
            return;
        }

        TreasureMapCell treasureMapCell = new TreasureMapCell();
        treasureMapCell.setTreasure(treasure);

        treasureMapCells[treasurePosition.getHorizontalPosition()][treasurePosition.getVerticalPosition()]
                = treasureMapCell;
    }

    /**
     * Ajoute un {@link Adventurer} dans un emplacement de la carte aux trésors {@link TreasureMap}.
     *
     * @param line             ligne du fichier en entrée contenant les informations de l'aventurier.
     * @param treasureMap      : la carte aux trésors.
     * @param treasureMapCells : matrice de {@link TreasureMapCell} représentant la carte aux trésors.
     * @param apparitionOrder  : ordre d'apparition de l'aventurier dans le fichier en entrée.
     * @throws ParseAdventurerLineException en cas d'erreur de lecture des informations.
     */
    protected void putAdventurerOnTreasureMapCell(String line,
                                                  TreasureMap treasureMap,
                                                  TreasureMapCell[][] treasureMapCells,
                                                  int apparitionOrder)
            throws ParseAdventurerLineException {
        Adventurer adventurer = adventurerService.createAdventurerFromInputFileLine(line);
        if (adventurer == null) return;

        Position adventurerPosition = adventurer.getPosition();
        if (isPositionNegativeOrOutOfBounds(adventurerPosition, treasureMap)) {
            logWhenPositionIsNegativeOrOutOfBounds(adventurerPosition, treasureMap, "Montagne");
            return;
        }
        if (isPositionNullOrEmptyTakenOnTreasureMap(adventurerPosition, treasureMapCells)) {
            logWhenPositionIsTaken(adventurerPosition, treasureMapCells, "Aventurier");
            return;
        }

        TreasureMapCell treasureMapCell = new TreasureMapCell();
        adventurer.setApparitionOrder(apparitionOrder);
        treasureMapCell.setAdventurer(adventurer);

        treasureMapCells[adventurerPosition.getHorizontalPosition()][adventurerPosition.getVerticalPosition()]
                = treasureMapCell;
    }
}
