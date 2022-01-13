package carbon.exercise.treasuremap.service;

import carbon.exercise.treasuremap.exception.ParseLineException;
import carbon.exercise.treasuremap.model.*;
import carbon.exercise.treasuremap.utils.TreasureMapGameUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static carbon.exercise.treasuremap.utils.TreasureMapGameUtils.ERROR_WHEN_READING_DETAILS;
import static carbon.exercise.treasuremap.utils.TreasureMapGameUtils.splitLine;

@Service
public class AdventurerService {

    protected static Adventurer createAdventurerFromInputFileLine(String line) throws ParseLineException {
        String[] adventurerDetails = splitLine(line);
        if (adventurerDetails.length != 6) {
            throw new ParseLineException(ERROR_WHEN_READING_DETAILS +
                    "de l'aventurier : des informations sans manquantes");
        }

        String verticalPosition = adventurerDetails[2];
        String horizontalPosition = adventurerDetails[3];
        TreasureMapGameUtils.checkPositionFromInputFileType(horizontalPosition, verticalPosition);

        return new Adventurer(
                adventurerDetails[1],
                new Position(Integer.parseInt(horizontalPosition),
                        Integer.parseInt(verticalPosition)),
                getAdventurerOrientation(adventurerDetails[4]),
                getAdventurerMovements(adventurerDetails[5].toCharArray()),
                0
        );
    }

    private static Orientation getAdventurerOrientation(String adventurerOrientation)
            throws ParseLineException {
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
        throw new ParseLineException(
                TreasureMapGameUtils.ERROR_WHEN_READING_DETAILS +
                        "de l'aventurier : les orientations possibles sont N, S, E et W uniquement.");
    }

    private static List<Movement> getAdventurerMovements(char[] adventurerMovements)
            throws ParseLineException {
        List<Movement> movements = new ArrayList<>();
        for (char movement : adventurerMovements) {
            switch (movement) {
                case 'A' -> movements.add(Movement.FORWARD);
                case 'D' -> movements.add(Movement.TURN_RIGHT);
                case 'G' -> movements.add(Movement.TURN_LEFT);
                default -> throw new ParseLineException(
                        TreasureMapGameUtils.ERROR_WHEN_READING_DETAILS +
                                "de l'aventurier : les mouvements possibles sont A, D et G uniquement.");
            }
        }
        return movements;
    }

    private static Position getNextAdventurerPosition(Adventurer adventurer) {
        Position currentAdventurerPosition = adventurer.getPosition();
        Position nextPosition = new Position();

        switch (adventurer.getOrientation()) {
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

    private static boolean isNextPositionOutOfBounds(Position position, TreasureMap treasureMap) {
        return position.getHorizontalPosition() >= treasureMap.getRowCount()
                || position.getVerticalPosition() >= treasureMap.getColumnCount();
    }

    /**protected static void processAdventurerMovements(Adventurer adventurer){

     }*/
}
