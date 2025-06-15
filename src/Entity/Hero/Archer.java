package Entity.Hero;

import Battle.AttackResult;
import Game.GameRules;
import Map.World;
import Primitive.Skill;
import Primitive.Weapon;

import java.util.Random;

public class Archer extends Hero {
    public Archer(String name, World world) {
        super(name, world);
        this.weapon = Weapon.BOW;

        this.skills = new Skill[] {
            new Skill("사격", GameRules.SKILL_BASIC_LEVEL, GameRules.SKILL_BASIC_MULTIPLIER * 1.1),
            new Skill("관통", GameRules.SKILL_INTERMEDIATE_LEVEL, GameRules.SKILL_INTERMEDIATE_MULTIPLIER * 1.3),
            new Skill("폭발 화살", GameRules.SKILL_ADVANCED_LEVEL, GameRules.SKILL_ADVANCED_MULTIPLIER * 1.6)
        };
        
        Random rng = new Random(name.chars().sum());
        this.power = rng.nextInt(GameRules.MIN_HERO_STAT + 2, GameRules.MAX_HERO_STAT + 2);
        this.defense = (GameRules.MAX_HERO_STAT + 4) - this.power;
    }
    
    @Override
    public String getClassName() {
        return "궁수";
    }

}