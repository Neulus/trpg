package Entity.Static;

import Entity.Entity;
import Map.World;

public class Wall extends Entity {
    public Wall(World world) {
        super("벽", false, false, world);
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void interact(Entity entity) {}
}
