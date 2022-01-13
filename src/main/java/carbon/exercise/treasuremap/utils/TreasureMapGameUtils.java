package carbon.exercise.treasuremap.utils;

import carbon.exercise.treasuremap.exception.ParseLineException;
import org.apache.commons.lang3.math.NumberUtils;

public abstract class TreasureMapGameUtils {

    private static final String LINE_DELIMITER = " - ";
    public static final char ADVENTURER_LINE_CHAR = 'A';
    public static final char MOUNTAIN_LINE_CHAR= 'M';
    public static final char TREASURE_LINE_CHAR = 'T';
    public static final String ERROR_WHEN_READING_DETAILS =
            "Une erreur est survenue lors de la lecture des informations ";

    public static String[] splitLine(String line) {
        return line.split(LINE_DELIMITER);
    }

    public static void checkPositionFromInputFileType(String horizontalPosition, String verticalPosition)
            throws ParseLineException {
        if (!NumberUtils.isParsable(horizontalPosition) || !NumberUtils.isParsable(verticalPosition)) {
            throw new ParseLineException(ERROR_WHEN_READING_DETAILS +
                            ": les positions horizontales et verticales doivent être numériques");
        }
    }
}
