import bagel.Image;
import bagel.util.Point;

/**
 * Enemy that moves along a predefined path and gets removed when the player overlaps with it
 */
public class KeyBulletKin {
    private Point position;
    private final Image image;
    private boolean active = false; // only true when the Battle Room has been activated
    private boolean dead = false;

    public KeyBulletKin(String pathString) {
        // Parse the first coordinate as the starting position
        String[] pathPoints = pathString.split(";");
        if (pathPoints.length > 0) {
            this.position = IOUtils.parseCoords(pathPoints[0]);
        }
        this.image = new Image("res/key_bullet_kin.png");
    }

    public void update(Player player) {
        if (hasCollidedWith(player)) {
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
}
