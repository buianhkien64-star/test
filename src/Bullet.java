import bagel.Image;
import bagel.Window;
import bagel.util.Point;

/**
 * Bullet fired by the player towards enemies
 */
public class Bullet extends GameEntity {
    private final double velocityX;
    private final double velocityY;
    private final int damage;

    private static final Image BULLET_IMAGE = new Image("res/bullet.png");

    public Bullet(Point startPos, Point targetPos, int damage) {
        super(startPos, BULLET_IMAGE);
        this.damage = damage;

        // Calculate velocity towards target
        double speed = GameConstants.BULLET_SPEED;
        double dx = targetPos.x - startPos.x;
        double dy = targetPos.y - startPos.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Normalize and apply speed
        this.velocityX = (dx / distance) * speed;
        this.velocityY = (dy / distance) * speed;
    }

    @Override
    public void update() {
        if (!active) {
            return;
        }

        // Move bullet
        position = new Point(position.x + velocityX, position.y + velocityY);

        // Check if bullet is out of bounds
        if (position.x < 0 || position.x > Window.getWidth() ||
            position.y < 0 || position.y > Window.getHeight()) {
            active = false;
        }
    }

    public int getDamage() {
        return damage;
    }
}
