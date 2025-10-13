import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Destructible obstacle that blocks player and fireballs, but can be destroyed by player bullets
 */
public class Table {
    private final Point position;
    private final Image image;
    private boolean destroyed = false;

    public Table(Point position) {
        this.position = position;
        this.image = new Image("res/table.png");
    }

    public void update(Player player) {
        if (destroyed) {
            return;
        }

        // Block player movement
        if (hasCollidedWith(player)) {
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }

    public void draw() {
        if (!destroyed) {
            image.draw(position.x, position.y);
        }
    }

    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }
}
