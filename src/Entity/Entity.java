package Entity;

import Map.World;

public abstract class Entity {
    protected final String name;
    protected final boolean walkable;
    protected final boolean transparent;
    protected World world;

    protected Entity(String name, boolean walkable, boolean transparent, World world) {
        this.name = name;
        this.walkable = walkable;
        this.transparent = transparent;
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public abstract boolean isInteractable();

    public abstract void interact(Entity entity);
}