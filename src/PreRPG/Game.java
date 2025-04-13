package PreRPG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
	static final int SPLITTER_LENGTH = 30;
	static final int PROGRESS_SIZE = 10;
	static final int NOTIFICATION_PADDING = 2;
	static final int EXTRA_BORDER_PADDING = 2;
	static final int PRINT_DELAY = 25;
	static final int SPAWN_PROTECTION_RANGE = 3;
	static final int MONSTER_COUNT = 50;

	static final String[] HUNTING_MAP = { "#########################", "#-#---#---#-------------#",
			"#-###-#-#-#-#####-###-#-#", "#---#---#---#---#---#-#-#", "###-#-#######-#-#####-#-#",
			"#---#-#---#---#-------#-#", "#-###-#-#-#-###########-#", "#-#-#-#-#-#-----#---#---#",
			"#-#-#-###-#####-###-#-###", "#-#-#---------#---#-#-#-#", "#-#-#-###########-#-#-#-#",
			"#-#-#-#---------#-#-#-#-#", "#-#-#-#-#######-#-#-#-#-#", "#-#---#-----#-#---#-#-#-#",
			"#-#########-#-#####-#-#-#", "#-----------#-------#-#-#", "#############-#####-#-#-#",
			"#-----------#---#-#-#---#", "#-###-#####-#-#-#-#-###-#", "#-#---#---#-#-#-#---#---#",
			"#-#-###-#-#-#-#-###-#-###", "#-#-#---#-#-#-#---#-#-#-#", "#-#-#-###-###-###-###-#-#",
			"#-#-----#-------#-------#", "#######################S#" };
	static final int[] HUNTING_MAP_STARTING_POINT = { 23, 23 };
	static final String[] TOWN_MAP = { "###G#", "#---#", "#P###" };
	static final int[] TOWN_MAP_STARTING_POINT = { 3, 1 };
	static final int EYESIGHT = 2;

	static final Scanner SCANNER = new Scanner(System.in);

	static String hero_name;
	static int hero_level, hero_power, hero_hp, hero_defense, hero_mp, hero_experience, hero_money;

	static String[] hero_skills = { "발차기", "배기", "썰기" };
	static int[] hero_skill_costs = { 0, 10, 30 };

	static String monster_name;
	static int monster_level, monster_power, monster_hp, monster_max_hp, monster_defense, monster_mp,
			monster_experience, monster_money;

	static final String[] potion_names = { "힘 증가 포션", "방어력 증가 포션", "경험치 포션", "HP 증가 포션", "MP 증가 포션" };
	static final int[] potion_prices = { 150, 150, 100, 25, 25 };

	// utility
	private static void sleep(int ms) {
		try {
			Thread.sleep(PRINT_DELAY);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	// IO
	private static void print(String content) {
		String[] pieces = content.split("");
		for (int i = 0; i < pieces.length; i++) {
			System.out.printf(pieces[i]);
			sleep(PRINT_DELAY);
		}
	}

	private static void println(String content) {
		print(content + "\n");
	}

	private static void print_splitter(int length) {
		for (int splitter = 0; splitter < length; splitter++)
			System.out.print("—");
		System.out.println();
	}

	private static void notification(String title, String content) {
		notification(title, content, false);
	}

	private static void notification(String title, String content, boolean boxed) {
		String[] contents = content.split("\n");

		int maximum_length = title.length();
		for (int idx = 0; idx < contents.length; idx++)
			maximum_length = Math.max(maximum_length, contents[idx].length());
		maximum_length += NOTIFICATION_PADDING + EXTRA_BORDER_PADDING;

		if (boxed) {
			print_splitter(maximum_length);
		}

		for (int space = 0; space < NOTIFICATION_PADDING; space++)
			print(" ");
		println(title + "\n");

		for (int idx = 0; idx < contents.length; idx++) {
			for (int space = 0; space < NOTIFICATION_PADDING; space++)
				print(" ");
			println(contents[idx]);
		}

		if (boxed) {
			print_splitter(maximum_length);
		}
	}

	private static int selection_prompt(String[] options, String prompt) {
		for (int idx = 0; idx < options.length; idx++) {
			println((idx + 1) + ". " + options[idx]);
		}

		int selection = -1;
		while (selection <= 0 || selection > options.length) {
			if (selection != -1)
				println("\n[" + selection + "번은 올바른 선택지가 아닙니다.]");

			print("\n" + prompt + ": ");
			selection = SCANNER.nextInt();
		}

		return selection - 1;
	}

	// 꾸미기
	private static String render_progress(int value, int maximum) {
		StringBuilder progress = new StringBuilder("[");

		for (int pg = 0; pg < PROGRESS_SIZE; pg++) {
			progress.append((double) pg / PROGRESS_SIZE >= (double) value / maximum ? " " : "=");
		}

		progress.append("]");

		return progress.toString();
	}

	private static String render_heroStatus(String status_name) {
		return switch (status_name) {
		case "레벨" -> "%d".formatted(hero_level);
		case "경험치" -> "%s (%d/%d)".formatted(render_progress(hero_experience - get_required_experience(hero_level - 1),
				get_required_experience(hero_level)), hero_experience, get_required_experience(hero_level));
		case "힘" -> "%d".formatted(hero_power);
		case "방어력" -> "%d".formatted(hero_defense);
		case "체력" ->
			"%s (%d/%d)".formatted(render_progress(Math.max(hero_hp, 0), Math.max(hero_hp, get_max_hp(hero_level))),
					Math.max(hero_hp, 0), Math.max(hero_hp, get_max_hp(hero_level)));
		case "마나" -> "%s (%d/%d)".formatted(render_progress(hero_mp, Math.max(hero_mp, get_max_mp(hero_level))),
				hero_mp, Math.max(hero_mp, get_max_mp(hero_level)));
		case "돈" -> "%d원".formatted(hero_money);
		default -> "";
		};
	}

	private static String render_monsterStatus(String status_name) {
		return switch (status_name) {
		case "레벨" -> "%d".formatted(monster_level);
		case "경험치" -> "%s (%d/%d)".formatted(
				render_progress(monster_experience - get_required_experience(monster_level - 1),
						get_required_experience(monster_level)),
				monster_experience, get_required_experience(monster_level));
		case "힘" -> "%d".formatted(monster_power);
		case "방어력" -> "%d".formatted(monster_defense);
		case "체력" ->
			"%s (%d/%d)".formatted(render_progress(Math.max(monster_hp, 0), Math.max(monster_hp, monster_max_hp)),
					Math.max(monster_hp, 0), Math.max(monster_hp, monster_max_hp));
		case "마나" ->
			"%s (%d/%d)".formatted(render_progress(monster_mp, Math.max(monster_mp, get_max_mp(monster_level))),
					monster_mp, Math.max(monster_mp, get_max_mp(monster_level)));
		case "돈" -> "%d원".formatted(monster_money);
		default -> "";
		};
	}

	private static void heroStatus_show(boolean box) {
		String status_text = "　레벨 " + render_heroStatus("레벨") + "\n경험치 " + render_heroStatus("경험치") + "\n　　힘 "
				+ render_heroStatus("힘") + "\n방어력 " + render_heroStatus("방어력") + "\n　체력 " + render_heroStatus("체력")
				+ "\n　마나 " + render_heroStatus("마나") + "\n　　돈 " + render_heroStatus("돈");

		notification("　　　 " + hero_name, status_text, box);
	}

	private static void potionStore_show(int money, int ignored) {
		List<String> available_portions = new ArrayList<>();
		List<Integer> effects = new ArrayList<>();

		for (int i = 0; i < potion_names.length; i++) {
			if (hero_money >= potion_prices[i]) {
				available_portions.add(potion_names[i] + " (" + potion_prices[i] + "원)");
				effects.add(i);
			}
		}
		available_portions.add("나가기");
		effects.add(-1);

		println("현재 돈: " + render_heroStatus("돈") + "\n");

		int selected = selection_prompt(available_portions.toArray(new String[0]), "");
		int index = effects.get(selected);

		if (index == -1) // not buying anything
			return;

		int price = potion_prices[index];

		hero_money -= price;
		println(potion_names[index] + "을(를) 구매했습니다!");
		println(price + "원 사용했습니다. 남은 돈: " + render_heroStatus("돈"));

		switch (index) {
		case 0 -> {
			hero_power += 3;
			println("힘이 3 증가했습니다!\n현재 힘: " + render_heroStatus("힘"));
		}
		case 1 -> {
			hero_defense += 3;
			println("방어력이 3 증가했습니다!\n현재 방어력: " + render_heroStatus("방어력"));
		}
		case 2 -> {
			hero_experience += 50;
			println("경험치가 50 증가했습니다!");
			check_level_up();
			println("현재 경험치: " + render_heroStatus("경험치"));
		}
		case 3 -> {
			hero_hp += 50;
			println("체력이 50 증가했습니다! (임시)\n현재 체력: " + render_heroStatus("체력"));
		}
		case 4 -> {
			hero_mp += 50;
			println("마나가 50 증가했습니다! (임시)\n현재 마나: " + render_heroStatus("마나"));
		}
		}

		print_splitter(SPLITTER_LENGTH);
	}

	// 규칙들
	private static int get_required_experience(int level) {
		return level >= 1 ? level * 80 : 0;
	}

	private static int get_reward_for_level_up(int level) {
		return level * 50;
	}

	private static int get_max_hp(int level) {
		return 100 + (level - 1) * 10;
	}

	private static int get_max_mp(int level) {
		return 100 + (level - 1) * 5;
	}

	private static double get_critical_rate(int level) {
		return Math.min(0.5, 0.03 * Math.sqrt(Math.max(0, level)));
	}

	private static int get_damage(int power, double multiplier, int level) {
		double critical_rate = get_critical_rate(level);

		Random rng = new Random();

		if (critical_rate > rng.nextDouble()) {
			return (int) (power * 5 * multiplier);
		} else {
			return (int) (power * multiplier);
		}
	}

	private static void check_level_up() {
		if (hero_experience >= get_required_experience(hero_level)) {
			hero_level++;
			println("레벨 업! " + (hero_level - 1) + " -> " + hero_level);
			hero_money += get_reward_for_level_up(hero_level);
			println("힘 " + hero_power + " -> " + (int) ((hero_power + 1) * 1.3));
			println("방어력 " + hero_defense + " -> " + (int) ((hero_defense + 1) * 1.3));
			println("레벨 업 기념으로 돈이 " + get_reward_for_level_up(hero_level) + "원 증가하여 " + render_heroStatus("돈")
					+ "이 되었습니다.");
			hero_power = (int) ((hero_power + 1) * 1.3);
			hero_defense = (int) ((hero_defense + 1) * 1.3);

			hero_hp = Math.max(hero_hp, get_max_hp(hero_level));
			hero_mp = Math.max(hero_mp, get_max_mp(hero_level));
		}
	}

	// 초기화
	private static void init_hero(String name) {
		hero_name = name;

		Random hero_rng = new Random((long) hero_name.chars().sum());

		// Static
		hero_level = 1;

		hero_hp = 100;
		hero_mp = 100;

		hero_experience = 0;
		hero_money = 0;

		// Random status
		hero_power = hero_rng.nextInt(1, 20);
		hero_defense = 20 - hero_power;
	}

	private static void init_hero() {
		println("환영합니다!\n");
		print("캐릭터의 이름을 정해주세요: ");

		init_hero(SCANNER.next());

		println("안녕하세요, " + hero_name + "님.\n");
		heroStatus_show(false);
		println("\n게임을 시작합니다!");
	}

	private static void init_monster(String name) {
		switch (name) {
		case "너구리", "W" -> {
			monster_name = "너구리";
			monster_level = 1;
			monster_power = 10;
			monster_hp = 30;
			monster_max_hp = 30;
			monster_defense = 5;
			monster_mp = 0;
			monster_experience = 15;
			monster_money = 10;
		}

		case "살퀭이", "X" -> {
			monster_name = "살퀭이";
			monster_level = 5;
			monster_power = 25;
			monster_hp = 120;
			monster_max_hp = 120;
			monster_defense = 15;
			monster_mp = 5;
			monster_experience = 60;
			monster_money = 40;
		}

		case "뱀", "Y" -> {
			monster_name = "뱀";
			monster_level = 10;
			monster_power = 65;
			monster_hp = 600;
			monster_max_hp = 600;
			monster_defense = 50;
			monster_mp = 15;
			monster_experience = 300;
			monster_money = 220;
		}

		case "곰", "Z" -> {
			monster_name = "곰";
			monster_level = 20;
			monster_power = 120;
			monster_hp = 1800;
			monster_max_hp = 1800;
			monster_defense = 85;
			monster_mp = 20;
			monster_experience = 1000;
			monster_money = 600;
		}

		default -> {
			monster_name = "ERROR";
			monster_level = 1;
			monster_power = 1;
			monster_hp = 1;
			monster_defense = 0;
			monster_mp = 0;
			monster_experience = 0;
			monster_money = 0;
		}
		}
	}

	// Battle Logic
	private static void hero_attack() {
		println(hero_name + "님의 공격입니다. 사용할 스킬을 선택해주세요.\n\n마나 " + render_heroStatus("마나"));

		List<String> available_skills = new ArrayList<>();

		for (int idx = 0; idx < hero_skills.length; idx++)
			if (hero_skill_costs[idx] <= hero_mp)
				available_skills.add(hero_skills[idx] + " [" + hero_skill_costs[idx] + " 마나 필요]");

		int skill_idx = selection_prompt(available_skills.toArray(new String[0]), "");
		int skil_cost = hero_skill_costs[skill_idx];
		String skill_name = hero_skills[skill_idx];
		double multiplier = switch (skill_name) {
		case "발차기" -> 1.0;
		case "배기" -> 1.5;
		case "썰기" -> 2.5;
		default -> 0.0;
		};

		hero_mp -= skil_cost;

		int damage = get_damage(hero_power, multiplier, hero_level);

		monster_attacked(damage);

		if (damage < monster_defense) {
			println("\n" + monster_name + "은 피해를 입지 않았습니다. (" + damage + " < " + monster_defense + ")");
			return;
		}

		if (damage > get_damage(hero_power, multiplier, 0)) {
			print("\n[크리티컬] ");
		}
		println(damage + "의 피해를 입혔습니다.");
	}

	private static void hero_attacked(int sum) {
		if (sum < hero_defense) {
			return;
		}

		hero_hp -= sum;
	}

	private static void hero_killed() {
		println(monster_name + "에게 죽었습니다.");
		init_hero(hero_name);
		println("모든 레벨과 경험치, 스텟, 포션 효과 등을 잃었습니다.");

		println("[죽은 자리에서 부활합니다.]");
		print_splitter(SPLITTER_LENGTH);
	}

	private static void monster_attack() {
		println(monster_name + "의 공격입니다.\n");

		int damage = get_damage(monster_power, 1.0, monster_level);

		hero_attacked(damage);

		if (damage < hero_defense) {
			println(hero_name + "님은 피해를 입지 않았습니다.");
			return;
		}

		if (damage > get_damage(hero_power, 1.0, 0)) {
			print("[크리티컬] ");
		}
		println(damage + "의 피해를 입었습니다.");
	}

	private static void monster_attacked(int sum) {
		if (sum < monster_defense) {
			return;
		}

		monster_hp -= sum;
	}

	private static void monster_killed() {
		println(monster_name + "이(가) 죽었습니다.");

		hero_experience += monster_experience;
		hero_money += monster_money;
		println("\n" + monster_name + "이(가) 가지고 있던 " + render_monsterStatus("돈") + "을 강탈했습니다.");

		check_level_up();

		println("\n현재 경험치 " + render_heroStatus("경험치"));
		print_splitter(SPLITTER_LENGTH);
	}

	private static void monster_fight_prompt(String monster_name) {
		init_monster(monster_name);

		while (true) {
			hero_attack();

			if (monster_hp <= 0) {
				monster_killed();
				break;
			}
			println("몬스터 체력 " + render_monsterStatus("체력"));

			print_splitter(SPLITTER_LENGTH);

			monster_attack();

			if (hero_hp <= 0) {
				hero_killed();
				break;
			}
			println(hero_name + "의 체력 " + render_heroStatus("체력"));
			print_splitter(SPLITTER_LENGTH);
		}
	}

	// movement
	private static String get_cell_name(char point) {
		return switch (point) {
		case 'W' -> "너구리";
		case 'X' -> "살퀭이";
		case 'Y' -> "뱀";
		case 'Z' -> "곰";
		case 'P' -> "포션 상점";
		case 'S' -> "사냥터 출구";
		case 'G' -> "사냥터 입구";
		default -> "알 수 없는 물체";
		};
	}

	private static String[] hydrate_map(String[] map, int[] spawn_point) {
		List<int[]> available_positions = new ArrayList<>();
		Random random = new Random();

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length(); x++) {
				int dx = x - spawn_point[0];
				int dy = y - spawn_point[1];
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance > SPAWN_PROTECTION_RANGE && map[y].charAt(x) == '-') {
					available_positions.add(new int[] { x, y });
				}
			}
		}

		String[] new_map = new String[map.length];
		for (int i = 0; i < map.length; i++) {
			new_map[i] = map[i];
		}

		for (int i = 0; i < MONSTER_COUNT; i++) {
			int pos_index = random.nextInt(available_positions.size());
			int[] pos = available_positions.get(pos_index);

			available_positions.remove(pos_index);

			double prob = random.nextDouble();
			char new_item;

			if (prob < 0.5) {
				new_item = 'W';
			} else if (prob < 0.8) {
				new_item = 'X';
			} else if (prob < 0.95) {
				new_item = 'Y';
			} else {
				new_item = 'Z';
			}

			char[] row = new_map[pos[1]].toCharArray();
			row[pos[0]] = new_item;
			new_map[pos[1]] = new String(row);
		}

		return new_map;
	}

	private static double visibility_strength(int[] position1, int[] position2, String[] map) {
		// Ray casting
		double[] vector = { position2[0] - position1[0], position2[1] - position1[1] };
		double length = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);

		if (length < 0.001) {
			return 1.0;
		}

		vector[0] = vector[0] / length;
		vector[1] = vector[1] / length;
		// 유닛 백터로 변환

		int steps = (int) Math.max(Math.abs(vector[0] * length) * 2, Math.abs(vector[1] * length) * 2) + 1;
		double visibility = 1.0;

		for (double progress = 1.0 / steps; progress < 1.0; progress += 1.0 / steps) {
			int x = (int) (position1[0] + vector[0] * length * progress);
			int y = (int) (position1[1] + vector[1] * length * progress);

			if (y < 0 || y >= map.length || x < 0 || x >= map[y].length()) {
				return 0.0;
			}

			char cell = map[y].charAt(x);
			if (cell == '#') {
				return 0.0;
			} else if (cell != '-') {
				visibility -= 0.1;
				if (visibility <= 0.0) {
					return 0.0;
				}
			}
		}

		double distance_penalty = length / (EYESIGHT * 2.0);
		return Math.max(0.0, visibility * (1.0 - Math.min(1.0, distance_penalty)));
	}

	private static String render_visible(int[] position, int direction, String[] map) {
		String rendered_visible = "";

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length(); x++) {
				char cell = map[y].charAt(x);
				if (cell == '#' || cell == '-')
					continue;

				double visibility = visibility_strength(position, new int[] { x, y }, map);
				String name = get_cell_name(cell);

				if (visibility > 0.0) {
					double degree_diff = 90.0 - Math.toDegrees(Math.atan2(position[1] - y, x - position[0]))
							- direction * 90.0;
					degree_diff = degree_diff < 0 ? degree_diff % 360.0 + 360 : degree_diff % 360;

					String rendered_angle = "";
					if (degree_diff >= 337.5 || degree_diff < 22.5)
						rendered_angle = "정면";
					else if (degree_diff >= 22.5 && degree_diff < 67.5)
						rendered_angle = "우측 앞";
					else if (degree_diff >= 67.5 && degree_diff < 112.5)
						rendered_angle = "우측";
					else if (degree_diff >= 112.5 && degree_diff < 157.5)
						rendered_angle = "우측 뒤";
					else if (degree_diff >= 157.5 && degree_diff < 202.5)
						rendered_angle = "후방";
					else if (degree_diff >= 202.5 && degree_diff < 247.5)
						rendered_angle = "좌측 뒤";
					else if (degree_diff >= 247.5 && degree_diff < 292.5)
						rendered_angle = "좌측";
					else if (degree_diff >= 292.5 && degree_diff < 337.5)
						rendered_angle = "좌측 앞";

					String rendered_distance = "";
					if (visibility < 0.3)
						rendered_distance = name + "처럼 보이는 실루엣이 저 멀리 희미하게 보입니다.";
					else if (visibility < 0.6)
						rendered_distance = name + "이(가) 희미하게 보입니다.";
					else
						rendered_distance = name + "이(가) 있습니다.";

					rendered_visible += "[" + rendered_angle + "에 " + rendered_distance + "]\n";
				}
			}
		}

		return rendered_visible;
	}

	private static void display_map(String[] map, int[] hero_position, int direction) {
		char hero_icon = switch (direction % 4) {
		case 0 -> '↑';
		case 1 -> '→';
		case 2 -> '↓';
		case 3 -> '←';
		default -> '@';
		};

		System.out.println("지도\n#:벽 -:길 P:상점 S:출구 G:입구 W:너구리 X:살퀭이 Y:뱀 Z:곰 ↑→↓←:플레이어");

		for (int y = 0; y < map.length; y++) {
			if (y == hero_position[1]) {
				String row = map[y];
				char[] chars = row.toCharArray();
				chars[hero_position[0]] = hero_icon;
				System.out.println(new String(chars));
			} else {
				System.out.println(map[y]);
			}
		}

		print_splitter(SPLITTER_LENGTH);
	}

	private static int get_direction_from_delta(int dx, int dy) {
		if (dx == 0 && dy == -1)
			return 0;
		if (dx == 1 && dy == 0)
			return 1;
		if (dx == 0 && dy == 1)
			return 2;
		if (dx == -1 && dy == 0)
			return 3;
		return -1;
	}

	private static String get_direction_name(int direction) {
		return switch (direction % 4) {
		case 0 -> "정면";
		case 1 -> "우측";
		case 2 -> "후방";
		case 3 -> "좌측";
		default -> "ERROR";
		};
	}

	private static void wander_map_prompt(int[] current_pos, int direction, String[] map) {
		String visible = render_visible(current_pos, direction, map);
		println(visible.isEmpty() ? "주변에 아무것도 보이지 않습니다." : visible);

		List<String> options = new ArrayList<>();
		List<String> actions = new ArrayList<>();
		List<int[]> targets = new ArrayList<>();

		for (int i = 0; i < 4; i++) {
			int relative_index = (i + 4 + direction) % 4;
			int dx = switch (relative_index) {
			case 0, 2 -> 0;
			case 1 -> 1;
			case 3 -> -1;
			default -> -100; // would never occur
			};
			int dy = switch (relative_index) {
			case 0 -> -1;
			case 1, 3 -> 0;
			case 2 -> 1;
			default -> -100; // would never occur
			};

			int x = current_pos[0] + dx;
			int y = current_pos[1] + dy;

			if (y >= 0 && y < map.length && x >= 0 && x < map[y].length() && map[y].charAt(x) == '-') {
				options.add(get_direction_name(i) + "으로 이동");
				actions.add("move");
				targets.add(new int[] { x, y });
			}
		}

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length(); x++) {
				if (x == current_pos[0] && y == current_pos[1])
					continue;

				char cell = map[y].charAt(x);
				if (cell != '#' && cell != '-') {
					double visibility = visibility_strength(current_pos, new int[] { x, y }, map);

					if (visibility > 0.6) {
						if (cell == 'W' || cell == 'X' || cell == 'Y' || cell == 'Z') {
							options.add(get_cell_name(cell) + "와(과) 전투하기");
							actions.add("fight");
							targets.add(new int[] { x, y });
						} else if (cell == 'P') {
							options.add("상점 이용하기");
							actions.add("shop");
							targets.add(new int[] { x, y });
						} else if (cell == 'S') {
							options.add("출구로 탈출하기");
							actions.add("exit");
							targets.add(new int[] { x, y });
						} else if (cell == 'G') {
							options.add("사냥터 들어가기");
							actions.add("enter");
							targets.add(new int[] { x, y });
						}
					}
				}
			}
		}

		options.add("상태 확인하기");
		actions.add("status");
		targets.add(new int[] {});

		options.add("지도 보기");
		actions.add("map");
		targets.add(new int[] {});

		int choice = selection_prompt(options.toArray(new String[0]), "행동을 선택하세요");
		String action = actions.get(choice);
		int[] target = targets.get(choice);

		switch (action) {
		case "move" -> {
			int dx = target[0] - current_pos[0];
			int dy = target[1] - current_pos[1];

			int new_direction = get_direction_from_delta(dx, dy);
			println("한 걸음 이동합니다.");
			print_splitter(SPLITTER_LENGTH);
			wander_map_prompt(target, new_direction, map);
		}

		case "fight" -> {
			String monster_name = get_cell_name(map[target[1]].charAt(target[0]));
			println(monster_name + "와(과) 전투를 시작합니다!");

			monster_fight_prompt(monster_name);

			// both case, monster is gone.
			char[] row = map[target[1]].toCharArray();
			row[target[0]] = '-';
			map[target[1]] = new String(row);
			wander_map_prompt(current_pos, direction, map);
		}

		case "shop" -> {
			println("상점에 입장합니다.");
			print_splitter(SPLITTER_LENGTH);
			potionStore_show(hero_money, 4);
			wander_map_prompt(current_pos, direction, map);
		}

		case "exit" -> {
			println("마을로 돌아갑니다.");
			print_splitter(SPLITTER_LENGTH);
			wander_map_prompt(TOWN_MAP_STARTING_POINT, 3, TOWN_MAP);
		}

		case "enter" -> {
			println("사냥터에 입장합니다.");
			print_splitter(SPLITTER_LENGTH);
			wander_map_prompt(HUNTING_MAP_STARTING_POINT, 0, hydrate_map(HUNTING_MAP, HUNTING_MAP_STARTING_POINT));
		}

		case "status" -> {
			heroStatus_show(true);
			wander_map_prompt(current_pos, direction, map);
		}

		case "map" -> {
			display_map(map, current_pos, direction);
			wander_map_prompt(current_pos, direction, map);
		}

		default -> {
			println("[축하합니다. 버그를 발견하셨습니다!]");
		}
		}
		;
	}

	public static void main(String[] args) {
		init_hero();
		hero_money = 10000;
		wander_map_prompt(TOWN_MAP_STARTING_POINT, 3, TOWN_MAP);
	}
}
