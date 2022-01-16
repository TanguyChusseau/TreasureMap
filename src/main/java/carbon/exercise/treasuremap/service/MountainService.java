package carbon.exercise.treasuremap.service;

import carbon.exercise.treasuremap.exception.ParseLineException;
import carbon.exercise.treasuremap.exception.ParseMountainLineException;
import carbon.exercise.treasuremap.model.Mountain;
import carbon.exercise.treasuremap.model.Position;
import carbon.exercise.treasuremap.utils.TreasureMapGameUtils;

public class MountainService {

    /**
     * Créé une nouvelle montagne {@link Mountain} à partir des informations lues dans le fichier en entrée.
     *
     * @param line : la ligne correspondante dans le fichier.
     * @return la montagne construite.
     * @throws ParseMountainLineException en cas d'erreur de lecture des informations de la montagne dans le fichier.
     */
    protected static Mountain createMountainFromInputFileLine(String line) throws ParseMountainLineException {
        String[] mountainInformations = TreasureMapGameUtils.splitLine(line);
        if (mountainInformations.length != 3) {
            throw new ParseMountainLineException("Error parsing mountain informations.");
        }

        String horizontalPosition = mountainInformations[2];
        String verticalPosition = mountainInformations[1];
        try {
            TreasureMapGameUtils.checkPositionFromInputFileType(horizontalPosition, verticalPosition);
        } catch (ParseLineException ple) {
            throw new ParseMountainLineException(ple.getMessage());
        }

        return new Mountain(
                new Position(Integer.parseInt(horizontalPosition), Integer.parseInt(verticalPosition))
        );
    }
}
