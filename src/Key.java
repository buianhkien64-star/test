import bagel.Image;
import bagel.util.Point;

/**
 * Collectible key dropped by KeyBulletKin when defeated
 */
public class Key {
    private final Point position;
    private final Image image;
    private boolean collected = false;

    private static final Image KEY_IMAGE = new Image("res/key.png");

    public Key(Point position) {
        this.position = position;
        this.image = KEY_IMAGE;
    }

    public void update(Player player) {
        if (!collected && hasCollidedWith(player)) {
            player.addKey();
            collected = true;
        }
    }

    public void draw() {
        if (!collected) {
            image.draw(position.x, position.y);
        }
    }

    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    public boolean isCollected() {
        return collected;
    }
}
