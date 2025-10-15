/**
 * Game constants loaded from properties file
 */
public class GameConstants {
    public static double RIVER_DAMAGE_PER_FRAME;
    public static double PLAYER_MOVING_SPEED;
    public static double INITIAL_HEALTH;
    public static int INITIAL_COINS;
    public static double BULLET_SPEED;
    public static int BULLET_FREQ;
    public static double FIREBALL_SPEED;
    public static double FIREBALL_DAMAGE;
    public static int BULLET_KIN_HEALTH;
    public static int BULLET_KIN_COIN;
    public static int BULLET_KIN_SHOOT_FREQUENCY;
    public static int ASHEN_BULLET_KIN_HEALTH;
    public static int ASHEN_BULLET_KIN_COIN;
    public static int ASHEN_BULLET_KIN_SHOOT_FREQUENCY;
    public static int KEY_BULLET_KIN_HEALTH;
    public static double KEY_BULLET_KIN_SPEED;
    public static int HEALTH_PURCHASE;
    public static int HEALTH_BONUS;
    public static int WEAPON_PURCHASE;
    public static int BASKET_COIN;

    /**
     * Initialize constants from properties file
     */
    public static void init() {
        RIVER_DAMAGE_PER_FRAME = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame"));
        PLAYER_MOVING_SPEED = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("movingSpeed"));
        INITIAL_HEALTH = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("initialHealth"));
        INITIAL_COINS = 0;
        BULLET_SPEED = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletSpeed"));
        BULLET_FREQ = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletFreq"));
        FIREBALL_SPEED = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballSpeed"));
        FIREBALL_DAMAGE = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballDamage"));
        BULLET_KIN_HEALTH = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletKinHealth"));
        BULLET_KIN_COIN = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletKinCoin"));
        BULLET_KIN_SHOOT_FREQUENCY = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletKinShootFrequency"));
        ASHEN_BULLET_KIN_HEALTH = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("ashenBulletKinHealth"));
        ASHEN_BULLET_KIN_COIN = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("ashenBulletKinCoin"));
        ASHEN_BULLET_KIN_SHOOT_FREQUENCY = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("ashenBulletKinShootFrequency"));
        KEY_BULLET_KIN_HEALTH = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("keyBulletKinHealth"));
        KEY_BULLET_KIN_SPEED = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinSpeed"));
        HEALTH_PURCHASE = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("healthPurchase"));
        HEALTH_BONUS = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("healthBonus"));
        WEAPON_PURCHASE = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("weaponPurchase"));
        BASKET_COIN = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("basketCoin"));
    }
}
