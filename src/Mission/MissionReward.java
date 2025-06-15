package Mission;

import Entity.Hero.Hero;

public class MissionReward {
    private final int experienceReward;
    private final int moneyReward;
    private final String description;
    
    public MissionReward(int experienceReward, int moneyReward) {
        this.experienceReward = experienceReward;
        this.moneyReward = moneyReward;
        this.description = buildDescription();
    }
    
    private String buildDescription() {
        StringBuilder sb = new StringBuilder();
        if (experienceReward > 0) {
            sb.append(experienceReward).append(" 경험치");
        }
        if (moneyReward > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(moneyReward).append("원");
        }
        return sb.toString();
    }
    
    public void giveReward(Hero hero) {
        if (experienceReward > 0) {
            hero.gainExperience(experienceReward);
        }
        if (moneyReward > 0) {
            hero.gainMoney(moneyReward);
        }
    }
    
    public int getExperienceReward() { return experienceReward; }
    public int getMoneyReward() { return moneyReward; }
    public String getDescription() { return description; }
}