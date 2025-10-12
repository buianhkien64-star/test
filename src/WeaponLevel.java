/**
 * Enum representing the different weapon upgrade levels
 */
public enum WeaponLevel {
    STANDARD(0, 30),   // Level 0, 30 damage
    ADVANCED(1, 50),   // Level 1, 50 damage
    ELITE(2, 100);     // Level 2, 100 damage

    private final int level;
    private final int damage;

    WeaponLevel(int level, int damage) {
        this.level = level;
        this.damage = damage;
    }

    public int getLevel() {
        return level;
    }

    public int getDamage() {
        return damage;
    }

    public WeaponLevel getNextLevel() {
        switch (this) {
            case STANDARD:
                return ADVANCED;
            case ADVANCED:
                return ELITE;
            default:
                return ELITE; // Already at max level
        }
    }

    public boolean canUpgrade() {
        return this != ELITE;
    }
}
