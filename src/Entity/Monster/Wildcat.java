package Entity.Monster;

import Map.World;

public class Wildcat extends Monster {
    public Wildcat(World world) {
        super("살퀭이", 5, 25, 120, 15, 5, 60, 40, world);
    }

    @Override
    public double getSkillMultiplier(String skillName) {
        switch (skillName) {
            case "아이스 스피어":
                return 1.6;
            case "강타":
                return 1.5;
            default:
                return 1.0;
        }
    }
}
