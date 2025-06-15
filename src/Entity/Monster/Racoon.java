package Entity.Monster;

import Map.World;

public class Racoon extends Monster {
    public Racoon(World world) {
        super("너구리", 1, 10, 30, 5, 0, 15, 10, world);
    }

    @Override
    public double getSkillMultiplier(String skillName) {
        switch (skillName) {
            case "라이트닝 볼트":
                return 2.2;
            case "관통의 화살":
                return 1.5;
            default:
                return 1.0;
        }
    }
}
