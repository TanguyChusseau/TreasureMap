package carbon.exercise.treasuremap.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe représentant la carte aux trésors où seront placés les aventuriers {@link Adventurer},
 * les montagnes {@link Mountain} ainsi que les trésors {@link Treasure}.
 */
@Getter
@Setter
public class TreasureMap {

    private int columnCount;
    private int rowCount;
    private TreasureMapCell[][] treasureMapCells;

    public TreasureMap(int columnCount, int rowCount, TreasureMapCell[][] treasureMapCells) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.treasureMapCells = treasureMapCells;
    }
}
