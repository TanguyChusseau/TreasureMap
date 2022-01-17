package fr.carbon.treasuremap.model;

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

    public TreasureMap(int columnCount, int rowCount) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.treasureMapCells = setupTreasureMapMatrix(this.columnCount, this.rowCount);
    }

    /**
     * Créé la matrice de {@link TreasureMapCell} représentant la carte aux trésors.
     *
     * @param columnCount : le nombre de colonnes.
     * @param rowCount    : le nombre de lignes.
     * @return : la matrice construite (array à deux dimensions).
     */
    private TreasureMapCell[][] setupTreasureMapMatrix(int columnCount, int rowCount) {
        TreasureMapCell[][] treasureMapMatrix = new TreasureMapCell[columnCount][rowCount];
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                treasureMapMatrix[i][j] = new TreasureMapCell();
            }
        }
        return treasureMapMatrix;
    }
}
