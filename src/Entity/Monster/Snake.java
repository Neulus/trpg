package Entity.Monster;

import Map.World;

public class Snake extends Monster {
    public Snake(World world) {
        super("뱀", 10, 65, 600, 50, 15, 300, 220, world);
    }

    @Override
    public double getSkillMultiplier(String skillName) {
        switch (skillName) {
            case "정확한 사격":
                return 1.7;
            case "검기":
                return 2.0;
            default:
                return 1.0;
        }
    }
}
