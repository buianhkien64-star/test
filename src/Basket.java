import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Destructible obstacle containing coins, destroyed by player bullets
 */
public class Basket {
    private final Point position;
    private final Image image;
    private boolean destroyed = false;
    private final int coinValue;

    public Basket(Point position) {
        this.position = position;
        this.image = new Image("res/basket.png");
        this.coinValue = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("basketCoin"));
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

    public void destroy(Player player) {
        destroyed = true;
        player.earnCoins(coinValue);
    }
}
