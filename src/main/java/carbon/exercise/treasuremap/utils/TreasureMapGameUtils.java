package carbon.exercise.treasuremap.utils;

import carbon.exercise.treasuremap.exception.ParseLineException;
import org.apache.commons.lang3.math.NumberUtils;

public abstract class TreasureMapGameUtils {

    public static final String LINE_DELIMITER = " - ";
    public static final String NEW_LINE= "\n";

    public static final char TREASURE_MAP_LINE_CHAR = 'C';
    public static final char ADVENTURER_LINE_CHAR = 'A';
    public static final char MOUNTAIN_LINE_CHAR = 'M';
    public static final char TREASURE_LINE_CHAR = 'T';

    public static final String ERROR_WHEN_READING_DETAILS =
            "Une erreur est survenue lors de la lecture des informations ";

    public static String[] splitLine(String line) {
        return line.split(LINE_DELIMITER);
    }

    /**
     * Vérifie si les informations concernant la position d'un des objets à ajouter à la carte aux trésors sont bien
     * numériques.
     *
     * @param horizontalPosition : position x.
     * @param verticalPosition   : position y.
     * @throws ParseLineException en cas d'erreur de lecture des informations dans le fichier.
     */
    public static void checkPositionFromInputFileType(String horizontalPosition, String verticalPosition)
            throws ParseLineException {
        if (!NumberUtils.isParsable(horizontalPosition) || !NumberUtils.isParsable(verticalPosition)) {
            throw new ParseLineException(ERROR_WHEN_READING_DETAILS +
                    ": les positions horizontales et verticales doivent être numériques");
        }
    }
}
