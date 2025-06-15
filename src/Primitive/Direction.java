package Primitive;

public enum Direction {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
