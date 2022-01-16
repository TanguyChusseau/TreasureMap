package carbon.exercise.treasuremap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant la position sur la carte aux trésors {@link TreasureMapCell} d'un aventurier {@link Adventurer}
 * d'une montagne {@link Mountain} ou d'un trésor {@link Treasure}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Position {

    private int horizontalPosition;
    private int verticalPosition;
}
