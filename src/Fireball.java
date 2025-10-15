import bagel.Image;
import bagel.util.Point;

/**
 * Projectile fired by enemies toward the player
 */
public class Fireball extends GameEntity {
    private final double velocityX;
    private final double velocityY;
    private final double damage;

    public Fireball(Point startPos, Point targetPos) {
        super(startPos, new Image("res/fireball.png"));
        this.damage = GameConstants.FIREBALL_DAMAGE;

        // Calculate velocity towards target (player position)
        double speed = GameConstants.FIREBALL_SPEED;
        double dx = targetPos.x - startPos.x;
        double dy = targetPos.y - startPos.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Normalize and multiply by speed
        this.velocityX = (dx / distance) * speed;
        this.velocityY = (dy / distance) * speed;
    }

    @Override
    public void update() {
        if (!active) {
            return;
        }

        // Move the fireball
        position = new Point(position.x + velocityX, position.y + velocityY);

        // Deactivate if out of bounds
        if (position.x < 0 || position.x > ShadowDungeon.screenWidth ||
            position.y < 0 || position.y > ShadowDungeon.screenHeight) {
            active = false;
        }
    }

    public double getDamage() {
        return damage;
    }
}
