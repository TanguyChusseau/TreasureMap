package fr.carbon.treasuremap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Classe représentant les aventuriers qui se déplaceront sur la carte aux trésors {@link TreasureMapCell}.
 */
@AllArgsConstructor
@Getter
@Setter
public class Adventurer {

    private String name;
    private Position position;
    private Orientation orientation;
    private List<Movement> movements;
    private int collectedTreasuresCount;
}
