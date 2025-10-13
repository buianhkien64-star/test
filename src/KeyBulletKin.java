import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;

/**
 * Enemy that moves along a predefined path and gets removed when the player overlaps with it
 */
public class KeyBulletKin {
    private Point position;
    private final Image image;
    private boolean active = false; // only true when the Battle Room has been activated
    private boolean dead = false;
    private int health;
    private final ArrayList<Point> movementPath;
    private int currentTargetIndex = 1; // Start moving towards the second point
    private final double speed;
    private boolean keyDropped = false;

    public KeyBulletKin(String pathString) {
        this.image = new Image("res/key_bullet_kin.png");
        this.movementPath = new ArrayList<>();
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinSpeed"));
        this.health = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("keyBulletKinHealth"));

        // Parse all points in the path
        String[] pathPoints = pathString.split(";");
        for (String coords : pathPoints) {
            movementPath.add(IOUtils.parseCoords(coords));
        }

        // Start at the first point
        if (!movementPath.isEmpty()) {
            this.position = new Point(movementPath.get(0).x, movementPath.get(0).y);
        }
    }

    public void update(Player player) {
        if (!active || dead) {
            return;
        }

        // Move along the path
        if (!movementPath.isEmpty() && currentTargetIndex < movementPath.size()) {
            Point target = movementPath.get(currentTargetIndex);
            double dx = target.x - position.x;
            double dy = target.y - position.y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Check if we've reached the current target
            if (distance <= speed) {
                // Move to the exact target position
                position = new Point(target.x, target.y);

                // Move to next target (loop back to start if at end)
                currentTargetIndex++;
                if (currentTargetIndex >= movementPath.size()) {
                    currentTargetIndex = 0;
                }
            } else {
                // Move towards the target
                double moveX = (dx / distance) * speed;
                double moveY = (dy / distance) * speed;
                position = new Point(position.x + moveX, position.y + moveY);
            }
        }

        // Check collision with player
        if (hasCollidedWith(player)) {
            player.receiveDamage(Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame")));
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            dead = true;
            active = false;
        }
    }

    public void draw() {
        image.draw(position.x, position.y);
    }

    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Point getPosition() {
        return position;
    }

    public Image getImage() {
        return image;
    }

    public int getHealth() {
        return health;
    }

    public boolean hasDroppedKey() {
        return keyDropped;
    }

    public void setKeyDropped() {
        keyDropped = true;
    }
}
