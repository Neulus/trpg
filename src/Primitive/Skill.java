package Primitive;

public class Skill {
    private final String name;
    private final int mpCost;
    private final double multiplier;

    public Skill(String name, int mpCost, double multiplier) {
        this.name = name;
        this.mpCost = mpCost;
        this.multiplier = multiplier;
    }

    public String getName() { return name; }
    public int getMpCost() { return mpCost; }
    public double getMultiplier() { return multiplier; }
}