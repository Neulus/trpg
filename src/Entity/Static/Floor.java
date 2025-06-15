package Entity.Static;

import Entity.Entity;
import Map.World;

public class Floor extends Entity {
    public Floor(World world) {
        super("바닥", true, true, world);
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void interact(Entity entity) {
    }
}