package Entity.Hero;

import Battle.AttackResult;
import Entity.Character;
import Entity.Entity;
import Game.GameRules;
import Map.World;
import Primitive.Skill;
import Primitive.Weapon;
import Mission.MissionManager;

import java.util.ArrayList;
import java.util.Random;

public class Hero extends Character {
    private int experience;
    private int money;
    protected Weapon weapon;

    protected Skill[] skills = {
            new Skill("발차기", GameRules.SKILL_BASIC_LEVEL, GameRules.SKILL_BASIC_MULTIPLIER),
            new Skill("배기", GameRules.SKILL_INTERMEDIATE_LEVEL, GameRules.SKILL_INTERMEDIATE_MULTIPLIER),
            new Skill("썰기", GameRules.SKILL_ADVANCED_LEVEL, GameRules.SKILL_ADVANCED_MULTIPLIER)
    };

    public Hero(String name, World world) {
        super(name, GameRules.HERO_BASE_LEVEL, GameRules.HERO_BASE_POWER, GameRules.HERO_BASE_DEFENSE, 
              GameRules.HERO_BASE_HP, GameRules.HERO_BASE_HP, GameRules.HERO_BASE_MP, GameRules.HERO_BASE_MP, world);
        this.experience = 0;
        this.money = 0;
        this.weapon = Weapon.NONE;

        Random rng = new Random(name.chars().sum());
        this.power = rng.nextInt(GameRules.MIN_HERO_STAT, GameRules.MAX_HERO_STAT);
        this.defense = GameRules.MAX_HERO_STAT - this.power;
    }

    public boolean isInteractable() {
        return false;
    }

    public void interact(Entity entity) {}

    @Override
    public AttackResult attack() {
        return useSkill(0);
    }

    public AttackResult useSkill(int skillIndex) {
        Skill skill = skills[skillIndex];
        mp -= skill.getMpCost();

        int baseDamage = GameRules.calculateDamage(power, skill.getMultiplier());
        boolean isCritical = GameRules.isCriticalHit(level);
        
        if (isCritical) {
            baseDamage = GameRules.applyCritical(baseDamage);
        }
        
        int weaponDamage = weapon.getDamage();
        int totalDamage = baseDamage + weaponDamage;

        return new AttackResult(totalDamage, isCritical, skill.getName());
    }

    public void gainExperience(int exp) {
        experience += exp;
        checkLevelUp();
    }

    public void gainMoney(int amount) {
        money += amount;
    }

    public void spendMoney(int amount) {
        money -= amount;
    }

    private void checkLevelUp() {
        if (experience >= GameRules.getRequiredExperience(level)) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;

        // 체력 및 마나 회복
        power = (int) ((power + 1) * GameRules.HERO_STAT_MULTIPLIER);
        defense = (int) ((defense + 1) * GameRules.HERO_STAT_MULTIPLIER);

        maxHp = GameRules.getMaxHp(level);
        maxMp = GameRules.getMaxMp(level);

        hp = Math.max(hp, maxHp);
        mp = Math.max(mp, maxMp);

        money += GameRules.getLevelUpReward(level);
    }

    public void resurrect() {
        level = GameRules.HERO_BASE_LEVEL;
        hp = maxHp = GameRules.HERO_BASE_HP;
        mp = maxMp = GameRules.HERO_BASE_MP;
        experience = 0;
        money = 0;
        weapon = Weapon.NONE;

        Random rng = new Random(name.chars().sum());
        power = rng.nextInt(GameRules.MIN_HERO_STAT, GameRules.MAX_HERO_STAT);
        defense = GameRules.MAX_HERO_STAT - power;
    }

    public int getExperience() {
        return experience;
    }

    public int getMoney() {
        return money;
    }

    public Skill[] getAvailableSkills() {
        ArrayList<Skill> availableSkills = new ArrayList<>();
        for (Skill skill : skills) {
            if (skill.getMpCost() <= mp) {
                availableSkills.add(skill);
            }
        }
        return availableSkills.toArray(new Skill[0]);
    }

    public void increasePower(int amount) {
        power += amount;
    }

    public void increaseDefense(int amount) {
        defense += amount;
    }

    public void increaseHp(int amount) {
        hp = Math.min(hp + amount, maxHp);
    }

    public void increaseMp(int amount) {
        mp = Math.min(mp + amount, maxMp);
    }
    
    public String getMissionLog() {
        return MissionManager.getInstance().getMissionLogDisplay();
    }
    
    public Weapon getWeapon() {
        return weapon;
    }
    
    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    
    public String getClassName() {
        return "";
    }
}