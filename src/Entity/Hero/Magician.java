package Entity.Hero;

import Battle.AttackResult;
import Game.GameRules;
import Map.World;
import Primitive.Skill;
import Primitive.Weapon;

import java.util.Random;

public class Magician extends Hero {
    public Magician(String name, World world) {
        super(name, world);
        this.weapon = Weapon.STAFF;

        this.skills = new Skill[] {
            new Skill("파이어볼", GameRules.SKILL_BASIC_LEVEL, GameRules.SKILL_BASIC_MULTIPLIER * 1.3),
            new Skill("아이스 스피어", GameRules.SKILL_INTERMEDIATE_LEVEL, GameRules.SKILL_INTERMEDIATE_MULTIPLIER * 1.4),
            new Skill("라이트닝 볼트", GameRules.SKILL_ADVANCED_LEVEL, GameRules.SKILL_ADVANCED_MULTIPLIER * 1.8)
        };
        
        Random rng = new Random(name.chars().sum());
        this.power = rng.nextInt(GameRules.MIN_HERO_STAT - 3, GameRules.MAX_HERO_STAT - 3);
        this.defense = (GameRules.MAX_HERO_STAT - 6) - this.power;
        this.maxMp = GameRules.HERO_BASE_MP + 20;
        this.mp = this.maxMp;
    }
    
    @Override
    public String getClassName() {
        return "마법사";
    }
}