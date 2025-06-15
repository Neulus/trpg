package Map;

import Entity.Entity;
import Entity.Static.Floor;
import Entity.Static.Wall;
import Game.GameRules;
import Game.Universe;
import Primitive.Direction;
import Primitive.Position;
import Primitive.VisibleEntity;
import UI.UI;

import java.util.ArrayList;
import java.util.List;

public abstract class World {

    protected String[] mapData;
    protected Position startingPosition;
    protected Direction spawnFacing;
    protected Entity[][] cells;
    protected UI ui;
    protected Universe universe;

    public void setUI(UI ui) {
        this.ui = ui;
    }

    public void setUniverse(Universe universe) {
        this.universe = universe;
    }

    public Universe getUniverse() {
        return universe;
    }

    public UI getUI() {
        return ui;
    }

    public Entity[][] getCells() {
        return cells;
    }

    public abstract Position getStartingPosition();

    public abstract String[] getMapData();

    public Direction getSpawnFacing() {
        return spawnFacing;
    }

    public void enterWorld() {
        if (universe != null) {
            universe.switchTo(this);
        }
    }

    private double calculateVisibility(Position from, Position to) {
        double distance = from.distanceTo(to);
        if (distance < 0.001) return 1.0;

        int steps = (int) (distance * 2) + 1;
        double visibility = 1.0;

        for (int i = 1; i < steps; i++) {
            double progress = (double) i / steps;
            int x = (int) (from.getX() + (to.getX() - from.getX()) * progress);
            int y = (int) (from.getY() + (to.getY() - from.getY()) * progress);

            Position checkPos = new Position(x, y);
            if (!isValidPosition(checkPos)) return 0.0;

            Entity entity = getEntityAt(checkPos);
            if (!entity.isTransparent()) return 0.0;
            if (entity.isInteractable() && !checkPos.equals(to)) {
                visibility -= 0.1;
                if (visibility <= 0) return 0.0;
            }
        }

        double distancePenalty = distance / (GameRules.EYESIGHT * 2.0);
        return Math.max(
            0.0,
            visibility * (1.0 - Math.min(1.0, distancePenalty))
        );
    }

    public List<VisibleEntity> getVisibleEntities(
        Position heroPos,
        Direction direction
    ) {
        List<VisibleEntity> visible = new ArrayList<>();

        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                Position objPos = new Position(x, y);
                Entity entity = cells[y][x];

                if (entity.isInteractable() && !objPos.equals(heroPos)) {
                    double visibility = calculateVisibility(heroPos, objPos);
                    if (visibility > 0) {
                        String angle = calculateRelativeAngle(
                            heroPos,
                            objPos,
                            direction
                        );
                        visible.add(
                            new VisibleEntity(
                                entity,
                                objPos,
                                visibility,
                                angle
                            )
                        );
                    }
                }
            }
        }

        return visible;
    }

    private String calculateRelativeAngle(
        Position from,
        Position to,
        Direction facing
    ) {
        double angle = Math.toDegrees(
            Math.atan2(from.getY() - to.getY(), to.getX() - from.getX())
        );
        angle = 90 - angle - facing.getValue() * 90;
        angle = ((angle % 360) + 360) % 360;

        if (angle >= 337.5 || angle < 22.5) return "정면";
        else if (angle < 67.5) return "우측 앞";
        else if (angle < 112.5) return "우측";
        else if (angle < 157.5) return "우측 뒤";
        else if (angle < 202.5) return "후방";
        else if (angle < 247.5) return "좌측 뒤";
        else if (angle < 292.5) return "좌측";
        else return "좌측 앞";
    }

    public Entity getEntityAt(Position pos) {
        if (isValidPosition(pos)) {
            return cells[pos.getY()][pos.getX()];
        }
        return new Wall(this);
    }

    public char getCellAt(Position pos) {
        if (isValidPosition(pos)) {
            return mapData[pos.getY()].charAt(pos.getX());
        }
        return '#';
    }

    public void clearCell(Position pos) {
        if (isValidPosition(pos)) {
            char[] row = mapData[pos.getY()].toCharArray();
            row[pos.getX()] = '-';
            mapData[pos.getY()] = new String(row);
            cells[pos.getY()][pos.getX()] = new Floor(this);
        }
    }

    public boolean isValidPosition(Position pos) {
        return (
            pos.getY() >= 0 &&
            pos.getY() < cells.length &&
            pos.getX() >= 0 &&
            pos.getX() < cells[pos.getY()].length
        );
    }

    public boolean isWalkable(Position pos) {
        if (!isValidPosition(pos)) return false;
        Entity entity = getEntityAt(pos);
        return entity.isWalkable();
    }

    public void initCells() {
        int rows = mapData.length;
        cells = new Entity[rows][];
        for (int y = 0; y < rows; y++) {
            int cols = mapData[y].length();
            cells[y] = new Entity[cols];
            for (int x = 0; x < cols; x++) {
                char symbol = mapData[y].charAt(x);
                switch (symbol) {
                    case '#' -> cells[y][x] = new Wall(this);
                    case '-' -> cells[y][x] = new Floor(this);
                    default -> cells[y][x] = new Floor(this);
                }
            }
        }
    }
}
