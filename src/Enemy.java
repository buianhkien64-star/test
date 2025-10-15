import bagel.Image;
import bagel.util.Point;

/**
 * Abstract base class for all enemies
 */
public abstract class Enemy extends GameEntity {
    protected double health;
    protected EnemyType enemyType;
    protected int framesSinceLastShot = 0;
    protected int shootingInterval;
    protected int coinReward;
    protected boolean defeated = false;

    public Enemy(Point position, Image image, EnemyType enemyType, double health, int shootingInterval, int coinReward) {
        super(position, image);
        this.enemyType = enemyType;
        this.health = health;
        this.shootingInterval = shootingInterval;
        this.coinReward = coinReward;
    }

    @Override
    public void update() {
        if (!active || defeated) {
            return;
        }
        framesSinceLastShot++;
    }

    /**
     * Take damage and check if defeated
     */
    public void takeDamage(double damage) {
        health -= damage;
        if (health <= 0 && !defeated) {
            defeated = true;
            onDeath();
        }
    }

    /**
     * Called when enemy is defeated
     */
    protected void onDeath() {
        // Override in subclasses if needed
    }

    /**
     * Try to shoot a projectile
     */
    public abstract Fireball shoot(Player player);

    public boolean isDefeated() {
        return defeated;
    }

    public int getCoinReward() {
        return coinReward;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public double getHealth() {
        return health;
    }
}
