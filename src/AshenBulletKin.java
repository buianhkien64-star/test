import bagel.Image;
import bagel.util.Point;

/**
 * Stronger variant of BulletKin with more health and faster shooting
 */
public class AshenBulletKin extends Enemy {

    public AshenBulletKin(Point position) {
        super(position,
              new Image("res/ashen_bullet_kin.png"),
              EnemyType.ASHEN_BULLET_KIN,
              GameConstants.ASHEN_BULLET_KIN_HEALTH,
              GameConstants.ASHEN_BULLET_KIN_SHOOT_FREQUENCY,
              GameConstants.ASHEN_BULLET_KIN_COIN);
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
