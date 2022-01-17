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
    public void should_create_treasure_map_from_input_file() throws ParseLineException {
        //Given
        String treasureMapLineTest = "C - 3 - 3";
        String mountainLineTest = "M - 0 - 0";
        String treasureLineTest = "T - 0 - 1 - 2";
        String adventurerLineTest = "A - Sarah - 1 - 1 - E - DAD";
        List<String> fileLinesTest = List.of(treasureMapLineTest, mountainLineTest, treasureLineTest, adventurerLineTest);

        List<Movement> expectedAdventurerMovements = List.of(Movement.TURN_RIGHT, Movement.FORWARD, Movement.TURN_RIGHT);

        Mountain expectedMountain = new Mountain(new Position(0, 0));
        Treasure expectedTreasure = new Treasure(new Position(0, 1), 2);
        Adventurer expectedAdventurer = new Adventurer("Sarah",
                new Position(1, 1),
                Orientation.EAST,
                expectedAdventurerMovements,
                0
        );

        //When
        TreasureMap createdTreasureMap = treasureMapService.createTreasureMapFromInputFile(fileLinesTest);
        TreasureMapCell[][] createdTreasureMapCells = createdTreasureMap.getTreasureMapCells();
        TreasureMapCell createdTreasureMapMountainCell = createdTreasureMapCells[0][0];
        TreasureMapCell createdTreasureMapTreasureCell = createdTreasureMapCells[0][1];
        TreasureMapCell createdTreasureMapAdventurerCell = createdTreasureMapCells[1][1];

        Mountain createdMountain = createdTreasureMapMountainCell.getMountain();
        Treasure createdTreasure = createdTreasureMapTreasureCell.getTreasure();
        Adventurer createdAdventurer = createdTreasureMapAdventurerCell.getAdventurer();

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
        String invalidTreasureMapLineTest = "B - 2 - 3";
        String mountainLineTest = "M - 0 - 0";
        String treasureLineTest = "T - 0 - 1";
        List<String> fileLinesTest = List.of(invalidTreasureMapLineTest, mountainLineTest, treasureLineTest);

        //When - Then
        assertThrows(ParseLineException.class, () -> treasureMapService.createTreasureMapFromInputFile(fileLinesTest));
    }

    @Test
    public void should_throw_when_file_content_is_invalid() {
        //Given
        String treasureMapLineTest = "C - 2 - 3";
        String mountainLineTest = "M - 0 - 0";
        String invalidLineTest = "Z - 0 - 1";
        List<String> fileLinesTest = List.of(treasureMapLineTest, mountainLineTest, invalidLineTest);

        //When - Then
        assertThrows(ParseLineException.class, () -> treasureMapService.createTreasureMapFromInputFile(fileLinesTest));
    }

    @Test
    public void should_retrieve_treasure_map_details_from_input_file() throws ParseLineException {
        //Given
        String treasureMapLineTest = "C - 2 - 3";
        TreasureMap treasureMapTest = new TreasureMap(2, 3, new TreasureMapCell[2][3]);

        //When
        TreasureMap createdTreasureMap = treasureMapService.getTreasureMapDetailsFromInputFileLine(treasureMapLineTest);

        //Then
        assertEquals(treasureMapTest.getColumnCount(), createdTreasureMap.getColumnCount());
        assertEquals(treasureMapTest.getRowCount(), createdTreasureMap.getRowCount());
    }

    @Test
    public void should_throw_when_not_enough_details_from_input_file() {
        //Given
        String treasureMapLineTest = "C - 2 -";

        //When - Then
        assertThrows(ParseTreasureMapLineException.class,
                () -> treasureMapService.getTreasureMapDetailsFromInputFileLine(treasureMapLineTest)
        );
    }

    @Test
    public void should_throw_when_treasure_map_size_not_numeric() {
        //Given
        String treasureMapLineTest = "C - 2 - G";

        //When - Then
        assertThrows(ParseTreasureMapLineException.class,
                () -> treasureMapService.getTreasureMapDetailsFromInputFileLine(treasureMapLineTest)
        );
    }

    @Test
    public void should_return_true_when_position_is_out_of_bounds() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2, new TreasureMapCell[2][2]);
        Position position = new Position(2, 2);

        //When - Then
        assertTrue(treasureMapService.isPositionOutOfBounds(position, treasureMap));
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
        //Given,
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        Position position = new Position(0, 0);
        treasureMapCells[0][0] = new TreasureMapCell();
        treasureMapCells[0][0].setMountain(new Mountain(position));

        //When - Then
        assertTrue(treasureMapService.isPositionNullOrEmptyTakenOnTreasureMap(position, treasureMapCells));
    }

    @Test
    public void should_put_mountain_on_treasure_map() throws ParseMountainLineException {
        //Given
        String testLine = "M - 0 - 0";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        treasureMapCells[0][0] = new TreasureMapCell();
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);

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
        String testLine = "M - 1 - 1";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        TreasureMapCell testTreasureMapCell = new TreasureMapCell();
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);
        Treasure testTreasure = new Treasure(new Position(0, 0), 0);
        testTreasureMapCell.setTreasure(testTreasure);
        treasureMapCells[0][0] = testTreasureMapCell;

        //When
        treasureMapService.putMountainOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Mountain createdMountain = treasureMapCells[0][0].getMountain();

        //Then
        assertNull(createdMountain);
    }

    @Test
    public void should_not_put_mountain_on_treasure_map_when_position_is_taken() throws ParseMountainLineException {
        //Given
        String testLine = "M - 0 - 0";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        TreasureMapCell testTreasureMapCell = new TreasureMapCell();
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);
        Treasure testTreasure = new Treasure(new Position(0, 0), 0);
        testTreasureMapCell.setTreasure(testTreasure);
        treasureMapCells[0][0] = testTreasureMapCell;

        //When
        treasureMapService.putMountainOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Mountain createdMountain = treasureMapCells[0][0].getMountain();

        //Then
        assertNull(createdMountain);
    }

    @Test
    public void should_put_treasure_on_treasure_map() throws ParseTreasureLineException {
        //Given
        String testLine = "T - 0 - 0 - 0";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        treasureMapCells[0][0] = new TreasureMapCell();
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);

        Treasure testTreasure = new Treasure(new Position(0, 0), 0);

        //When
        treasureMapService.putTreasureOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Treasure createdTreasure = treasureMapCells[0][0].getTreasure();

        //Then
        assertEquals(testTreasure.getPosition().getHorizontalPosition(),
                createdTreasure.getPosition().getHorizontalPosition()
        );
        assertEquals(testTreasure.getPosition().getVerticalPosition(),
                createdTreasure.getPosition().getVerticalPosition()
        );
        assertEquals(testTreasure.getCount(), createdTreasure.getCount());
    }

    @Test
    public void should_not_put_treasure_on_treasure_map_when_position_is_out_of_bounds() throws ParseTreasureLineException {
        //Given
        String testLine = "T - 1 - 1 - 0";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        TreasureMapCell testTreasureMapCell = new TreasureMapCell();
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);
        Mountain testMountain = new Mountain(new Position(0, 0));
        testTreasureMapCell.setMountain(testMountain);
        treasureMapCells[0][0] = testTreasureMapCell;

        //When
        treasureMapService.putTreasureOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Treasure createdTreasure = treasureMapCells[0][0].getTreasure();

        //Then
        assertNull(createdTreasure);
    }

    @Test
    public void should_not_put_treasure_on_treasure_map_when_position_is_taken() throws ParseTreasureLineException {
        //Given
        String testLine = "T - 0 - 0 - 0";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        TreasureMapCell testTreasureMapCell = new TreasureMapCell();
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);
        Mountain testMountain = new Mountain(new Position(0, 0));
        testTreasureMapCell.setMountain(testMountain);
        treasureMapCells[0][0] = testTreasureMapCell;

        //When
        treasureMapService.putTreasureOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Treasure createdTreasure = treasureMapCells[0][0].getTreasure();

        //Then
        assertNull(createdTreasure);
    }

    @Test
    public void should_put_adventurer_on_treasure_map() throws ParseAdventurerLineException {
        //Given
        String testLine = "A - Antoine - 0 - 0 - S - ADG - 0";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        treasureMapCells[0][0] = new TreasureMapCell();
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);

        Adventurer testAdventurer = new Adventurer(
                "Antoine",
                new Position(0, 0),
                Orientation.SOUTH,
                List.of(Movement.FORWARD, Movement.TURN_RIGHT, Movement.TURN_LEFT),
                0
        );

        //When
        treasureMapService.putAdventurerOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Adventurer createdAdventurer = treasureMapCells[0][0].getAdventurer();

        //Then
        assertEquals(testAdventurer.getName(), createdAdventurer.getName());
        assertEquals(testAdventurer.getPosition().getHorizontalPosition(),
                createdAdventurer.getPosition().getVerticalPosition());
        assertEquals(testAdventurer.getOrientation(), createdAdventurer.getOrientation());
        assertEquals(testAdventurer.getMovements(), createdAdventurer.getMovements());
        assertEquals(testAdventurer.getCollectedTreasuresCount(), createdAdventurer.getCollectedTreasuresCount());
    }

    @Test
    public void should_not_put_adventurer_on_treasure_map_when_position_is_out_of_bounds() throws ParseAdventurerLineException {
        //Given
        String testLine = "A - Antoine - 1 - 1 - S - ADG - 0";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);
        TreasureMapCell testTreasureMapCell = new TreasureMapCell();
        Mountain testMountain = new Mountain(new Position(0, 0));
        testTreasureMapCell.setMountain(testMountain);
        treasureMapCells[0][0] = testTreasureMapCell;

        //When
        treasureMapService.putAdventurerOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Adventurer createdAdventurer = treasureMapCells[0][0].getAdventurer();

        //Then
        assertNull(createdAdventurer);
    }

    @Test
    public void should_not_put_adventurer_on_treasure_map_when_position_is_taken() throws ParseAdventurerLineException {
        //Given
        String testLine = "A - Antoine - 0 - 0 - S - ADG - 0";
        TreasureMapCell[][] treasureMapCells = new TreasureMapCell[1][1];
        TreasureMapCell testTreasureMapCell = new TreasureMapCell();
        TreasureMap treasureMap = new TreasureMap(1, 1, treasureMapCells);
        Mountain testMountain = new Mountain(new Position(0, 0));
        testTreasureMapCell.setMountain(testMountain);
        treasureMapCells[0][0] = testTreasureMapCell;

        //When
        treasureMapService.putAdventurerOnTreasureMapCell(testLine, treasureMap, treasureMapCells);
        Adventurer createdAdventurer = treasureMapCells[0][0].getAdventurer();

        //Then
        assertNull(createdAdventurer);
    }
}
