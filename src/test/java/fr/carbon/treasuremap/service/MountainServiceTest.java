package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseMountainLineException;
import fr.carbon.treasuremap.model.Mountain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MountainServiceTest {

    private final MountainService mountainService = new MountainService();

    @Test
    public void should_create_mountain_from_line() throws ParseMountainLineException {
        //Given
        String line = "M - 1 - 0";
        //When
        Mountain mountain = mountainService.createMountainFromInputFileLine(line);
        //Then
        assertEquals(1, mountain.getPosition().getHorizontalPosition());
        assertEquals(0, mountain.getPosition().getVerticalPosition());

    }

    @Test
    public void should_throw_when_not_enough_mountain_details_from__input_file() {
        //Given
        String mountainLineTest = "M - 1 -";

        //When-Then
        assertThrows(ParseMountainLineException.class,
                () -> mountainService.createMountainFromInputFileLine(mountainLineTest)
        );
    }

    @Test
    public void should_throw_when_mountain_position_not_numeric() {
        //Given
        String mountainLineTest = "M - T - 0";

        //When-Then
        assertThrows(ParseMountainLineException.class,
                () -> mountainService.createMountainFromInputFileLine(mountainLineTest)
        );
    }
}
