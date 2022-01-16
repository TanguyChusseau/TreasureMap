package fr.carbon.treasuremap.service;

import fr.carbon.treasuremap.exception.ParseLineException;
import fr.carbon.treasuremap.exception.ParseMountainLineException;
import fr.carbon.treasuremap.model.Mountain;
import fr.carbon.treasuremap.model.Position;

import static fr.carbon.treasuremap.utils.TreasureMapGameUtils.checkPositionFromInputFileType;
import static fr.carbon.treasuremap.utils.TreasureMapGameUtils.splitLine;

public class MountainService {

    /**
     * Créé une nouvelle montagne {@link Mountain} à partir des informations lues dans le fichier en entrée.
     *
     * @param line : la ligne correspondante dans le fichier.
     * @return la montagne construite.
     * @throws ParseMountainLineException en cas d'erreur de lecture des informations de la montagne dans le fichier.
     */
    protected static Mountain createMountainFromInputFileLine(String line) throws ParseMountainLineException {
        String[] mountainInformations = splitLine(line);
        if (mountainInformations.length != 3) {
            throw new ParseMountainLineException("Error parsing mountain informations.");
        }

        String horizontalPosition = mountainInformations[1];
        String verticalPosition = mountainInformations[2];
        try {
            checkPositionFromInputFileType(horizontalPosition, verticalPosition);
        } catch (ParseLineException ple) {
            throw new ParseMountainLineException(ple.getMessage());
        }

        return new Mountain(
                new Position(Integer.parseInt(horizontalPosition), Integer.parseInt(verticalPosition))
        );
    }
}
