package Primitive;

public enum Weapon {
    NONE("맨손", 0, 0, "ALL"),
    SWORD("검", 5, 100, "전사"),
    GREAT_SWORD("대검", 8, 200, "전사"),
    SHIELD("방패", 2, 80, "전사"),
    BOW("활", 4, 90, "궁수"),
    CROSSBOW("석궁", 7, 180, "궁수"),
    LONGBOW("장궁", 10, 300, "궁수"),
    STAFF("지팡이", 3, 70, "마법사"),
    MAGIC_WAND("마법 지팡이", 6, 150, "마법사"),
    CRYSTAL_STAFF("수정 지팡이", 9, 250, "마법사");
    
    private final String koreanName;
    private final int damage;
    private final int price;
    private final String allowedClass;
    
    Weapon(String koreanName, int damage, int price, String allowedClass) {
        this.koreanName = koreanName;
        this.damage = damage;
        this.price = price;
        this.allowedClass = allowedClass;
    }
    
    public String getName() {
        return koreanName;
    }
    
    public String getKoreanName() {
        return koreanName;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getPrice() {
        return price;
    }
    
    public String getAllowedClass() {
        return allowedClass;
    }
    
    public boolean canBeUsedBy(String heroClass) {
        return "ALL".equals(allowedClass) || allowedClass.equals(heroClass);
    }
    
    public double getMultiplier(String monsterName) {
        return switch (this) {
            case SWORD, GREAT_SWORD -> switch (monsterName) {
                case "살퀭이" -> 1.3;
                case "뱀" -> 1.2;
                default -> 1.0;
            };
            case BOW, CROSSBOW, LONGBOW -> switch (monsterName) {
                case "살퀭이" -> 1.5;
                case "곰" -> 0.8;
                default -> 1.0;
            };
            case STAFF, MAGIC_WAND, CRYSTAL_STAFF -> switch (monsterName) {
                case "곰" -> 1.4;
                case "너구리" -> 1.2;
                default -> 1.0;
            };
            default -> 1.0;
        };
    }
}