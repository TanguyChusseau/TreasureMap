package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseMountainLineException;
import fr.carbon.treasuremap.exception.ParseTreasureLineException;
import fr.carbon.treasuremap.model.Treasure;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TreasureServiceTest {

    private final TreasureService treasureService = new TreasureService();
    private final MountainService mountainService = new MountainService();

    @Test
    public void should_create_treasure_from_line() throws ParseTreasureLineException {
        //Given
        String treasureLineTest = "T - 1 - 0 - 2";

        //When
        Treasure treasure = treasureService.createTreasureFromInputFileLine(treasureLineTest);

        //Then
        assertEquals(1, treasure.getPosition().getHorizontalPosition());
        assertEquals(0, treasure.getPosition().getVerticalPosition());
        assertEquals(2, treasure.getCount());
    }

    @Test
    public void should_throw_when_not_enough_treasure_details_from_line() {
        //Given
        String line = "T - 1 -";

        //When-Then
        assertThrows(ParseMountainLineException.class,
                () -> mountainService.createMountainFromInputFileLine(line)
        );
    }

    @Test
    public void should_throw_when_treasure_position_is_not_numeric() {
        //Given
        String treasureLineTest = "T - T - 0";

        //When-Then
        assertThrows(ParseTreasureLineException.class,
                () -> treasureService.createTreasureFromInputFileLine(treasureLineTest)
        );
    }

    @Test
    public void should_throw_when_treasure_count_is_not_numeric() {
        //Given
        String treasureLineTest = "T - 1 - 0 - E";

        //When-Then
        assertThrows(ParseTreasureLineException.class,
                () -> treasureService.createTreasureFromInputFileLine(treasureLineTest)
        );
    }
}
