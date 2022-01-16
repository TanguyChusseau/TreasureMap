package fr.carbon.treasuremap.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe représentant chaque cellule de la carte aux trésors {@link TreasureMap}.
 */
@Getter
@Setter
public class TreasureMapCell {

    private Adventurer adventurer;
    private Mountain mountain;
    private Treasure treasure;
}
