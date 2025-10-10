import bagel.Image;
import bagel.util.Point;

/**
 * Character selection sprite displayed in the Prep Room
 */
public class CharacterSprite {
    private final Point position;
    private final Image image;

    public CharacterSprite(Point position, String imagePath) {
        this.position = position;
        this.image = new Image(imagePath);
    }

    public void draw() {
        image.draw(position.x, position.y);
    }

    public Point getPosition() {
        return position;
    }
}
