package Entity.Hero;

import Battle.AttackResult;
import Entity.Entity;
import Game.GameRules;
import Map.World;
import Primitive.Skill;
import Primitive.Weapon;

import java.util.Random;

public class Warrior extends Hero {
    public Warrior(String name, World world) {
        super(name, world);
        this.weapon = Weapon.SWORD;

        this.skills = new Skill[] {
            new Skill("강타", GameRules.SKILL_BASIC_LEVEL, GameRules.SKILL_BASIC_MULTIPLIER * 1.2),
            new Skill("방패", GameRules.SKILL_INTERMEDIATE_LEVEL, GameRules.SKILL_INTERMEDIATE_MULTIPLIER),
            new Skill("검기", GameRules.SKILL_ADVANCED_LEVEL, GameRules.SKILL_ADVANCED_MULTIPLIER * 1.5)
        };
        
        Random rng = new Random(name.chars().sum());
        this.power = rng.nextInt(GameRules.MIN_HERO_STAT + 5, GameRules.MAX_HERO_STAT + 5);
        this.defense = (GameRules.MAX_HERO_STAT + 10) - this.power;
    }

    @Override
    public String getClassName() {
        return "전사";
    }

}