import bagel.Image;
import bagel.util.Point;

/**
 * Stationary enemy that shoots fireballs at the player periodically
 */
public class BulletKin extends Enemy {

    public BulletKin(Point position) {
        super(position,
              new Image("res/bullet_kin.png"),
              EnemyType.BULLET_KIN,
              GameConstants.BULLET_KIN_HEALTH,
              GameConstants.BULLET_KIN_SHOOT_FREQUENCY,
              GameConstants.BULLET_KIN_COIN);
    }

    @Override
    public Fireball shoot(Player player) {
        if (!active || defeated || player == null) {
            return null;
        }

        // Check if enough frames have passed since last shot
        if (framesSinceLastShot >= shootingInterval) {
            framesSinceLastShot = 0;
            return new Fireball(position, player.getPosition());
        }

        return null;
    }
}
