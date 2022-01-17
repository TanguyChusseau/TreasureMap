package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.*;
import fr.carbon.treasuremap.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TreasureMapServiceTest {

    private final TreasureMapService treasureMapService =
            new TreasureMapService(new AdventurerService(), new MountainService(), new TreasureService());

    @Test
    public void should_create_treasure_map_from_line() throws ParseLineException {
        //Given
        String treasureMapLine = "C - 3 - 3";
        String mountainLine = "M - 0 - 0";
        String treasureLine = "T - 0 - 1 - 2";
        String adventurerLine = "A - Sarah - 1 - 1 - E - DAD";
        List<String> fileLinesTest = List.of(treasureMapLine, mountainLine, treasureLine, adventurerLine);

        List<Movement> expectedAdventurerMovements = List.of(Movement.TURN_RIGHT, Movement.FORWARD, Movement.TURN_RIGHT);

        Mountain expectedMountain = new Mountain(new Position(0, 0));
        Treasure expectedTreasure = new Treasure(new Position(0, 1), 2);
        Adventurer expectedAdventurer = new Adventurer("Sarah",
                new Position(1, 1),
                Orientation.EAST,
                expectedAdventurerMovements,
                0,
                1
        );

        //When
        TreasureMap createdTreasureMap = treasureMapService.createTreasureMapFromInputFile(fileLinesTest);
        TreasureMapCell[][] createdTreasureMapCells = createdTreasureMap.getTreasureMapCells();

        Mountain createdMountain = createdTreasureMapCells[0][0].getMountain();
        Treasure createdTreasure = createdTreasureMapCells[0][1].getTreasure();
        Adventurer createdAdventurer = createdTreasureMapCells[1][1].getAdventurer();

        //Then
        assertEquals(expectedMountain.getPosition().getHorizontalPosition(),
                createdMountain.getPosition().getHorizontalPosition()
        );
        assertEquals(expectedMountain.getPosition().getVerticalPosition(),
                createdMountain.getPosition().getVerticalPosition()
        );
        assertEquals(expectedTreasure.getPosition().getHorizontalPosition(),
                createdTreasure.getPosition().getHorizontalPosition()
        );
        assertEquals(expectedTreasure.getPosition().getVerticalPosition(),
                createdTreasure.getPosition().getVerticalPosition()
        );
        assertEquals(expectedTreasure.getCount(), createdTreasure.getCount());
        assertEquals(expectedAdventurer.getName(), createdAdventurer.getName());
        assertEquals(expectedAdventurer.getOrientation(), createdAdventurer.getOrientation());
        assertEquals(expectedAdventurer.getMovements(), createdAdventurer.getMovements());
    }

    @Test
    public void should_throw_when_first_line_is_invalid() {
        //Given
        String invalidTreasureMapLine = "B - 2 - 3";
        String mountainLine = "M - 0 - 0";
        String treasureLine = "T - 0 - 1";
        List<String> fileLines = List.of(invalidTreasureMapLine, mountainLine, treasureLine);

        //When - Then
        assertThrows(ParseLineException.class, () -> treasureMapService.createTreasureMapFromInputFile(fileLines));
    }

    @Test
    public void should_throw_when_file_content_is_invalid() {
        //Given
        String treasureMapLine = "C - 2 - 3";
        String mountainLine = "M - 0 - 0";
        String invalidLine = "Z - 0 - 1";
        List<String> fileLines = List.of(treasureMapLine, mountainLine, invalidLine);

        //When - Then
        assertThrows(ParseLineException.class, () -> treasureMapService.createTreasureMapFromInputFile(fileLines));
    }

    @Test
    public void should_retrieve_treasure_map_details_from_line() throws ParseLineException {
        //Given
        String treasureMapLine = "C - 2 - 3";
        TreasureMap treasureMap = new TreasureMap(2, 3);

        //When
        TreasureMap createdTreasureMap = treasureMapService.getTreasureMapDetailsFromInputFileLine(treasureMapLine);

        //Then
        assertEquals(treasureMap.getColumnCount(), createdTreasureMap.getColumnCount());
        assertEquals(treasureMap.getRowCount(), createdTreasureMap.getRowCount());
    }

    @Test
    public void should_throw_when_not_enough_details_from_line() {
        //Given
        String treasureMapLine = "C - 2 -";

        //When - Then
        assertThrows(ParseTreasureMapLineException.class,
                () -> treasureMapService.getTreasureMapDetailsFromInputFileLine(treasureMapLine)
        );
    }

    @Test
    public void should_throw_when_treasure_map_dimensions_are_not_numeric() {
        //Given
        String treasureMapLine = "C - 2 - G";

        //When - Then
        assertThrows(ParseTreasureMapLineException.class,
                () -> treasureMapService.getTreasureMapDetailsFromInputFileLine(treasureMapLine)
        );
    }

    @Test
    public void should_return_true_when_position_is_negative_or_out_of_bounds() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        Position outOfBoundsPosition = new Position(2, 2);
        Position negativePosition = new Position(-1, 2);

        //When - Then
        assertTrue(treasureMapService.isPositionNegativeOrOutOfBounds(outOfBoundsPosition, treasureMap));
        assertTrue(treasureMapService.isPositionNegativeOrOutOfBounds(negativePosition, treasureMap));
    }

    @Test
    public void should_return_true_when_position_is_null() {
        //Given
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        Position position = new Position(0, 0);

        //When - Then
        assertTrue(treasureMapService.isPositionNullOrEmptyTakenOnTreasureMap(position, treasureMapCells));
    }

    @Test
    public void should_return_true_when_position_is_taken() {
        //Given
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Position position = new Position(0, 0);
        treasureMapCells[0][0].setMountain(new Mountain(position));

        //When - Then
        assertTrue(treasureMapService.isPositionNullOrEmptyTakenOnTreasureMap(position, treasureMapCells));
    }

    @Test
    public void should_put_mountain_on_treasure_map() throws ParseMountainLineException {
        //Given
        String testLine = "M - 0 - 0";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Mountain testMountain = new Mountain(new Position(0, 0));

        //When
        treasureMapService.putMountainOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Mountain createdMountain = treasureMapCells[0][0].getMountain();

        //Then
        assertEquals(testMountain.getPosition().getHorizontalPosition(),
                createdMountain.getPosition().getHorizontalPosition()
        );
        assertEquals(testMountain.getPosition().getVerticalPosition(),
                createdMountain.getPosition().getVerticalPosition()
        );
    }

    @Test
    public void should_not_put_mountain_on_treasure_map_when_position_is_out_of_bounds() throws ParseMountainLineException {
        //Given
        String line = "M - 1 - 1";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Treasure treasure = new Treasure(new Position(0, 0), 0);
        treasureMapCells[0][0].setTreasure(treasure);

        //When
        treasureMapService.putMountainOnTreasureMapCell(line, treasureMap, treasureMapCells);
        Mountain createdMountain = treasureMapCells[0][0].getMountain();

        //Then
        assertNull(createdMountain);
    }

    @Test
    public void should_not_put_mountain_on_treasure_map_when_position_is_taken() throws ParseMountainLineException {
        //Given
        String line = "M - 0 - 0";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Treasure treasure = new Treasure(new Position(0, 0), 0);
        treasureMapCells[0][0].setTreasure(treasure);

        //When
        treasureMapService.putMountainOnTreasureMapCell(line, treasureMap, treasureMapCells);
        Mountain createdMountain = treasureMapCells[0][0].getMountain();

        //Then
        assertNull(createdMountain);
    }

    @Test
    public void should_put_treasure_on_treasure_map() throws ParseTreasureLineException {
        //Given
        String line = "T - 0 - 0 - 0";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();
        Treasure treasure = new Treasure(new Position(0, 0), 0);

        //When
        treasureMapService.putTreasureOnTreasureMapCell(line, treasureMap, treasureMapCells);
        Treasure createdTreasure = treasureMapCells[0][0].getTreasure();

        //Then
        assertEquals(treasure.getPosition().getHorizontalPosition(),
                createdTreasure.getPosition().getHorizontalPosition()
        );
        assertEquals(treasure.getPosition().getVerticalPosition(),
                createdTreasure.getPosition().getVerticalPosition()
        );
        assertEquals(treasure.getCount(), createdTreasure.getCount());
    }

    @Test
    public void should_not_put_treasure_on_treasure_map_when_position_is_out_of_bounds() throws ParseTreasureLineException {
        //Given
        String line = "T - 1 - 1 - 0";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Mountain mountain = new Mountain(new Position(0, 0));
        treasureMapCells[0][0].setMountain(mountain);

        //When
        treasureMapService.putTreasureOnTreasureMapCell(line, treasureMap, treasureMapCells);
        Treasure createdTreasure = treasureMapCells[0][0].getTreasure();

        //Then
        assertNull(createdTreasure);
    }

    @Test
    public void should_not_put_treasure_on_treasure_map_when_position_is_taken() throws ParseTreasureLineException {
        //Given
        String testLine = "T - 0 - 0 - 0";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Mountain mountain = new Mountain(new Position(0, 0));
        treasureMapCells[0][0].setMountain(mountain);

        //When
        treasureMapService.putTreasureOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Treasure createdTreasure = treasureMapCells[0][0].getTreasure();

        //Then
        assertNull(createdTreasure);
    }

    @Test
    public void should_put_adventurer_on_treasure_map() throws ParseAdventurerLineException {
        //Given
        String line = "A - Antoine - 0 - 0 - S - ADG - 0";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Adventurer adventurer = new Adventurer(
                "Antoine",
                new Position(0, 0),
                Orientation.SOUTH,
                List.of(Movement.FORWARD, Movement.TURN_RIGHT, Movement.TURN_LEFT),
                0,
                1
        );

        //When
        treasureMapService.putAdventurerOnTreasureMapCell(line, treasureMap, treasureMapCells, 1);
        Adventurer createdAdventurer = treasureMapCells[0][0].getAdventurer();

        //Then
        assertEquals(adventurer.getName(), createdAdventurer.getName());
        assertEquals(adventurer.getPosition().getHorizontalPosition(),
                createdAdventurer.getPosition().getVerticalPosition());
        assertEquals(adventurer.getOrientation(), createdAdventurer.getOrientation());
        assertEquals(adventurer.getMovements(), createdAdventurer.getMovements());
        assertEquals(adventurer.getCollectedTreasuresCount(), createdAdventurer.getCollectedTreasuresCount());
    }

    @Test
    public void should_not_put_adventurer_on_treasure_map_when_position_is_out_of_bounds() throws ParseAdventurerLineException {
        //Given
        String line = "A - Antoine - 1 - 1 - S - ADG - 0";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Mountain mountain = new Mountain(new Position(0, 0));
        treasureMapCells[0][0].setMountain(mountain);

        //When
        treasureMapService.putAdventurerOnTreasureMapCell(line, treasureMap, treasureMapCells, 1);
        Adventurer createdAdventurer = treasureMapCells[0][0].getAdventurer();

        //Then
        assertNull(createdAdventurer);
    }

    @Test
    public void should_not_put_adventurer_on_treasure_map_when_position_is_taken() throws ParseAdventurerLineException {
        //Given
        String line = "A - Antoine - 0 - 0 - S - ADG - 0";
        TreasureMap treasureMap = new TreasureMap(1, 1);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();

        Mountain mountain = new Mountain(new Position(0, 0));
        treasureMapCells[0][0].setMountain(mountain);

        //When
        treasureMapService.putAdventurerOnTreasureMapCell(line, treasureMap, treasureMapCells, 1);
        Adventurer createdAdventurer = treasureMapCells[0][0].getAdventurer();

        //Then
        assertNull(createdAdventurer);
    }
}
