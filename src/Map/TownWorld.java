package Map;

import Entity.Static.Exit;
import Entity.Static.PortionShop;
import Entity.Static.WeaponShop;
import Entity.NPC.TownGuard;
import Primitive.Direction;
import Primitive.Position;

public class TownWorld extends World {
    public TownWorld() {
        this.mapData = new String[] { "#N#G#", "#---#", "#PW##" };
        this.startingPosition = new Position(3, 1);
        this.spawnFacing = Direction.WEST;
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
                    case 'P' -> cells[y][x] = new PortionShop(this, ui);
                    case 'W' -> cells[y][x] = new WeaponShop(this, ui);
                    case 'G' -> cells[y][x] = new Exit(
                        this,
                        universe.getHuntingWorld()
                    );
                    case 'N' -> cells[y][x] = new TownGuard(this);
                    default -> {}
                }
            }
        }
    }
}
