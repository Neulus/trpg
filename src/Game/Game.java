package Game;

import Entity.Hero.Hero;
import Entity.Hero.Warrior;
import Entity.Hero.Magician;
import Entity.Hero.Archer;
import UI.UI;
import java.util.Scanner;

public class Game {

    private final UI ui;
    private final Scanner scanner;
    private Hero hero;
    private Universe universe;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.ui = new UI(scanner);
    }

    public void start() {
        initHero();
        startUniverse();
    }

    private void initHero() {
        ui.println("환영합니다!\n");
        String heroName = ui.promptString("캐릭터의 이름을 정해주세요: ");
        
        String[] classOptions = {
            "전사",
            "마법사",
            "궁수"
        };
        
        int classChoice = ui.promptSelection(classOptions, "직업을 선택하세요");
        
        switch (classChoice) {
            case 0:
                hero = new Warrior(heroName, null);
                ui.println("전사를 선택하셨습니다!");
                break;
            case 1:
                hero = new Magician(heroName, null);
                ui.println("마법사를 선택하셨습니다!");
                break;
            case 2:
                hero = new Archer(heroName, null);
                ui.println("궁수를 선택하셨습니다!");
                break;
            default:
                hero = new Warrior(heroName, null);
                ui.println("전사로 설정됩니다.");
                break;
        }
        
        ui.println("안녕하세요, " + hero.getName() + "님.\n");
        ui.showHeroStatus(hero, false);
        ui.println("\n게임을 시작합니다!");
    }

    private void startUniverse() {
        universe = new Universe(ui, hero);
        universe.start();
    }
}
