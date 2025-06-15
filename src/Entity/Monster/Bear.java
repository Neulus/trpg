package Entity.Monster;

import Map.World;

public class Bear extends Monster {
    public Bear(World world) {
        super("곰", 20, 120, 1800, 85, 20, 1000, 600, world);
    }

    @Override
    public double getSkillMultiplier(String skillName) {
        switch (skillName) {
            case "폭발 화살":
                return 1.9;
            case "파이어볼":
                return 1.8;
            case "강타":
                return 1.5;
            default:
                return 1.0;
        }
    }
}
