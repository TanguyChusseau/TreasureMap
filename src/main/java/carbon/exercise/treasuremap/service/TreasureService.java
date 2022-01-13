package carbon.exercise.treasuremap.service;

import carbon.exercise.treasuremap.exception.ParseLineException;
import carbon.exercise.treasuremap.model.Position;
import carbon.exercise.treasuremap.model.Treasure;
import carbon.exercise.treasuremap.utils.TreasureMapGameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

@Service
public class TreasureService {

    protected static Treasure createTreasureFromInputFileLine(String line) throws ParseLineException {
        String[] treasureDetails = TreasureMapGameUtils.splitLine(line);
        if (treasureDetails.length != 4) {
            throw new ParseLineException("Les informations du trésor sont incomplètes.");
        }

        String verticalPosition = treasureDetails[1];
        String horizontalPosition = treasureDetails[2];
        String treasureCount = treasureDetails[3];
        if (!NumberUtils.isParsable(horizontalPosition)
                || !NumberUtils.isParsable(verticalPosition)
                || !NumberUtils.isParsable(treasureCount)) {
            throw new ParseLineException(
                    "Une erreur est survenue lors de la lecture des informations du trésor :" +
                            " les positions possibles doivent être numériques"
            );
        }

        return new Treasure(
                new Position(Integer.parseInt(horizontalPosition),
                        Integer.parseInt(verticalPosition)),
                Integer.parseInt(treasureCount)
        );
    }
}
