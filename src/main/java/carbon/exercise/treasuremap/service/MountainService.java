package carbon.exercise.treasuremap.service;

import carbon.exercise.treasuremap.exception.ParseLineException;
import carbon.exercise.treasuremap.model.Mountain;
import carbon.exercise.treasuremap.model.Position;
import carbon.exercise.treasuremap.utils.TreasureMapGameUtils;
import org.springframework.stereotype.Service;

@Service
public class MountainService {

    protected static Mountain createMountainFromInputFileLine(String line) throws ParseLineException {
        String[] mountainInformations = TreasureMapGameUtils.splitLine(line);
        if (mountainInformations.length != 3) {
            throw new ParseLineException("Error parsing mountain informations.");
        }

        String verticalPosition = mountainInformations[1];
        String horizontalPosition = mountainInformations[2];
        TreasureMapGameUtils.checkPositionFromInputFileType(horizontalPosition, verticalPosition);

        return new Mountain(
                new Position(Integer.parseInt(horizontalPosition), Integer.parseInt(verticalPosition))
        );
    }
}
