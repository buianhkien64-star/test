import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Projectile fired by enemies toward the player
 */
public class Fireball {
    private Point position;
    private final Image image;
    private final double velocityX;
    private final double velocityY;
    private final double damage;
    private boolean active = true;
    private final double speed;

    public Fireball(Point startPos, Point targetPos) {
        this.position = startPos;
        this.image = new Image("res/fireball.png");
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballSpeed"));
        this.damage = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballDamage"));

        // Calculate velocity towards target (player position)
        double dx = targetPos.x - startPos.x;
        double dy = targetPos.y - startPos.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Normalize and multiply by speed
        this.velocityX = (dx / distance) * speed;
        this.velocityY = (dy / distance) * speed;
    }

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

    public void draw() {
        if (active) {
            image.draw(position.x, position.y);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    public double getDamage() {
        return damage;
    }

    public Point getPosition() {
        return position;
    }
}
