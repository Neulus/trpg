package Entity.Monster;

import Battle.AttackResult;
import Battle.Battle;
import Entity.Character;
import Entity.Entity;
import Entity.Hero.Hero;
import Game.GameRules;
import Map.World;
import Primitive.Position;
import Primitive.Weapon;
import Mission.Mission;
import Mission.MissionManager;
import UI.UI;

public class Monster extends Character {

    private final int experienceReward;
    private final int moneyReward;

    protected Monster(String name, int level, int power, int hp, int defense,
                   int mp, int experienceReward, int moneyReward, World world) {
        super(name, level, power, defense, hp, hp, mp, mp, world);
        this.experienceReward = experienceReward;
        this.moneyReward = moneyReward;
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Hero hero) {
            UI ui = world.getUI();
            
            ui.println(this.getName() + "와(과) 전투를 시작합니다!");
            new Battle(hero, this, ui).fight();
            if (!hero.isAlive()) {
                hero.resurrect();
                ui.println(
                    "모든 레벨과 경험치, 스텟, 포션 효과 등을 잃었습니다."
                );
                ui.println("[죽은 자리에서 부활합니다.]");
            }
            
            if (hero.isAlive()) {
                MissionManager.getInstance().updateMissionProgress(Mission.ObjectiveType.KILL_MONSTERS, this.getName(), hero);
            }
            
            clearFromWorld();
            ui.printSplitter();
        }
    }
    
    private void clearFromWorld() {
        Entity[][] cells = world.getCells();
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                if (cells[y][x] == this) {
                    world.clearCell(new Position(x, y));
                    return;
                }
            }
        }
    }

    @Override
    public AttackResult attack() {
        int baseDamage = GameRules.calculateDamage(power, 1.0);
        boolean isCritical = GameRules.isCriticalHit(level);
        
        if (isCritical) {
            baseDamage = GameRules.applyCritical(baseDamage);
        }
        
        return new AttackResult(baseDamage, isCritical, "공격");
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public int getMoneyReward() {
        return moneyReward;
    }

    public double getSkillMultiplier(String skillName) {
        return 1.0;
    }
}
