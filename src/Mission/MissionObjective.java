package Mission;

public class MissionObjective {
    private final Mission.ObjectiveType type;
    private final String target;
    private final int requiredAmount;
    private int currentProgress;
    
    public MissionObjective(Mission.ObjectiveType type, String target, int requiredAmount) {
        this.type = type;
        this.target = target;
        this.requiredAmount = requiredAmount;
        this.currentProgress = 0;
    }
    
    public void incrementProgress() {
        if (currentProgress < requiredAmount) {
            currentProgress++;
        }
    }
    
    public boolean isComplete() {
        return currentProgress >= requiredAmount;
    }
    
    public String getDescription() {
        String description = "";
        switch (type) {
            case KILL_MONSTERS:
                description = target + " " + requiredAmount + "마리 정리하기";
                break;
        }
        
        if (requiredAmount > 1) {
            description += " (" + currentProgress + "/" + requiredAmount + ")";
        } else {
            description += isComplete() ? " ✓" : "";
        }
        
        return description;
    }
    
    public Mission.ObjectiveType getType() { return type; }
    public String getTarget() { return target; }
    public int getRequiredAmount() { return requiredAmount; }
    public int getCurrentProgress() { return currentProgress; }
}