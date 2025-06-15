package Mission;

import Entity.Hero.Hero;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionManager {
    private static MissionManager instance;
    private final Map<String, Mission> allMissions;
    private final List<Mission> activeMissions;
    private final List<Mission> completedMissions;
    
    private MissionManager() {
        allMissions = new HashMap<>();
        activeMissions = new ArrayList<>();
        completedMissions = new ArrayList<>();
        initMissions();
    }
    
    public static MissionManager getInstance() {
        if (instance == null) {
            instance = new MissionManager();
        }
        return instance;
    }
    
    private void initMissions() {
        Mission racoonMission = new Mission("kill_racoons", "너구리 소탕", 
            "마을 근처에 너구리들이 농작물을 훔쳐가고 있습니다. 너구리 3마리를 처치해주세요.", 
            new MissionReward(50, 30));
        racoonMission.addObjective(Mission.ObjectiveType.KILL_MONSTERS, "너구리", 3);
        
        Mission wildcatMission = new Mission("hunt_wildcats", "살퀭이 사냥", 
            "위험한 살퀭이들이 사냥터에 나타났습니다. 살퀭이 2마리를 처치하세요.", 
            new MissionReward(120, 75));
        wildcatMission.addObjective(Mission.ObjectiveType.KILL_MONSTERS, "살퀭이", 2);
        
        Mission snakeMission = new Mission("slay_snake", "독사 퇴치", 
            "독성이 강한 뱀이 사냥터를 위험하게 만들고 있습니다. 뱀 1마리를 처치하세요.", 
            new MissionReward(200, 150));
        snakeMission.addObjective(Mission.ObjectiveType.KILL_MONSTERS, "뱀", 1);
        
        Mission bearMission = new Mission("slay_bear", "거대한 곰", 
            "거대한 곰이 사냥꾼들을 위협하고 있습니다. 이 위험한 짐승을 처치하세요.", 
            new MissionReward(500, 300));
        bearMission.addObjective(Mission.ObjectiveType.KILL_MONSTERS, "곰", 1);
        
        Mission beginnerMission = new Mission("first_hunt", "첫 사냥", 
            "사냥터에서 몬스터 하나를 처치하여 사냥의 기본을 익혀보세요.", 
            new MissionReward(25, 15));
        beginnerMission.addObjective(Mission.ObjectiveType.KILL_MONSTERS, "너구리", 1);
        
        Mission veteranMission = new Mission("veteran_hunter", "숙련된 사냥꾼", 
            "다양한 몬스터들을 처치하여 숙련된 사냥꾼임을 증명하세요.", 
            new MissionReward(300, 200));
        veteranMission.addObjective(Mission.ObjectiveType.KILL_MONSTERS, "살퀭이", 3);
        veteranMission.addObjective(Mission.ObjectiveType.KILL_MONSTERS, "뱀", 2);

        allMissions.put(beginnerMission.getId(), beginnerMission);
        allMissions.put(racoonMission.getId(), racoonMission);
        allMissions.put(wildcatMission.getId(), wildcatMission);
        allMissions.put(snakeMission.getId(), snakeMission);
        allMissions.put(bearMission.getId(), bearMission);
        allMissions.put(veteranMission.getId(), veteranMission);
    }
    
    public Mission getMission(String missionId) {
        return allMissions.get(missionId);
    }
    
    public boolean acceptMission(String missionId, Hero hero) {
        Mission mission = allMissions.get(missionId);
        if (mission != null && mission.getStatus() == Mission.MissionStatus.AVAILABLE) {
            mission.activate();
            activeMissions.add(mission);
            return true;
        }
        return false;
    }
    
    public void updateMissionProgress(Mission.ObjectiveType type, String target, Hero hero) {
        List<Mission> missionsToComplete = new ArrayList<>();
        
        for (Mission mission : activeMissions) {
            if (mission.updateObjective(type, target)) {
                if (mission.getStatus() == Mission.MissionStatus.COMPLETED) {
                    missionsToComplete.add(mission);
                }
            }
        }
        
        for (Mission mission : missionsToComplete) {
            completeMission(mission, hero);
        }
    }
    
    private void completeMission(Mission mission, Hero hero) {
        activeMissions.remove(mission);
        completedMissions.add(mission);
        mission.giveReward(hero);

        if (hero.getWorld() != null && hero.getWorld().getUI() != null) {
            hero.getWorld().getUI().showMessage("미션 완료: " + mission.getTitle());
            hero.getWorld().getUI().showMessage("보상: " + mission.getReward().getDescription());
        }
    }
    
    public List<Mission> getActiveMissions() {
        return new ArrayList<>(activeMissions);
    }
    
    public List<Mission> getCompletedMissions() {
        return new ArrayList<>(completedMissions);
    }
    
    public List<Mission> getAvailableMissions() {
        List<Mission> available = new ArrayList<>();
        for (Mission mission : allMissions.values()) {
            if (mission.getStatus() == Mission.MissionStatus.AVAILABLE) {
                available.add(mission);
            }
        }
        return available;
    }
    
    public String getMissionLogDisplay() {
        StringBuilder missionLog = new StringBuilder();
        missionLog.append("=== 미션들 ===\n\n");
        
        if (!activeMissions.isEmpty()) {
            missionLog.append("현재 미션:\n");
            for (Mission mission : activeMissions) {
                missionLog.append(mission.render()).append("\n\n");
            }
        }
        
        if (!completedMissions.isEmpty()) {
            missionLog.append("완료한 미션:\n");
            for (Mission mission : completedMissions) {
                missionLog.append("🌙 ").append(mission.getTitle()).append("\n");
            }
        }
        
        if (activeMissions.isEmpty() && completedMissions.isEmpty()) {
            missionLog.append("아직 미션이 없습니다. 사람들과 이야기해보세요.");
        }
        
        return missionLog.toString();
    }
    
    public List<Mission> getAvailableMissionsForLevel(int heroLevel) {
        List<Mission> levelAppropriate = new ArrayList<>();
        for (Mission mission : allMissions.values()) {
            if (mission.getStatus() == Mission.MissionStatus.AVAILABLE && isMissionAppropriateForLevel(mission.getId(), heroLevel)) {
                levelAppropriate.add(mission);
            }
        }
        return levelAppropriate;
    }
    
    private boolean isMissionAppropriateForLevel(String missionId, int heroLevel) {
        return switch (missionId) {
            case "first_hunt" -> heroLevel >= 1 && heroLevel <= 2;
            case "kill_racoons" -> heroLevel >= 2 && heroLevel <= 5;
            case "hunt_wildcats" -> heroLevel >= 5 && heroLevel <= 10;
            case "slay_snake" -> heroLevel >= 10 && heroLevel <= 15;
            case "slay_bear" -> heroLevel >= 15;
            case "veteran_hunter" -> heroLevel >= 12;
            default -> true;
        };
    }
    
    public String getNextMissionForLevel(int heroLevel) {
        List<Mission> available = getAvailableMissionsForLevel(heroLevel);
        if (!available.isEmpty()) {
            return available.get(0).getId();
        }
        return null;
    }
}