package fr.carbon.treasuremap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Les montagnes sont des obstacles de la carte aux trésors bloquant le mouvement des aventuriers.
 */
@AllArgsConstructor
@Getter
@Setter
public class Mountain {

    private Position position;
}
