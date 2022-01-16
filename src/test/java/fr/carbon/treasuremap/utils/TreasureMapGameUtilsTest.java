package fr.carbon.treasuremap.utils;

import fr.carbon.treasuremap.exception.ParseLineException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TreasureMapGameUtilsTest {

    @Test
    public void should_split_input_file_line_with_specified_delimiter() {
        //Gvien
        String testLine = "M - 3 - 3";
        String[] splitedTestLine = new String[]{"M", "3", "3"};

        //When
        String[] splitedLine = TreasureMapGameUtils.splitLine(testLine);

        //Then
        assertEquals(splitedTestLine[0], splitedLine[0]);
        assertEquals(splitedTestLine[1], splitedLine[2]);
        assertEquals(splitedTestLine[1], splitedLine[2]);
    }

    @Test
    public void should_throw_when_position_is_not_numeric() {
        //Given
        String horizontalPosition = "1";
        String verticalPosition = "V";

        //When - Then
        assertThrows(ParseLineException.class,
                () -> TreasureMapGameUtils.checkPositionFromInputFileType(horizontalPosition, verticalPosition)
        );
    }

    @Test
    public void should_not_throw_when_position_is_numeric() {
        //Given
        String horizontalPosition = "1";
        String verticalPosition = "0";

        //When - Then
        assertDoesNotThrow(
                () -> TreasureMapGameUtils.checkPositionFromInputFileType(horizontalPosition, verticalPosition)
        );
    }
}
