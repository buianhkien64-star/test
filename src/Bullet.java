import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Bullet fired by the player towards enemies
 */
public class Bullet {
    private Point position;
    private final Image image;
    private final double velocityX;
    private final double velocityY;
    private final int damage;
    private boolean active = true;

    private static final Image BULLET_IMAGE = new Image("res/bullet.png");

    public Bullet(Point startPos, Point targetPos, int damage) {
        this.position = startPos;
        this.image = BULLET_IMAGE;
        this.damage = damage;

        // Calculate velocity towards target
        double speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletSpeed"));
        double dx = targetPos.x - startPos.x;
        double dy = targetPos.y - startPos.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Normalize and apply speed
        this.velocityX = (dx / distance) * speed;
        this.velocityY = (dy / distance) * speed;
    }

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

    public void draw() {
        if (active) {
            image.draw(position.x, position.y);
        }
    }

    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDamage() {
        return damage;
    }

    public Point getPosition() {
        return position;
    }
}
