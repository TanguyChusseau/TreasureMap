package carbon.exercise.treasuremap.service;

import carbon.exercise.treasuremap.exception.ParseTreasureLineException;
import carbon.exercise.treasuremap.model.Position;
import carbon.exercise.treasuremap.model.Treasure;
import carbon.exercise.treasuremap.utils.TreasureMapGameUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class TreasureService {

    /**
     * Créé un nouveau trésor {@link Treasure} à partir des informations lues dans le fichier en entrée.
     *
     * @param line : la ligne correspondante dans le fichier.
     * @return le trésor construit.
     * @throws ParseTreasureLineException en cas d'erreur de lecture des informations du trésor dans le fichier.
     */
    protected static Treasure createTreasureFromInputFileLine(String line) throws ParseTreasureLineException {
        String[] treasureDetails = TreasureMapGameUtils.splitLine(line);
        if (treasureDetails.length != 4) {
            throw new ParseTreasureLineException("Les informations du trésor sont incomplètes.");
        }

        String horizontalPosition = treasureDetails[2];
        String verticalPosition = treasureDetails[1];
        String treasureCount = treasureDetails[3];

        if (!NumberUtils.isParsable(horizontalPosition)
                || !NumberUtils.isParsable(verticalPosition)
                || !NumberUtils.isParsable(treasureCount)) {
            throw new ParseTreasureLineException(
                    "Une erreur est survenue lors de la lecture des informations du trésor :" +
                            " les positions possibles et/ou le nombre de trésors doivent être numériques."
            );
        }

        return new Treasure(
                new Position(Integer.parseInt(horizontalPosition),
                        Integer.parseInt(verticalPosition)),
                Integer.parseInt(treasureCount)
        );
    }
}
