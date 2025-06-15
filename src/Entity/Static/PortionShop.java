package Entity.Static;

import Entity.Entity;
import Entity.Hero.Hero;
import Game.GameRules;
import Map.World;
import UI.UI;

import java.util.List;
import java.util.ArrayList;

public class PortionShop extends Entity {
    private static final String[] POTION_NAMES = {
            "힘 증가 포션", "방어력 증가 포션", "경험치 포션", "HP 증가 포션", "MP 증가 포션"
    };

    private static final int[] POTION_PRICES = {
            GameRules.POWER_POTION_PRICE, GameRules.DEFENSE_POTION_PRICE, 
            GameRules.EXP_POTION_PRICE, GameRules.HP_POTION_PRICE, GameRules.MP_POTION_PRICE
    };

    private final UI ui;

    public PortionShop(World world, UI ui) {
        super("포션 상점", false, true, world);
        this.ui = ui;
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Hero hero) {
            showShop(hero);
        }
    }

    private void showShop(Hero hero) {
        List<String> availableOptions = new ArrayList<>();
        List<Integer> potionIndices = new ArrayList<>();

        for (int i = 0; i < POTION_NAMES.length; i++) {
            if (hero.getMoney() >= POTION_PRICES[i]) {
                availableOptions.add(POTION_NAMES[i] + " (" + POTION_PRICES[i] + "원)");
                potionIndices.add(i);
            }
        }

        availableOptions.add("나가기");
        potionIndices.add(-1);

        ui.println("현재 돈: " + hero.getMoney() + "원\n");

        int selected = ui.promptSelection(availableOptions.toArray(new String[0]), "");
        int potionIndex = potionIndices.get(selected);

        if (potionIndex == -1) {
            return;
        }

        purchasePotion(hero, potionIndex);
        ui.printSplitter();
    }

    private void purchasePotion(Hero hero, int index) {
        int price = POTION_PRICES[index];
        hero.spendMoney(price);

        ui.println(POTION_NAMES[index] + "을(를) 구매했습니다!");
        ui.println(price + "원 사용했습니다. 남은 돈: " + hero.getMoney() + "원");

        applyPotionEffect(hero, index);
    }

    private void applyPotionEffect(Hero hero, int index) {
        switch (index) {
            case 0 -> {
                hero.increasePower(GameRules.POTION_STAT_BOOST);
                ui.println("힘이 " + GameRules.POTION_STAT_BOOST + " 증가했습니다!\n현재 힘: " + hero.getPower());
            }
            case 1 -> {
                hero.increaseDefense(GameRules.POTION_STAT_BOOST);
                ui.println("방어력이 " + GameRules.POTION_STAT_BOOST + " 증가했습니다!\n현재 방어력: " + hero.getDefense());
            }
            case 2 -> {
                hero.gainExperience(GameRules.POTION_EXP_BOOST);
                ui.println("경험치가 " + GameRules.POTION_EXP_BOOST + " 증가했습니다!");
                ui.println("현재 경험치: " + hero.getExperience() + "/" +
                        GameRules.getRequiredExperience(hero.getLevel()));
            }
            case 3 -> {
                hero.increaseHp(GameRules.POTION_HP_BOOST);
                ui.println("체력이 " + GameRules.POTION_HP_BOOST + " 증가했습니다! (임시)\n현재 체력: " + hero.getHp() + "/" +
                        GameRules.getMaxHp(hero.getLevel()));
            }
            case 4 -> {
                hero.increaseMp(GameRules.POTION_MP_BOOST);
                ui.println("마나가 " + GameRules.POTION_MP_BOOST + " 증가했습니다! (임시)\n현재 마나: " + hero.getMp() + "/" +
                        GameRules.getMaxMp(hero.getLevel()));
            }
        }
    }
}