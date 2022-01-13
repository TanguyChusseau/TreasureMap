package carbon.exercise.treasuremap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum représentant les différents déplacements possibles d'un aventurier {@link Adventurer } sur la carte aux trésors.
 */
@AllArgsConstructor
@Getter
public enum Movement {
    FORWARD('A'),
    TURN_RIGHT('D'),
    TURN_LEFT('G');

    private final char value;
}
