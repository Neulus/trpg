package Entity.NPC;

import Map.World;
import Dialog.DialogNode;
import Entity.Hero.Hero;
import Mission.Mission;
import Mission.MissionManager;
import UI.UI;

public class TownGuard extends NPC {
    
    public TownGuard(World world) {
        super("마을 경비병", world, "greeting");
    }

    // 오버라이딩해 다이나믹하게 노드 생성
    @Override
    protected void startConversation(Hero hero, UI ui) {
        buildDialogForHero(hero);
        super.startConversation(hero, ui);
    }
    
    private void buildDialogForHero(Hero hero) {
        dialogTree.clear();
        
        int level = hero.getLevel();
        MissionManager missionManager = MissionManager.getInstance();
        String nextMission = missionManager.getNextMissionForLevel(level);
        
        String greetingText = getGreetingForLevel(level);
        DialogNode greeting = new DialogNode("greeting", greetingText);
        greeting.addOption("마을에 대해 알려주세요", "town_info");
        greeting.addOption("할 일이 있나요?", "mission_check");
        greeting.addOption("안녕히 계세요", "goodbye");
        addDialogNode(greeting);
        
        DialogNode townInfo = new DialogNode("town_info", 
            "이곳은 모험가들이 쉬어가는 평화로운 마을입니다. 북쪽에는 사냥터가 있어 " +
            "경험을 쌓고 돈을 벌 수 있습니다. 상점에서는 필요한 물품을 구입할 수 있어요.");
        townInfo.addOption("사냥터는 안전한가요?", "hunting_safety");
        townInfo.addOption("다른 얘기를 들어보겠습니다", "greeting");
        addDialogNode(townInfo);
        
        String huntingSafetyText = getHuntingSafetyForLevel(level);
        DialogNode huntingSafety = new DialogNode("hunting_safety", huntingSafetyText);
        huntingSafety.addOption("알겠습니다", "greeting");
        addDialogNode(huntingSafety);

        if (nextMission != null) {
            Mission mission = missionManager.getMission(nextMission);
            String missionText = getMissionOfferTextForLevel(level, mission);
            DialogNode missionCheck = new DialogNode("mission_check", missionText);
            missionCheck.addOption("네, 도와드리겠습니다", "accept_mission", createMissionGiveAction(nextMission));
            missionCheck.addOption("좀 더 생각해보겠습니다", "thinking");
            missionCheck.addOption("지금은 괜찮습니다", "greeting");
            addDialogNode(missionCheck);
            
            String acceptText = getAcceptMissionTextForLevel(level);
            DialogNode acceptMission = new DialogNode("accept_mission", acceptText, true);
            addDialogNode(acceptMission);
        } else {
            String noMissionText = getNoMissionTextForLevel(level);
            DialogNode missionCheck = new DialogNode("mission_check", noMissionText);
            missionCheck.addOption("알겠습니다", "greeting");
            addDialogNode(missionCheck);
        }
        
        DialogNode thinking = new DialogNode("thinking", 
            "물론입니다. 충분히 생각해보세요. 준비가 되시면 언제든 말씀해주세요.");
        thinking.addOption("돌아가겠습니다", "greeting");
        addDialogNode(thinking);
        
        DialogNode goodbye = new DialogNode("goodbye", 
            "안녕히 가십시오. 마을의 평화를 위해 항상 여기 있겠습니다.", true);
        addDialogNode(goodbye);
    }
    
    private String getGreetingForLevel(int level) {
        if (level == 1) {
            return "어서 오세요, 새로운 모험가님! 이곳은 처음이신가요? " +
                   "마을에 적응할 수 있도록 도와드리겠습니다.";
        } else if (level <= 3) {
            return "안녕하세요, 모험가님! 마을 생활에는 적응하셨나요? " +
                   "아직 해볼 만한 일들이 있을 텐데요.";
        } else if (level <= 7) {
            return "오, 꽤 경험을 쌓으신 모험가군요! 이제 좀 더 도전적인 " +
                   "미션을 맡기실 수 있을 것 같습니다.";
        } else if (level <= 12) {
            return "훌륭하십니다! " + level + "레벨이라니, 이제 베테랑 모험가시네요. " +
                   "마을에서 가장 어려운 미션들도 맡기실 수 있겠어요.";
        } else {
            return "대단하십니다! " + level + "레벨의 전설적인 모험가를 뵙게 되다니... " +
                   "당신 같은 분이라면 어떤 위험한 미션이라도 해낼 수 있을 것입니다!";
        }
    }
    
    private String getHuntingSafetyForLevel(int level) {
        if (level <= 3) {
            return "초보 모험가에게는 약간 위험할 수 있어요. 너구리 정도는 " +
                   "상대할 수 있지만, 항상 조심하시고 무리하지 마세요.";
        } else if (level <= 7) {
            return "당신 정도 실력이라면 웬만한 동물들은 문제없이 상대하실 수 있어요. " +
                   "하지만 깊은 곳에는 더 위험한 존재들이 있으니 주의하세요.";
        } else if (level <= 12) {
            return "당신 실력이라면 사냥터 대부분은 안전할 겁니다. " +
                   "하지만 최근 더 강한 몬스터들이 나타나고 있어서 방심은 금물이에요.";
        } else {
            return "당신 정도 실력이라면 사냥터는 산책로나 다름없겠네요. " +
                   "오히려 몬스터들이 당신을 무서워할 것 같습니다!";
        }
    }
    
    private String getMissionOfferTextForLevel(int level, Mission mission) {
        String baseText = mission.getDescription();
        if (level <= 3) {
            return "마침 좋습니다! 초보 모험가분께 딱 맞는 미션이 있어요. " +
                   baseText + " 도와주시겠어요?";
        } else if (level <= 7) {
            return "좋은 타이밍이네요! 당신 정도 실력의 모험가가 필요한 미션이 있습니다. " +
                   baseText + " 어떠신가요?";
        } else if (level <= 12) {
            return "완벽한 타이밍입니다! 베테랑 모험가만 할 수 있는 중요한 미션이 있어요. " +
                   baseText + " 맡아주시겠습니까?";
        } else {
            return "전설적인 모험가님! 사실 아무에게나 부탁할 수 없는 극비 미션이 있습니다. " +
                   baseText + " 당신이라면 가능하실까요?";
        }
    }
    
    private String getAcceptMissionTextForLevel(int level) {
        if (level <= 3) {
            return "훌륭합니다! 처음이니 무리하지 마시고 차근차근 해보세요. " +
                   "미션을 완수하고 무사히 돌아오시기를 기다리겠습니다!";
        } else if (level <= 7) {
            return "역시 믿을 만한 모험가시군요! 당신이라면 분명 성공하실 거예요. " +
                   "조심히 다녀오세요!";
        } else if (level <= 12) {
            return "베테랑다운 결단이네요! 당신의 실력이라면 걱정할 것도 없겠어요. " +
                   "성공적인 완수를 기대하겠습니다!";
        } else {
            return "역시 전설은 다르군요! 이런 위험한 미션도 서슴없이 받아주시다니... " +
                   "온 마을이 당신을 믿고 있습니다!";
        }
    }
    
    private String getNoMissionTextForLevel(int level) {
        if (level <= 3) {
            return "음... 지금은 당신에게 맞는 적당한 미션이 없네요. " +
                   "좀 더 경험을 쌓으시면 새로운 기회가 있을 거예요.";
        } else if (level <= 7) {
            return "현재로는 당신 수준에 맞는 새로운 미션이 준비되지 않았습니다. " +
                   "조금 더 기다려 주시거나, 다른 활동을 해보시는 것은 어떨까요?";
        } else {
            return "죄송합니다. 당신 같은 고수에게 어울리는 미션을 찾기가 쉽지 않네요. " +
                   "새로운 도전거리가 생기면 바로 연락드리겠습니다.";
        }
    }
}