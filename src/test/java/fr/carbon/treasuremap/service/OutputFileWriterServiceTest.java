package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseLineException;
import fr.carbon.treasuremap.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OutputFileWriterServiceTest {

    private final OutputFileWriterService outputFileWriterService = new OutputFileWriterService();
    private final static String outputFileLocation = "src/test/resources/";
    private final static String outputFileName = "outputFileTest.txt";

    @AfterEach
    void cleanUp(TestInfo testInfo) throws IOException {
        if (testInfo.getTags().contains("toCleanUp")) {
            Files.deleteIfExists(Paths.get(outputFileLocation + outputFileName));
        }
    }

    @Test
    @Tag("toCleanUp")
    public void should_write_only_treasure_map_line_to_output_file() throws ParseLineException, IOException {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);

        //When
        outputFileWriterService.writeTreasureMapLinesToOutputFile(treasureMap, outputFileLocation, outputFileName);
        Path path = Paths.get(outputFileLocation + outputFileName);

        //Then
        assertNotNull(path);
        assertEquals(1, Files.lines(path).toList().size());
        assertEquals("C - 2 - 2", Files.lines(path).toList().get(0));
    }

    @Test
    @Tag("toCleanUp")
    public void should_write_treasure_map_details_to_output_file() throws ParseLineException, IOException {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();
        List<Mountain> mountains = new ArrayList<>();
        List<Treasure> treasures = new ArrayList<>();
        List<Adventurer> adventurers = new ArrayList<>();

        Mountain exptectedMountain = new Mountain(new Position(0, 0));
        Treasure expectedTreasure = new Treasure(new Position(1, 0), 2);
        Adventurer expectedAdventurer = new Adventurer(
                "Laura",
                new Position(1, 1),
                Orientation.NORTH,
                new ArrayList<>(),
                2,
                1
        );
        TreasureMapCell treasureMapCellMountain = new TreasureMapCell();
        treasureMapCellMountain.setMountain(exptectedMountain);
        TreasureMapCell treasureMapCellTreasure = new TreasureMapCell();
        treasureMapCellTreasure.setTreasure(expectedTreasure);
        TreasureMapCell treasureMapCellAdventurer = new TreasureMapCell();
        treasureMapCellAdventurer.setAdventurer(expectedAdventurer);

        treasureMapCells[0][0] = treasureMapCellMountain;
        treasureMapCells[0][1] = treasureMapCellTreasure;
        treasureMapCells[1][1] = treasureMapCellAdventurer;

        //When
        outputFileWriterService.writeTreasureMapLinesToOutputFile(treasureMap, outputFileLocation, outputFileName);
        Path path = Paths.get(outputFileLocation + outputFileName);

        //Then
        assertNotNull(path);
        List<String> lines = Files.lines(path).toList();
        assertEquals(4, Files.lines(path).toList().size());
        assertEquals("C - 2 - 2", lines.get(0));
        assertEquals("M - 0 - 0", lines.get(1));
        assertEquals("T - 1 - 0 - 2", lines.get(2));
        assertEquals("A - Laura - 1 - 1 - N - 2", lines.get(3));

    }

    @Test
    @Tag("toCleanUp")
    public void should_not_throw_when_output_file_found() throws ParseLineException {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);

        //When
        outputFileWriterService.writeTreasureMapLinesToOutputFile(treasureMap, outputFileLocation, outputFileName);

        //Then
        assertDoesNotThrow(() -> outputFileWriterService
                .writeTreasureMapLinesToOutputFile(treasureMap, outputFileLocation, outputFileName)
        );
    }

    @Test
    public void should_throw_when_output_file_not_found() {
        //Given
        String outputFileInvalidLocation = "src/invalidLocation/";
        TreasureMap treasureMap = new TreasureMap(1, 1);

        //When - Then
        assertThrows(ParseLineException.class, () -> outputFileWriterService
                .writeTreasureMapLinesToOutputFile(treasureMap, outputFileInvalidLocation, outputFileName)
        );
    }

    @Test
    public void should_get_treasure_map_content() {
        //Given
        TreasureMap treasureMap = new TreasureMap(2, 2);
        TreasureMapCell[][] treasureMapCells = treasureMap.getTreasureMapCells();
        List<Mountain> mountains = new ArrayList<>();
        List<Treasure> treasures = new ArrayList<>();
        List<Adventurer> adventurers = new ArrayList<>();

        Mountain exptectedMountain = new Mountain(new Position(0, 0));
        Treasure expectedTreasure = new Treasure(new Position(1, 0), 2);
        Adventurer expectedAdventurer = new Adventurer(
                "Laura",
                new Position(1, 1),
                Orientation.NORTH,
                new ArrayList<>(),
                2,
                1
        );
        TreasureMapCell treasureMapCellMountain = new TreasureMapCell();
        treasureMapCellMountain.setMountain(exptectedMountain);
        TreasureMapCell treasureMapCellTreasure = new TreasureMapCell();
        treasureMapCellTreasure.setTreasure(expectedTreasure);
        TreasureMapCell treasureMapCellAdventurer = new TreasureMapCell();
        treasureMapCellAdventurer.setAdventurer(expectedAdventurer);

        treasureMapCells[0][0] = treasureMapCellMountain;
        treasureMapCells[0][1] = treasureMapCellTreasure;
        treasureMapCells[1][1] = treasureMapCellAdventurer;

        //When
        outputFileWriterService.getTreasureMapContent(treasureMap, mountains, treasures, adventurers);

        //Then
        assertEquals(exptectedMountain, mountains.get(0));
        assertEquals(expectedTreasure, treasures.get(0));
        assertEquals(expectedAdventurer, adventurers.get(0));
    }

    @Test
    public void should_format_and_add_treasure_map_line() {
        //Given
        String expectedLine = "C - 1 - 1";
        List<String> expectedLines = new ArrayList<>();
        TreasureMap treasureMap = new TreasureMap(1, 1);

        //When
        outputFileWriterService.formatAndAddTreasureMapLine(treasureMap, expectedLines);

        //Then
        assertEquals(expectedLine, expectedLines.get(0));
    }

    @Test
    public void should_format_and_add_mountain_line() {
        //Given
        String expectedLine = "M - 0 - 0";
        List<String> expectedLines = new ArrayList<>();
        Mountain mountain = new Mountain(new Position(0, 0));

        //When
        outputFileWriterService.formatAndAddMountainLine(mountain, expectedLines);

        //Then
        assertEquals(expectedLine, expectedLines.get(0));
    }

    @Test
    public void should_format_and_add_treasure_line() {
        //Given
        String expectedLine = "T - 1 - 1 - 2";
        List<String> expectedLines = new ArrayList<>();
        Treasure treasure = new Treasure(new Position(1, 1), 2);

        //When
        outputFileWriterService.formatAndAddTreasureLine(treasure, expectedLines);

        //Then
        assertEquals(expectedLine, expectedLines.get(0));
    }

    @Test
    public void should_format_and_add_adventurer_line() {
        //Given
        String expectedLine = "A - Audrey - 1 - 1 - S - 2";
        List<String> expectedLines = new ArrayList<>();
        Adventurer adventurer = new Adventurer(
                "Audrey",
                new Position(1, 1),
                Orientation.SOUTH,
                new ArrayList<>(),
                2,
                1
        );

        //When
        outputFileWriterService.formatAndAddAdventurerLine(adventurer, expectedLines);

        //Then
        assertEquals(expectedLine, expectedLines.get(0));
    }
}
