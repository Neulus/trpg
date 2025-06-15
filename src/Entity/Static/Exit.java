package Entity.Static;

import Entity.Entity;
import Map.World;

public class Exit extends Entity {
    private final World targetWorld;

    public Exit(World currentWorld, World targetWorld) {
        super("출구", false, true, currentWorld);
        this.targetWorld = targetWorld;
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public void interact(Entity entity) {
        targetWorld.enterWorld();
    }
}