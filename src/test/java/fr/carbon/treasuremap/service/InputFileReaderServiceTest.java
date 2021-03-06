package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseLineException;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Service
public class InputFileReaderServiceTest {

    private final InputFileReaderService inputFileReaderService = new InputFileReaderService();

    @Test
    public void should_return_lines_when_reading_input_file() throws ParseLineException {
        //Given
        String inputFileLocation = "src/test/resources/";
        String inputFileName = "inputFileTest.txt";
        List<String> testLines = new ArrayList<>();
        testLines.add("C - 3 - 4");
        testLines.add("M - 1 - 0");
        testLines.add("T - 0 - 3 - 2");
        testLines.add("A - Sarah - 2 - 1 - W - ADA");

        //When
        List<String> inputFileLines = inputFileReaderService.getInputFileLines(inputFileLocation, inputFileName);

        //Then
        assertEquals(testLines.size(), inputFileLines.size());
        assertEquals(testLines.get(0), inputFileLines.get(0));
        assertEquals(testLines.get(1), inputFileLines.get(1));
        assertEquals(testLines.get(2), inputFileLines.get(2));
        assertEquals(testLines.get(3), inputFileLines.get(3));
    }

    @Test
    public void should_ignore_lines_starting_with_hashtag() throws ParseLineException {
        //Given
        String inputFileLocation = "src/test/resources/";
        String inputFileName = "inputFileHashtagTest.txt";

        //When
        List<String> inputFileLines = inputFileReaderService.getInputFileLines(inputFileLocation, inputFileName);

        //Then
        assertEquals(0, inputFileLines.size());
    }

    @Test
    public void should_throw_when_file_not_found() {
        //Given
        String inputFileLocation = "src/test/resources/";
        String inputFileName = "inputFile.txt";

        //When - Then
        assertThrows(ParseLineException.class, () ->
                inputFileReaderService.getInputFileLines(inputFileLocation, inputFileName)
        );
    }
}
