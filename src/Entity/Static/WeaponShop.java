package Entity.Static;

import Entity.Entity;
import Entity.Hero.Hero;
import Map.World;
import Primitive.Weapon;
import UI.UI;

import java.util.List;
import java.util.ArrayList;

public class WeaponShop extends Entity {
    private final UI ui;

    public WeaponShop(World world, UI ui) {
        super("무기상점", false, true, world);
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
        List<Weapon> availableWeapons = new ArrayList<>();

        for (Weapon weapon : Weapon.values()) {
            if (weapon != Weapon.NONE && 
                weapon.canBeUsedBy(hero.getClassName()) &&
                hero.getMoney() >= weapon.getPrice()) {
                availableOptions.add(weapon.getKoreanName() + " (공격력: +" + weapon.getDamage() + ", 가격: " + weapon.getPrice() + "원)");
                availableWeapons.add(weapon);
            }
        }

        availableOptions.add("나가기");
        availableWeapons.add(null);

        ui.println("현재 돈: " + hero.getMoney() + "원");
        ui.println("현재 무기: " + hero.getWeapon().getKoreanName() + " (공격력: +" + hero.getWeapon().getDamage() + ")\n");

        int selected = ui.promptSelection(availableOptions.toArray(new String[0]), "");
        Weapon selectedWeapon = availableWeapons.get(selected);

        if (selectedWeapon == null) {
            return;
        }

        purchaseWeapon(hero, selectedWeapon);
        ui.printSplitter();
    }

    private void purchaseWeapon(Hero hero, Weapon weapon) {
        int price = weapon.getPrice();
        hero.spendMoney(price);
        hero.equipWeapon(weapon);

        ui.println(weapon.getKoreanName() + "을(를) 구매하고 장착했습니다!");
        ui.println(price + "원 사용했습니다. 남은 돈: " + hero.getMoney() + "원");
        ui.println("새로운 무기 공격력: +" + weapon.getDamage());
    }
}