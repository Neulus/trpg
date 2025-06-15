package Mission;

import Entity.Hero.Hero;
import java.util.ArrayList;
import java.util.List;

public class Mission {
    public enum MissionStatus {
        AVAILABLE,
        ACTIVE,
        COMPLETED,
    }
    
    public enum ObjectiveType {
        KILL_MONSTERS,
    }
    
    private final String id;
    private final String title;
    private final String description;
    private MissionStatus status;
    private final List<MissionObjective> objectives;
    private final MissionReward reward;
    
    public Mission(String id, String title, String description, MissionReward reward) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = MissionStatus.AVAILABLE;
        this.objectives = new ArrayList<>();
        this.reward = reward;
    }
    
    public void addObjective(ObjectiveType type, String target, int requiredAmount) {
        objectives.add(new MissionObjective(type, target, requiredAmount));
    }
    
    public void activate() {
        if (status == MissionStatus.AVAILABLE) {
            status = MissionStatus.ACTIVE;
        }
    }
    
    public void complete() {
        if (status == MissionStatus.ACTIVE && areAllObjectivesComplete()) {
            status = MissionStatus.COMPLETED;
        }
    }
    
    public boolean updateObjective(ObjectiveType type, String target) {
        boolean updated = false;
        for (MissionObjective objective : objectives) {
            if (objective.getType() == type && objective.getTarget().equals(target)) {
                objective.incrementProgress();
                updated = true;
            }
        }
        
        if (updated && areAllObjectivesComplete()) {
            complete();
        }
        
        return updated;
    }

    public boolean areAllObjectivesComplete() {
        for (MissionObjective objective : objectives) {
            if (!objective.isComplete()) {
                return false;
            }
        }
        return true;
    }
    
    public void giveReward(Hero hero) {
        if (status == MissionStatus.COMPLETED && reward != null) {
            reward.giveReward(hero);
        }
    }
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public MissionStatus getStatus() { return status; }
    public List<MissionObjective> getObjectives() { return objectives; }
    public MissionReward getReward() { return reward; }
    
    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(" (").append(status).append(")\n");
        sb.append(description).append("\n");
        
        for (MissionObjective objective : objectives) {
            sb.append("- ").append(objective.getDescription()).append("\n");
        }
        
        if (reward != null) {
            sb.append("보상: ").append(reward.getDescription());
        }
        
        return sb.toString();
    }
}