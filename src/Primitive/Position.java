package Primitive;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Position move(Direction direction) {
        return switch (direction) {
            case NORTH -> new Position(x, y - 1);
            case EAST -> new Position(x + 1, y);
            case SOUTH -> new Position(x, y + 1);
            case WEST -> new Position(x - 1, y);
        };
    }

    public double distanceTo(Position other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return x == other.x && y == other.y;
        }
        return false;
    }
}
