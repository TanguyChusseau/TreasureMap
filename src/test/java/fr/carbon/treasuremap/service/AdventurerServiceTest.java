package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseAdventurerLineException;
import fr.carbon.treasuremap.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdventurerServiceTest {

    private final AdventurerService adventurerService = new AdventurerService();

    @Test
    public void should_update_treasure_map() {
        //Given
        TreasureMap treasureMap = new TreasureMap(3, 3);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Mountain mountain = new Mountain(new Position(1, 1));
        Treasure firstTreasure = new Treasure(new Position(0, 2), 1);
        Treasure secondTreasure = new Treasure(new Position(2, 1), 2);

        Adventurer firstAdventurer = new Adventurer(
                "Jeanne",
                new Position(0, 0),
                Orientation.SOUTH,
                List.of(Movement.FORWARD, Movement.FORWARD, Movement.TURN_LEFT, Movement.TURN_LEFT, Movement.FORWARD),
                0,
                0
        );
        Adventurer secondAdventurer = new Adventurer(
                "Hugues",
                new Position(2, 0),
                Orientation.SOUTH,
                List.of(Movement.FORWARD, Movement.FORWARD, Movement.FORWARD,
                        Movement.TURN_RIGHT, Movement.FORWARD, Movement.FORWARD),
                0,
                1
        );
        Adventurer thirdAdventurer = new Adventurer(
                "Pascal",
                new Position(1, 0),
                Orientation.SOUTH,
                List.of(Movement.FORWARD),
                0,
                2
        );

        treasureMapCells[1][1].setMountain(mountain);

        treasureMapCells[0][2].setTreasure(firstTreasure);
        treasureMapCells[2][1].setTreasure(secondTreasure);

        treasureMapCells[0][0].setAdventurer(firstAdventurer);
        treasureMapCells[2][0].setAdventurer(secondAdventurer);
        treasureMapCells[1][0].setAdventurer(thirdAdventurer);

        //When
        adventurerService.moveAdventurersOnTreasureMap(treasureMap);

        //Then
        assertEquals(1, firstAdventurer.getCollectedTreasuresCount());
        assertEquals(0, firstAdventurer.getPosition().getHorizontalPosition());
        assertEquals(1, firstAdventurer.getPosition().getVerticalPosition());
        assertEquals(Orientation.NORTH, firstAdventurer.getOrientation());

        assertEquals(1, secondAdventurer.getCollectedTreasuresCount());
        assertEquals(0, secondAdventurer.getPosition().getHorizontalPosition());
        assertEquals(2, secondAdventurer.getPosition().getVerticalPosition());

        assertEquals(0, thirdAdventurer.getCollectedTreasuresCount());
        assertEquals(1, thirdAdventurer.getPosition().getHorizontalPosition());
        assertEquals(0, thirdAdventurer.getPosition().getVerticalPosition());
        assertEquals(Orientation.SOUTH, thirdAdventurer.getOrientation());

        assertEquals(0, firstTreasure.getCount());
        assertEquals(1, secondTreasure.getCount());
    }

    @Test
    public void should_update_adventurer_collected_treasures_count() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();
        Adventurer adventurer = new Adventurer(
                "Jean",
                new Position(0, 0),
                Orientation.SOUTH,
                List.of(Movement.FORWARD),
                1,
                1
        );
        Treasure treasure = new Treasure(new Position(0, 1), 1);
        Position nextAdventurerPosition = treasure.getPosition();
        treasureMapCells[0][1].setTreasure(treasure);

        //When
        adventurerService.updateAdventurerCollectedTreasures(treasureMap, adventurer, nextAdventurerPosition);

        //Then
        assertEquals(2, adventurer.getCollectedTreasuresCount());
        assertEquals(0, treasure.getCount());
    }

    @Test
    public void should_get_adventurers_sorted_by_apparition_order_from_treasure_map() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();
        List<Adventurer> adventurers;

        Adventurer firstAdventurer = new Adventurer(
                "Bob",
                new Position(0, 1),
                Orientation.EAST,
                List.of(Movement.FORWARD, Movement.TURN_RIGHT),
                3,
                2
        );
        Adventurer secondAdventurer = new Adventurer(
                "John",
                new Position(1, 1),
                Orientation.WEST,
                List.of(Movement.FORWARD, Movement.TURN_LEFT),
                3,
                1
        );

        treasureMapCells[0][0].setAdventurer(firstAdventurer);
        treasureMapCells[1][1].setAdventurer(secondAdventurer);

        //When
        adventurers = adventurerService.getAdventurersFromTreasureMap(treasureMap);

        //Then
        assertEquals("John", adventurers.get(0).getName());
        assertEquals(1, adventurers.get(0).getApparitionOrder());
        assertEquals("Bob", adventurers.get(1).getName());
        assertEquals(2, adventurers.get(1).getApparitionOrder());
    }

    @Test
    public void should_create_adventurer_from_input_file_line() throws ParseAdventurerLineException {
        //Given
        String line = "A - Arnold - 0 - 0 - E - ADA";
        List<Movement> movements = List.of(Movement.FORWARD, Movement.TURN_RIGHT, Movement.FORWARD);

        //When
        Adventurer adventurer = adventurerService.createAdventurerFromInputFileLine(line);

        //Then
        assertEquals("Arnold", adventurer.getName());
        assertEquals(0, adventurer.getPosition().getHorizontalPosition());
        assertEquals(0, adventurer.getPosition().getVerticalPosition());
        assertEquals(Orientation.EAST, adventurer.getOrientation());
        assertEquals(movements.get(0), adventurer.getMovements().get(0));
        assertEquals(movements.get(1), adventurer.getMovements().get(1));
        assertEquals(movements.get(2), adventurer.getMovements().get(2));
    }

    @Test
    public void should_throw_when_not_enough_adventurer_details_from_input_file_line() {
        //Given
        String invalidLine = "A - Arnold - 1 - 1";

        //When - Then
        assertThrows(ParseAdventurerLineException.class,
                () -> adventurerService.createAdventurerFromInputFileLine(invalidLine)
        );
    }

    @Test
    public void should_throw_when_adventurer_position_is_not_numeric() {
        //Given
        String invalidLine = "A - Arnold - 0P - 0 - E - ADA";

        //When - Then
        assertThrows(ParseAdventurerLineException.class,
                () -> adventurerService.createAdventurerFromInputFileLine(invalidLine)
        );
    }

    @Test
    public void should_get_adventurer_orientation_from_string() throws ParseAdventurerLineException {
        //Given
        String orientationNorth = "N";
        String orientationSouth = "S";
        String orientationEast = "E";
        String orientationWest = "W";

        //When
        Orientation orientationNorthFromString = adventurerService.getAdventurerOrientation(orientationNorth);
        Orientation orientationSouthFromString = adventurerService.getAdventurerOrientation(orientationSouth);
        Orientation orientationEastFromString = adventurerService.getAdventurerOrientation(orientationEast);
        Orientation orientationWestFromString = adventurerService.getAdventurerOrientation(orientationWest);

        ///Then
        assertEquals(orientationNorth, orientationNorthFromString.getValue());
        assertEquals(orientationSouth, orientationSouthFromString.getValue());
        assertEquals(orientationEast, orientationEastFromString.getValue());
        assertEquals(orientationWest, orientationWestFromString.getValue());
    }

    @Test
    public void should_throw_when_adventurer_orientation_is_not_valid() {
        //Given
        String invalidOrientation = "P";

        //When - Then
        assertThrows(ParseAdventurerLineException.class,
                () -> adventurerService.getAdventurerOrientation(invalidOrientation))
        ;
    }

    @Test
    public void should_get_adventurer_movements_from_chars() throws ParseAdventurerLineException {
        //Given
        char[] movementChars = {'A', 'D', 'G'};

        //When
        List<Movement> movementsFromLine = adventurerService.getAdventurerMovements(movementChars);

        //Then
        assertEquals(Movement.FORWARD, movementsFromLine.get(0));
        assertEquals(Movement.TURN_RIGHT, movementsFromLine.get(1));
        assertEquals(Movement.TURN_LEFT, movementsFromLine.get(2));
    }

    @Test
    public void should_throw_when_adventurer_movements_are_not_valid() {
        //Given
        char[] invalidMovementChars = {'A', 'P', 'R'};

        //When - Then
        assertThrows(ParseAdventurerLineException.class,
                () -> adventurerService.getAdventurerMovements(invalidMovementChars))
        ;
    }

    @Test
    public void should_get_adventurer_next_position() {
        //Given
        Position adventurerCurrentPositon = new Position(1, 1);
        Orientation adventurerOrientationNorth = Orientation.NORTH;
        Orientation adventurerOrientationSouth = Orientation.SOUTH;
        Orientation adventurerOrientationEast = Orientation.EAST;
        Orientation adventurerOrientationWest = Orientation.WEST;

        //When - Then
        assertEquals(1, adventurerService
                .getAdventurerNextPosition(adventurerCurrentPositon, adventurerOrientationNorth).getHorizontalPosition()
        );
        assertEquals(0, adventurerService
                .getAdventurerNextPosition(adventurerCurrentPositon, adventurerOrientationNorth).getVerticalPosition()
        );
        assertEquals(1, adventurerService
                .getAdventurerNextPosition(adventurerCurrentPositon, adventurerOrientationSouth).getHorizontalPosition()
        );
        assertEquals(2, adventurerService
                .getAdventurerNextPosition(adventurerCurrentPositon, adventurerOrientationSouth).getVerticalPosition()
        );
        assertEquals(2, adventurerService
                .getAdventurerNextPosition(adventurerCurrentPositon, adventurerOrientationEast).getHorizontalPosition()
        );
        assertEquals(1, adventurerService
                .getAdventurerNextPosition(adventurerCurrentPositon, adventurerOrientationEast).getVerticalPosition()
        );
        assertEquals(0, adventurerService
                .getAdventurerNextPosition(adventurerCurrentPositon, adventurerOrientationWest).getHorizontalPosition()
        );
        assertEquals(1, adventurerService
                .getAdventurerNextPosition(adventurerCurrentPositon, adventurerOrientationWest).getVerticalPosition()
        );
    }

    @Test
    public void should_get_next_position_after_right_turn() {
        //Given
        Orientation orientationNorth = Orientation.NORTH;
        Orientation orientationSouth = Orientation.SOUTH;
        Orientation orientationEast = Orientation.EAST;
        Orientation orientationWest = Orientation.WEST;

        //When - Then
        assertEquals(Orientation.EAST, adventurerService.getNextOrientationAfterRightTurn(orientationNorth));
        assertEquals(Orientation.WEST, adventurerService.getNextOrientationAfterRightTurn(orientationSouth));
        assertEquals(Orientation.SOUTH, adventurerService.getNextOrientationAfterRightTurn(orientationEast));
        assertEquals(Orientation.NORTH, adventurerService.getNextOrientationAfterRightTurn(orientationWest));
    }

    @Test
    public void should_get_next_position_after_left_turn() {
        //Given
        Orientation orientationNorth = Orientation.NORTH;
        Orientation orientationSouth = Orientation.SOUTH;
        Orientation orientationEast = Orientation.EAST;
        Orientation orientationWest = Orientation.WEST;

        //When - Then
        assertEquals(Orientation.WEST, adventurerService.getNextOrientationAfterLeftTurn(orientationNorth));
        assertEquals(Orientation.EAST, adventurerService.getNextOrientationAfterLeftTurn(orientationSouth));
        assertEquals(Orientation.NORTH, adventurerService.getNextOrientationAfterLeftTurn(orientationEast));
        assertEquals(Orientation.SOUTH, adventurerService.getNextOrientationAfterLeftTurn(orientationWest));
    }

    @Test
    public void should_return_true_when_next_position_is_out_of_bounds() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        Position outOfBoundsPosition = new Position(2, 2);
        Position negativePosition = new Position(2, -1);

        //When - Then
        assertTrue(adventurerService.isNextPositionNegativeOrOutOfBounds(outOfBoundsPosition, treasureMap));
        assertTrue(adventurerService.isNextPositionNegativeOrOutOfBounds(negativePosition, treasureMap));
    }

    @Test
    public void should_return_true_when_next_position_is_on_mountain() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        Position onMountainPosition = new Position(1, 1);
        Mountain mountain = new Mountain(new Position(1, 1));
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();
        treasureMapCells[1][1].setMountain(mountain);

        //When - Then
        assertTrue(adventurerService.isNextPositionOnMoutain(onMountainPosition, treasureMapCells));
    }

    @Test
    public void should_return_true_when_next_position_is_on_treasure() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        Position onTreasurePosition = new Position(1, 1);
        Treasure treasure = new Treasure(new Position(1, 1), 2);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();
        treasureMapCells[1][1].setTreasure(treasure);

        //When - Then
        assertTrue(adventurerService.isNextPositionOnTreasure(onTreasurePosition, treasureMapCells));
    }

    @Test
    public void should_return_true_when_next_position_is_on_adventurer() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        Position onAdventurerPosition = new Position(1, 1);
        Adventurer adventurer = new Adventurer(
                "Test",
                new Position(1, 1),
                Orientation.EAST,
                new ArrayList<>(),
                1,
                1
        );
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();
        treasureMapCells[1][1].setAdventurer(adventurer);

        //When - Then
        assertTrue(adventurerService.isNextPositionOnAdventurer(onAdventurerPosition, treasureMapCells));
    }

    @Test
    public void should_return_true_when_next_position_is_unreachable() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Position outOfBoundsPosition = new Position(2, 2);
        Position negativePosition = new Position(2, -1);
        Position onMountainPosition = new Position(0, 0);
        Position onAdventurerPosition = new Position(1, 1);

        Mountain mountain = new Mountain(onMountainPosition);
        Adventurer adventurer = new Adventurer(
                "Patrick",
                onAdventurerPosition,
                Orientation.NORTH,
                List.of(Movement.FORWARD),
                1,
                1
        );

        treasureMapCells[0][0].setMountain(mountain);
        treasureMapCells[1][1].setAdventurer(adventurer);

        //When - Then
        assertTrue(adventurerService.isNextPositionUnreachable(outOfBoundsPosition, treasureMap));
        assertTrue(adventurerService.isNextPositionUnreachable(negativePosition, treasureMap));
        assertTrue(adventurerService.isNextPositionUnreachable(onMountainPosition, treasureMap));
        assertTrue(adventurerService.isNextPositionUnreachable(onAdventurerPosition, treasureMap));
    }

    @Test
    public void should_return_false_when_next_position_is_reachable() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        Position reachablePosition = new Position(1, 1);

        //When - Then
        assertFalse(adventurerService.isNextPositionUnreachable(reachablePosition, treasureMap));
    }
}
