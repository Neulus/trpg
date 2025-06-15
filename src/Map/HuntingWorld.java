package Map;

import Entity.Monster.Bear;
import Entity.Monster.Racoon;
import Entity.Monster.Snake;
import Entity.Monster.Wildcat;
import Entity.Static.Exit;
import Game.GameRules;
import Primitive.Direction;
import Primitive.Position;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class HuntingWorld extends World {

    private static final String[] TEMPLATE = {
            "#########################",
            "#-#---#---#-------------#",
            "#-###-#-#-#-#####-###-#-#",
            "#---#---#---#---#---#-#-#",
            "###-#-#######-#-#####-#-#",
            "#---#-#---#---#-------#-#",
            "#-###-#-#-#-###########-#",
            "#-#-#-#-#-#-----#---#---#",
            "#-#-#-###-#####-###-#-###",
            "#-#-#---------#---#-#-#-#",
            "#-#-#-###########-#-#-#-#",
            "#-#-#-#---------#-#-#-#-#",
            "#-#-#-#-#######-#-#-#-#-#",
            "#-#---#-----#-#---#-#-#-#",
            "#-#########-#-#####-#-#-#",
            "#-----------#-------#-#-#",
            "#############-#####-#-#-#",
            "#-----------#---#-#-#---#",
            "#-###-#####-#-#-#-#-###-#",
            "#-#---#---#-#-#-#---#---#",
            "#-#-###-#-#-#-#-###-#-###",
            "#-#-#---#-#-#-#---#-#-#-#",
            "#-#-#-###-###-###-###-#-#",
            "#-#-----#-------#-------#",
            "#######################S#"
    };

    public HuntingWorld() {
        this.startingPosition = new Position(23, 23);
        this.spawnFacing = Direction.NORTH;
        this.mapData = generateMap();
    }

    private String[] generateMap() {
        String[] newMap = TEMPLATE.clone();
        List<Position> availablePositions = findAvailablePositions();
        Random random = new Random();

        for (int i = 0; i < Math.min(GameRules.MONSTER_COUNT, availablePositions.size()); i++) {
            int index = random.nextInt(availablePositions.size());
            Position pos = availablePositions.remove(index);

            char monster = selectMonster(random.nextDouble());
            setCell(newMap, pos, monster);
        }

        return newMap;
    }

    private List<Position> findAvailablePositions() {
        List<Position> positions = new ArrayList<>();

        for (int y = 0; y < TEMPLATE.length; y++) {
            for (int x = 0; x < TEMPLATE[y].length(); x++) {
                Position pos = new Position(x, y);
                if (TEMPLATE[y].charAt(x) == '-' &&
                        pos.distanceTo(startingPosition) > GameRules.SPAWN_PROTECTION_RANGE) {
                    positions.add(pos);
                }
            }
        }

        return positions;
    }

    private char selectMonster(double probability) {
        if (probability < 0.5) return 'W';
        else if (probability < 0.8) return 'X';
        else if (probability < 0.95) return 'Y';
        else return 'Z';
    }

    private void setCell(String[] map, Position pos, char value) {
        char[] row = map[pos.getY()].toCharArray();
        row[pos.getX()] = value;
        map[pos.getY()] = new String(row);
    }

    @Override
    public Position getStartingPosition() {
        return startingPosition;
    }

    @Override
    public String[] getMapData() {
        return mapData;
    }

    @Override
    public void initCells() {
        super.initCells();

        String[] data = getMapData();
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length(); x++) {
                char c = data[y].charAt(x);
                Position pos = new Position(x, y);
                switch (c) {
                    case 'W' -> cells[y][x] = new Racoon(this);
                    case 'X' -> cells[y][x] = new Wildcat(this);
                    case 'Y' -> cells[y][x] = new Snake(this);
                    case 'Z' -> cells[y][x] = new Bear(this);
                    case 'S' -> cells[y][x] = new Exit(
                        this,
                        universe.getTownWorld()
                    );
                    default -> {}
                }
            }
        }
    }
}