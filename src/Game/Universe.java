package Game;

import Entity.Entity;
import Entity.Hero.Hero;
import Map.HuntingWorld;
import Map.TownWorld;
import Map.World;
import Primitive.Direction;
import Primitive.Position;
import Primitive.VisibleEntity;
import UI.UI;
import java.util.ArrayList;
import java.util.List;

public class Universe {

    private final UI ui;
    private final Hero hero;

    private TownWorld town;
    private HuntingWorld hunt;
    private World currentWorld;
    private Position position;
    private Direction facing;

    public Universe(UI ui, Hero hero) {
        this.ui = ui;
        this.hero = hero;
    }

    public TownWorld getTownWorld() {
        return town;
    }

    public HuntingWorld getHuntingWorld() {
        return hunt;
    }

    public void start() {
        town = new TownWorld();
        hunt = new HuntingWorld();

        town.setUI(ui);
        town.setUniverse(this);
        hunt.setUI(ui);
        hunt.setUniverse(this);

        town.initCells();
        hunt.initCells();

        switchTo(town);
        gameLoop();
    }

    public void switchTo(World newWorld) {
        this.currentWorld = newWorld;
        this.position = newWorld.getStartingPosition();
        this.facing = newWorld.getSpawnFacing();

        hero.setWorld(newWorld);

        if (newWorld == town) {
            ui.println("마을로 돌아갑니다.");
        } else {
            ui.println("사냥터에 입장합니다.");
        }
        ui.printSplitter();
    }

    private void gameLoop() {
        while (true) {
            List<String> options = new ArrayList<>();
            List<Runnable> tasks = new ArrayList<>();

            for (Direction dir : Direction.values()) {
                Position next = position.move(dir);
                if (currentWorld.isWalkable(next)) {
                    String relativeDirection = getRelativeDirection(facing, dir);
                    options.add(relativeDirection + "로 이동");
                    tasks.add(() -> {
                        this.position = next;
                        this.facing = dir;
                    });
                }
            }

            List<VisibleEntity> visibles = currentWorld.getVisibleEntities(
                position,
                facing
            );
            for (VisibleEntity ve : visibles) {
                if (ve.getVisibility() > GameRules.INTERACTABLE_RANGE) continue;
                Entity entity = ve.getEntity();
                options.add(ve.getAngle() + "에 " + entity.getName());
                tasks.add(() -> entity.interact(this.hero));
            }

            options.add("상태 확인");
            tasks.add(() -> ui.showHeroStatus(hero, true));
            options.add("미션 확인");
            tasks.add(() -> ui.showMessage(hero.getMissionLog()));
            options.add("지도 보기");
            tasks.add(() -> ui.displayMap(currentWorld, position, facing));

            int choice = ui.promptSelectionWithHUD(
                options.toArray(new String[0]),
                "행동을 선택하세요",
                    currentWorld,
                    position,
                    facing
            );
            tasks.get(choice).run();
        }
    }

    private String getRelativeDirection(Direction playerFacing, Direction moveDirection) {
        int facingValue = playerFacing.getValue();
        int moveValue = moveDirection.getValue();
        int relativeDiff = (moveValue - facingValue + 4) % 4;
        
        return switch (relativeDiff) {
            case 0 -> "앞으";
            case 1 -> "우측으";
            case 2 -> "뒤";
            case 3 -> "좌측으";
            default -> "알 수 없는 방향으";
        };
    }
}