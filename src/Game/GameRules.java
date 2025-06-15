package Game;

import java.util.Random;

public class GameRules {
    private static final Random random = new Random();

    public static final int EYESIGHT = 2;
    public static final int SPAWN_PROTECTION_RANGE = 2;
    public static final int MONSTER_COUNT = 50;
    
    public static final int MIN_HERO_STAT = 1;
    public static final int MAX_HERO_STAT = 20;
    public static final double HERO_STAT_MULTIPLIER = 1.3;
    public static final int HERO_BASE_LEVEL = 1;
    public static final int HERO_BASE_POWER = 0;
    public static final int HERO_BASE_DEFENSE = 0;
    public static final int HERO_BASE_HP = 100;
    public static final int HERO_BASE_MP = 100;
    
    public static final int SKILL_BASIC_LEVEL = 0;
    public static final int SKILL_INTERMEDIATE_LEVEL = 10;
    public static final int SKILL_ADVANCED_LEVEL = 30;
    public static final double SKILL_BASIC_MULTIPLIER = 1.0;
    public static final double SKILL_INTERMEDIATE_MULTIPLIER = 1.5;
    public static final double SKILL_ADVANCED_MULTIPLIER = 2.5;
    
    public static final int POWER_POTION_PRICE = 150;
    public static final int DEFENSE_POTION_PRICE = 150;
    public static final int EXP_POTION_PRICE = 100;
    public static final int HP_POTION_PRICE = 25;
    public static final int MP_POTION_PRICE = 25;
    
    public static final int POTION_STAT_BOOST = 3;
    public static final int POTION_HP_BOOST = 50;
    public static final int POTION_MP_BOOST = 50;
    public static final int POTION_EXP_BOOST = 50;
    
    public static final int PROGRESS_BAR_SIZE = 10;
    public static final int SPLITTER_LENGTH = 30;
    public static final int NOTIFICATION_PADDING = 2;
    public static final int EXTRA_BORDER_PADDING = 2;
    public static final int PRINT_DELAY = 0;
    public static final double INTERACTABLE_RANGE = 1.0;

    public static int getRequiredExperience(int level) {
        return level >= 1 ? level * 80 : 0;
    }

    public static int getLevelUpReward(int level) {
        return level * 50;
    }

    public static int getMaxHp(int level) {
        return 100 + (level - 1) * 10;
    }

    public static int getMaxMp(int level) {
        return 100 + (level - 1) * 5;
    }

    public static double getCriticalRate(int level) {
        return Math.min(0.5, 0.03 * Math.sqrt(Math.max(0, level)));
    }

    public static int calculateDamage(int power, double multiplier) {
        return (int) (power * multiplier);
    }
    
    public static boolean isCriticalHit(int level) {
        double criticalRate = getCriticalRate(level);
        return criticalRate > random.nextDouble();
    }
    
    public static int applyCritical(int damage) {
        return damage * 5;
    }
    
    public static int applyDefense(int damage, int defense) {
        return Math.max(0, damage - defense);
    }
}