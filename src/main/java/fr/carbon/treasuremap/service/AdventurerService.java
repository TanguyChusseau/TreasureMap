package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseAdventurerLineException;
import fr.carbon.treasuremap.exception.ParseLineException;
import fr.carbon.treasuremap.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static fr.carbon.treasuremap.utils.TreasureMapGameUtils.*;

@Service
public class AdventurerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventurerService.class);

    /**
     * Créé un nouvel aventurier {@link Adventurer} à partir des informations lues dans le fichier en entrée.
     *
     * @param line : la ligne correspondante dans le fichier.
     * @return l'aventurier construit.
     * @throws ParseAdventurerLineException en cas d'erreur de lecture des informations de l'aventurier dans le fichier.
     */
    protected Adventurer createAdventurerFromInputFileLine(String line) throws ParseAdventurerLineException {
        String[] adventurerDetails = splitLine(line);
        if (adventurerDetails.length < 6) {
            throw new ParseAdventurerLineException(ERROR_WHEN_READING_DETAILS +
                    "de l'aventurier : des informations sans manquantes.");
        }

        String horizontalPosition = adventurerDetails[2];
        String verticalPosition = adventurerDetails[3];
        try {
            checkPositionFromInputFileType(horizontalPosition, verticalPosition);
        } catch (ParseLineException ple) {
            throw new ParseAdventurerLineException(ple.getMessage());
        }

        return new Adventurer(
                adventurerDetails[1],
                new Position(Integer.parseInt(horizontalPosition),
                        Integer.parseInt(verticalPosition)),
                getAdventurerOrientation(adventurerDetails[4]),
                getAdventurerMovements(adventurerDetails[5].toCharArray()),
                0,
                0
        );
    }

    /**
     * Pour chaque aventurier présent sur la carte aux trésors, applique les différentes déplacements {@link Movement}.
     *
     * @param treasureMap : la carte aux trésors {@link TreasureMap}.
     * @return la carte aux trésors mise à jour.
     */
    public TreasureMap moveAdventurersOnTreasureMap(TreasureMap treasureMap) {
        List<Adventurer> adventurers = getAdventurersFromTreasureMap(treasureMap);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        for (Adventurer adventurer : adventurers) {
            for (Movement movement : adventurer.getMovements()) {
                switch (movement) {
                    case FORWARD -> {
                        Position currentPosition = adventurer.getPosition();
                        Position nextPosition = getAdventurerNextPosition(currentPosition, adventurer.getOrientation());
                        if (isNextPositionUnreachable(nextPosition, treasureMap)) {
                            logWhenNextPositionIsUnreachable(adventurer, nextPosition, movement, treasureMap);
                            break;
                        }

                        updateAdventurerCollectedTreasures(treasureMap, adventurer, nextPosition);
                        adventurer.setPosition(nextPosition);

                        treasureMapCells[nextPosition.getHorizontalPosition()][nextPosition.getVerticalPosition()]
                                .setAdventurer(adventurer);

                        treasureMapCells[currentPosition.getHorizontalPosition()][nextPosition.getVerticalPosition()]
                                .setAdventurer(null);
                    }
                    case TURN_RIGHT -> adventurer.setOrientation(
                            getNextOrientationAfterRightTurn(adventurer.getOrientation())
                    );
                    case TURN_LEFT -> adventurer.setOrientation(
                            getNextOrientationAfterLeftTurn(adventurer.getOrientation())
                    );
                }
            }
        }
        return treasureMap;
    }

    /**
     * Vérifie si la prochaine position d'un {@link Adventurer} sur la carte aux trésors correspond à un trésor.
     * Si oui, incrémente le nombre de trésors collectés de l'aventurier et décrémente le nombre de trésors du trésor.
     *
     * @param treasureMap  : la carte aux trésors {@link TreasureMap}.
     * @param adventurer   : l'aventurier concerné.
     * @param nextPosition : la prochaine prosition de l'aventurier selon les éventuels obstacles.
     */
    protected void updateAdventurerCollectedTreasures(TreasureMap treasureMap, Adventurer adventurer, Position nextPosition) {
        if (isNextPositionOnTreasure(nextPosition, treasureMap.getTreasureMapCells())) {
            Treasure treasure = treasureMap.getTreasureMapCells()[nextPosition.getHorizontalPosition()]
                    [nextPosition.getVerticalPosition()].getTreasure();
            int count = treasure.getCount();
            if (count > 0) {
                adventurer.setCollectedTreasuresCount(adventurer.getCollectedTreasuresCount() + 1);
                treasure.setCount(treasure.getCount() - 1);
            }
        }
    }

    protected List<Adventurer> getAdventurersFromTreasureMap(TreasureMap treasureMap) {
        List<Adventurer> adventurers = new ArrayList<>();
        if (treasureMap == null || treasureMap.getTreasureMapCells() == null) return adventurers;
        for (TreasureMapCell[] treasureMapCells : treasureMap.getTreasureMapCells()) {
            for (TreasureMapCell treasureMapCell : treasureMapCells) {
                if (treasureMapCell.getAdventurer() != null) {
                    adventurers.add(treasureMapCell.getAdventurer());
                }
            }
        }
        return adventurers.stream().sorted(Comparator.comparing(Adventurer::getApparitionOrder)).toList();
    }

    /**
     * Récupère l'orientation actuelle de l'aventurier {@link Adventurer} à partir du fichier en entrée.
     *
     * @param adventurerOrientation l'orientation de l'aventurier dans le fichier.
     * @return l'orientation {@link Orientation} de l'aventurier.
     * @throws ParseAdventurerLineException en cas d'erreur de lecture des informations de l'aventurier dans le fichier.
     */
    protected Orientation getAdventurerOrientation(String adventurerOrientation)
            throws ParseAdventurerLineException {
        switch (adventurerOrientation) {
            case "N":
                return Orientation.NORTH;
            case "S":
                return Orientation.SOUTH;
            case "E":
                return Orientation.EAST;
            case "W":
                return Orientation.WEST;
        }
        throw new ParseAdventurerLineException(ERROR_WHEN_READING_DETAILS +
                "de l'aventurier : les orientations possibles sont N, S, E et W uniquement.");
    }

    /**
     * Récupère la liste des {@link Movement} à effectuer d'un {@link Adventurer}.
     *
     * @param adventurerMovements : la liste des mouvements dans le fichier en entrée.
     * @return la liste des mouvements.
     * @throws ParseAdventurerLineException en cas d'erreur de lecture des informations de l'aventurier dans le fichier.
     */
    protected List<Movement> getAdventurerMovements(char[] adventurerMovements)
            throws ParseAdventurerLineException {
        List<Movement> movements = new ArrayList<>();
        for (char movement : adventurerMovements) {
            switch (movement) {
                case 'A' -> movements.add(Movement.FORWARD);
                case 'D' -> movements.add(Movement.TURN_RIGHT);
                case 'G' -> movements.add(Movement.TURN_LEFT);
                default -> throw new ParseAdventurerLineException(ERROR_WHEN_READING_DETAILS +
                        "de l'aventurier : les mouvements possibles sont A, D et G uniquement.");
            }
        }
        return movements;
    }

    /**
     * Déduit la prochaine {@link Position} d'un aventurier en fonction de sa position et de son orientation actuelles.
     *
     * @param currentAdventurerPosition : la position actuelle de l'aventurier.
     * @param adventurerOrientation     : l'orienation de l'aventurier.
     * @return : la prochaine position de l'aventurier
     */
    protected Position getAdventurerNextPosition(Position currentAdventurerPosition, Orientation adventurerOrientation) {
        Position nextPosition = new Position();

        switch (adventurerOrientation) {
            case NORTH -> {
                nextPosition.setHorizontalPosition(currentAdventurerPosition.getHorizontalPosition());
                nextPosition.setVerticalPosition(currentAdventurerPosition.getVerticalPosition() - 1);
            }
            case SOUTH -> {
                nextPosition.setHorizontalPosition(currentAdventurerPosition.getHorizontalPosition());
                nextPosition.setVerticalPosition(currentAdventurerPosition.getVerticalPosition() + 1);
            }
            case EAST -> {
                nextPosition.setHorizontalPosition(currentAdventurerPosition.getHorizontalPosition() + 1);
                nextPosition.setVerticalPosition(currentAdventurerPosition.getVerticalPosition());
            }
            case WEST -> {
                nextPosition.setHorizontalPosition(currentAdventurerPosition.getHorizontalPosition() - 1);
                nextPosition.setVerticalPosition(currentAdventurerPosition.getVerticalPosition());
            }
        }
        return nextPosition;
    }

    protected Orientation getNextOrientationAfterRightTurn(Orientation orientation) {
        Orientation nextOrientation = orientation;
        switch (orientation) {
            case NORTH -> nextOrientation = Orientation.EAST;
            case SOUTH -> nextOrientation = Orientation.WEST;
            case EAST -> nextOrientation = Orientation.SOUTH;
            case WEST -> nextOrientation = Orientation.NORTH;
        }
        return nextOrientation;
    }

    protected Orientation getNextOrientationAfterLeftTurn(Orientation orientation) {
        Orientation nextOrientation = orientation;
        switch (orientation) {
            case NORTH -> nextOrientation = Orientation.WEST;
            case SOUTH -> nextOrientation = Orientation.EAST;
            case EAST -> nextOrientation = Orientation.NORTH;
            case WEST -> nextOrientation = Orientation.SOUTH;
        }
        return nextOrientation;
    }

    protected boolean isNextPositionNegativeOrOutOfBounds(Position nextPosition, TreasureMap treasureMap) {
        return nextPosition.getHorizontalPosition() < 0
                || nextPosition.getHorizontalPosition() >= treasureMap.getColumnCount()
                || nextPosition.getVerticalPosition() < 0
                || nextPosition.getVerticalPosition() >= treasureMap.getRowCount();
    }

    protected boolean isNextPositionOnMoutain(Position nextPosition, TreasureMapCell[][] treasureMapCells) {
        return treasureMapCells[nextPosition.getHorizontalPosition()][nextPosition.getVerticalPosition()]
                .getMountain() != null;
    }

    protected boolean isNextPositionOnTreasure(Position nextPosition, TreasureMapCell[][] treasureMapCells) {
        return treasureMapCells[nextPosition.getHorizontalPosition()][nextPosition.getVerticalPosition()]
                .getTreasure() != null;

    }

    protected boolean isNextPositionOnAdventurer(Position nextPosition, TreasureMapCell[][] treasureMapCells) {
        return treasureMapCells[nextPosition.getHorizontalPosition()][nextPosition.getVerticalPosition()]
                .getAdventurer() != null;
    }

    protected boolean isNextPositionUnreachable(Position position, TreasureMap treasureMap) {
        return isNextPositionNegativeOrOutOfBounds(position, treasureMap)
                || isNextPositionOnMoutain(position, treasureMap.getTreasureMapCells())
                || isNextPositionOnAdventurer(position, treasureMap.getTreasureMapCells());
    }

    private void logWhenNextPositionIsUnreachable(Adventurer adventurer,
                                                  Position nextPosition,
                                                  Movement movement,
                                                  TreasureMap treasureMap) {
        if (isNextPositionNegativeOrOutOfBounds(nextPosition, treasureMap)) {
            LOGGER.warn("Hors limites. Le déplacement " + movement.getValue() + " de l'aventurier : "
                    + adventurer.getName() + " est ignoré.");
            return;
        }
        if (isNextPositionOnMoutain(nextPosition, treasureMap.getTreasureMapCells())) {
            LOGGER.warn("Une montagne bloque l'aventurier : " + adventurer.getName() +
                    ". Le déplacement " + movement.getValue() + " est ignoré.");
        }
    }
}
