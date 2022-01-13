package carbon.exercise.treasuremap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe représentant la carte aux trésors qui servira de base du jeu pour les aventuriers.
 */
@AllArgsConstructor
@Getter
@Setter
public class TreasureMap {

    private int rowCount;
    private int columnCount;
}
