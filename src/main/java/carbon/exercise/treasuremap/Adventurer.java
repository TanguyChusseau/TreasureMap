package carbon.exercise.treasuremap;

import lombok.Data;

@Data
public class Adventurer {

    private String name;
    private int treasureCount;
    private int horizontalPosition;
    private int verticalPosition;
    private char orientation;
}
