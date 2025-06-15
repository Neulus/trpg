package Entity.NPC;

import Dialog.DialogNode;
import Dialog.DialogOption;
import Entity.Entity;
import Entity.Hero.Hero;
import Map.World;
import Mission.*;
import UI.UI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NPC extends Entity {
    protected final Map<String, DialogNode> dialogTree;
    protected String currentDialogNode;
    protected final String startingNodeId;
    protected boolean hasMetHero;
    
    public NPC(String name, World world, String startingNodeId) {
        super(name, false, true, world);
        this.dialogTree = new HashMap<>();
        this.startingNodeId = startingNodeId;
        this.currentDialogNode = startingNodeId;
        this.hasMetHero = false;
        initDialog();
    }

    // 오버라이딩 용
    protected void initDialog() {
    }
    
    protected void addDialogNode(DialogNode node) {
        dialogTree.put(node.getId(), node);
    }
    
    @Override
    public boolean isInteractable() {
        return true;
    }
    
    @Override
    public void interact(Entity entity) {
        if (!(entity instanceof Hero)) return;

        Hero hero = (Hero) entity;
        UI ui = world.getUI();
        
        startConversation(hero, ui);
    }

    protected void startConversation(Hero hero, UI ui) {
        currentDialogNode = startingNodeId;
        continueConversation(hero, ui);
    }
    
    private void continueConversation(Hero hero, UI ui) {
        DialogNode node = dialogTree.get(currentDialogNode);
        if (node == null) {
            ui.showMessage("[대화 노드 없음; 축하합니다. 버그를 발견하셨습니다.]");
            return;
        }
        
        ui.showMessage(name + ": " + node.getNpcText());
        
        if (node.endsConversation() || node.getOptions().isEmpty()) {
            ui.showMessage("[대화 종료됨]");
            return;
        }
        
        List<DialogOption> options = node.getOptions();
        String[] optionTexts = new String[options.size()];
        
        for (int i = 0; i < options.size(); i++) {
            optionTexts[i] = options.get(i).getText();
        }
        
        int choice = ui.promptSelection(optionTexts, "대화 선택");
        if (choice >= 0 && choice < options.size()) {
            DialogOption selectedOption = options.get(choice);
            
            if (selectedOption.getAction() != null) {
                selectedOption.getAction().accept(hero);
            }
            
            String nextNodeId = selectedOption.getNextNodeId();
            if (nextNodeId != null && !nextNodeId.equals("END")) {
                currentDialogNode = nextNodeId;
                continueConversation(hero, ui);
            } else {
                ui.showMessage("[대화 종료됨]");
            }
        }
    }

    protected Consumer<Hero> createMissionGiveAction(String missionId) {
        return hero -> {
            MissionManager missionManager = MissionManager.getInstance();
            if (missionManager.acceptMission(missionId, hero)) {
                world.getUI().showMessage("[미션 추가됨: " + missionManager.getMission(missionId).getTitle() + "]");
            } else {
                world.getUI().showMessage("[이미 이 미션이 있습니다.]");
            }
        };
    }
}