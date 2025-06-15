package UI;

import java.util.Scanner;
import java.util.List;
import Entity.Hero.Hero;
import Game.GameRules;
import Map.World;
import Primitive.Direction;
import Primitive.Position;
import Primitive.VisibleEntity;

import static Game.GameRules.*;

public class UI {

    private final Scanner scanner;

    public UI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void print(String content) {
        String[] pieces = content.split("");
        for (String piece : pieces) {
            System.out.print(piece);
            sleep(PRINT_DELAY);
        }
    }

    public void println(String content) {
        print(content + "\n");
    }

    public void printSplitter() {
        for (int i = 0; i < SPLITTER_LENGTH; i++) {
            System.out.print("—");
        }
        System.out.println();
    }

    public String promptString(String prompt) {
        print(prompt);
        return scanner.next();
    }

    public void showVisibleEntities(World world, Position heroPos, Direction direction) {
        List<VisibleEntity> visibleEntities = world.getVisibleEntities(heroPos, direction);
        
        if (!visibleEntities.isEmpty()) {
            println("주변 상황:");
            for (VisibleEntity visibleEntity : visibleEntities) {
                println("  " + visibleEntity.render());
            }
            printSplitter();
        }
    }

    public int promptSelection(String[] options, String prompt) {
        for (int i = 0; i < options.length; i++) {
            println((i + 1) + ". " + options[i]);
        }

        int selection = -1;
        while (selection <= 0 || selection > options.length) {
            if (selection != -1) {
                println("\n[" + selection + "번은 올바른 선택지가 아닙니다.]");
            }

            print("\n" + prompt + ": ");
            selection = scanner.nextInt();
        }

        return selection - 1;
    }
    
    public void showMessage(String message) {
        println(message);
    }

    public int promptSelectionWithHUD(String[] options, String prompt, World world, Position heroPos, Direction direction) {
        showVisibleEntities(world, heroPos, direction);
        return promptSelection(options, prompt);
    }

    public void notification(String title, String content, boolean boxed) {
        String[] contents = content.split("\n");

        int maxLength = title.length();
        for (String line : contents) {
            maxLength = Math.max(maxLength, line.length());
        }
        maxLength += NOTIFICATION_PADDING + EXTRA_BORDER_PADDING;

        if (boxed) {
            printSplitter();
        }

        printWithPadding(title);
        println("");

        for (String line : contents) {
            printWithPadding(line);
        }

        if (boxed) {
            printSplitter();
        }
    }

    private void printWithPadding(String content) {
        for (int i = 0; i < NOTIFICATION_PADDING; i++) {
            print(" ");
        }
        println(content);
    }

    public void showHeroStatus(Hero hero, boolean boxed) {
        String statusText = String.format(
                "　레벨 %d\n" +
                        "경험치 %s (%d/%d)\n" +
                        "　　힘 %d\n" +
                        "방어력 %d\n" +
                        "　체력 %s (%d/%d)\n" +
                        "　마나 %s (%d/%d)\n" +
                        "　무기 %s\n" +
                        "　　돈 %d원",
                hero.getLevel(),
                renderProgress(hero.getExperience() - GameRules.getRequiredExperience(hero.getLevel() - 1),
                        GameRules.getRequiredExperience(hero.getLevel())),
                hero.getExperience(), GameRules.getRequiredExperience(hero.getLevel()),
                hero.getPower(),
                hero.getDefense(),
                renderProgress(Math.max(hero.getHp(), 0), GameRules.getMaxHp(hero.getLevel())),
                Math.max(hero.getHp(), 0), GameRules.getMaxHp(hero.getLevel()),
                renderProgress(hero.getMp(), GameRules.getMaxMp(hero.getLevel())),
                hero.getMp(), GameRules.getMaxMp(hero.getLevel()),
                hero.getWeapon().getName(),
                hero.getMoney()
        );

        notification("　　　 " + hero.getName(), statusText, boxed);
    }

    public String renderProgress(int value, int maximum) {
        StringBuilder progress = new StringBuilder("[");
        for (int i = 0; i < GameRules.PROGRESS_BAR_SIZE; i++) {
            progress.append((double) i / GameRules.PROGRESS_BAR_SIZE >= (double) value / maximum ? " " : "=");
        }
        progress.append("]");
        return progress.toString();
    }

    public void displayMap(World map, Position heroPos, Direction direction) {
        println("지도\n#:벽 -:길 P:포션상점 W:무기상점 S:출구 G:입구 N:NPC W:너구리 X:살퀭이 Y:뱀 Z:곰 ↑→↓←:플레이어");

        char heroIcon = switch (direction) {
            case NORTH -> '↑';
            case EAST -> '→';
            case SOUTH -> '↓';
            case WEST -> '←';
        };

        String[] mapData = map.getMapData();
        for (int y = 0; y < mapData.length; y++) {
            if (y == heroPos.getY()) {
                char[] row = mapData[y].toCharArray();
                row[heroPos.getX()] = heroIcon;
                System.out.println(new String(row));
            } else {
                System.out.println(mapData[y]);
            }
        }

        printSplitter();
    }


    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}