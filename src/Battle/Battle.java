package Battle;

import Entity.Hero.Hero;
import Entity.Monster.Monster;
import Game.GameRules;
import Primitive.Skill;
import UI.UI;

public class Battle {
    private final Hero hero;
    private final Monster monster;
    private final UI ui;

    public Battle(Hero hero, Monster monster, UI ui) {
        this.hero = hero;
        this.monster = monster;
        this.ui = ui;
    }

    public void fight() {
        while (hero.isAlive() && monster.isAlive()) {
            heroTurn();

            if (!monster.isAlive()) {
                handleMonsterDeath();
                break;
            }

            ui.println("몬스터 체력 " + formatHp(monster.getHp(), monster.getMaxHp()));
            ui.printSplitter();

            monsterTurn();

            if (!hero.isAlive()) {
                handleHeroDeath();
                break;
            }

            ui.println(hero.getName() + "의 체력 " + formatHp(hero.getHp(), GameRules.getMaxHp(hero.getLevel())));
            ui.printSplitter();
        }
    }

    private void heroTurn() {
        ui.println(hero.getName() + "님의 공격입니다. 사용할 스킬을 선택해주세요.\n\n무기 " + hero.getWeapon().getName() +
                "\n마나 " + formatMp(hero.getMp(), GameRules.getMaxMp(hero.getLevel())));

        Skill[] availableSkills = hero.getAvailableSkills();
        String[] skillOptions = new String[availableSkills.length];

        for (int i = 0; i < availableSkills.length; i++) {
            skillOptions[i] = availableSkills[i].getName() + " [" + availableSkills[i].getMpCost() + " 마나 필요]";
        }

        int skillChoice = ui.promptSelection(skillOptions, "스킬 선택");
        AttackResult result = hero.useSkill(skillChoice);

        double weaponMultiplier = hero.getWeapon().getMultiplier(monster.getName());
        double skillMultiplier = monster.getSkillMultiplier(result.getSkillName());
        int modifiedDamage = (int) (result.getDamage() * weaponMultiplier * skillMultiplier);
        
        int finalDamage = GameRules.applyDefense(modifiedDamage, monster.getDefense());

        monster.takeDamage(finalDamage);

        if (finalDamage <= 0) {
            ui.println("\n" + monster.getName() + "은 피해를 입지 않았습니다.");
        } else {
            if (result.isCritical()) {
                ui.print("\n[크리티컬] ");
            }
            ui.println(finalDamage + "의 피해를 입혔습니다.");
        }
    }

    private void monsterTurn() {
        ui.println(monster.getName() + "의 공격입니다.\n");

        AttackResult result = monster.attack();
        int finalDamage = GameRules.applyDefense(result.getDamage(), hero.getDefense());
        hero.takeDamage(finalDamage);

        if (finalDamage <= 0) {
            ui.println(hero.getName() + "님은 피해를 입지 않았습니다.");
        } else {
            if (result.isCritical()) {
                ui.print("[크리티컬] ");
            }
            ui.println(finalDamage + "의 피해를 입었습니다.");
        }
    }

    private void handleMonsterDeath() {
        ui.println(monster.getName() + "이(가) 죽었습니다.");

        hero.gainExperience(monster.getExperienceReward());
        hero.gainMoney(monster.getMoneyReward());

        ui.println("\n" + monster.getName() + "이(가) 가지고 있던 " +
                monster.getMoneyReward() + "원을 강탈했습니다.");
        ui.println("\n현재 경험치 " + formatExperience(hero.getExperience(), hero.getLevel()));
        ui.printSplitter();
    }

    private void handleHeroDeath() {
        ui.println(monster.getName() + "에게 죽었습니다.");
    }

    private String formatHp(int current, int max) {
        return ui.renderProgress(Math.max(current, 0), max) + " (" + Math.max(current, 0) + "/" + max + ")";
    }

    private String formatMp(int current, int max) {
        return ui.renderProgress(current, max) + " (" + current + "/" + max + ")";
    }

    private String formatExperience(int exp, int level) {
        int required = GameRules.getRequiredExperience(level);
        int previous = GameRules.getRequiredExperience(level - 1);
        return ui.renderProgress(exp - previous, required) + " (" + exp + "/" + required + ")";
    }
}