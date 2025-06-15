package Entity;

import Battle.AttackResult;
import Map.World;

public abstract class Character extends Entity {
    protected int level;
    protected int power;
    protected int defense;
    protected int hp;
    protected int maxHp;
    protected int mp;
    protected int maxMp;

    protected Character(String name, int level, int power, int defense,
                        int hp, int maxHp, int mp, int maxMp, World world) {
        super(name, false, true, world);
        this.level = level;
        this.power = power;
        this.defense = defense;
        this.hp = hp;
        this.maxHp = maxHp;
        this.mp = mp;
        this.maxMp = maxMp;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }

    public abstract AttackResult attack();

    public int getLevel() { return level; }
    public int getPower() { return power; }
    public int getDefense() { return defense; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMp() { return mp; }
    public int getMaxMp() { return maxMp; }
}