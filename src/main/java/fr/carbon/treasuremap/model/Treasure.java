package fr.carbon.treasuremap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe représentant les trésors qui seront éventuellement récupérés par un aventurier {@link Adventurer} lors de ses
 * déplacements sur la carte aux trésors {@link TreasureMapCell}.
 */
@AllArgsConstructor
@Getter
@Setter
public class Treasure {

    private Position position;
    private int count;
}
