package carbon.exercise.treasuremap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum représentant les orientations possibles d'un aventurier {@link Adventurer} sur la carte aux trésors.
 */
@AllArgsConstructor
@Getter
public enum Orientation {
    NORTH("N"),
    SOUTH("S"),
    EAST("E"),
    WEST("W");

    private final String value;
}
