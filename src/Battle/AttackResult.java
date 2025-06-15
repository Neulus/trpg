package Battle;

public class AttackResult {
    private final int damage;
    private final boolean critical;
    private final String skillName;

    public AttackResult(int damage, boolean critical, String skillName) {
        this.damage = damage;
        this.critical = critical;
        this.skillName = skillName;
    }

    public int getDamage() { return damage; }
    public boolean isCritical() { return critical; }
    public String getSkillName() { return skillName; }
}
