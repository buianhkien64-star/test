import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Abstract base class for all game entities (Player, Enemy, Bullet, etc.)
 */
public abstract class GameEntity {
    protected Point position;
    protected Image image;
    protected boolean active = true;

    public GameEntity(Point position, Image image) {
        this.position = position;
        this.image = image;
    }

    /**
     * Update the entity's state
     */
    public abstract void update();

    /**
     * Render the entity on screen
     */
    public void render() {
        if (active && image != null) {
            image.draw(position.x, position.y);
        }
    }

    /**
     * Check if this entity intersects with another entity
     */
    public boolean intersects(GameEntity other) {
        if (!this.active || !other.active) {
            return false;
        }
        return this.getBoundingBox().intersects(other.getBoundingBox());
    }

    /**
     * Check if this entity intersects with a rectangle
     */
    public boolean intersects(Rectangle rect) {
        if (!this.active) {
            return false;
        }
        return this.getBoundingBox().intersects(rect);
    }

    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Image getImage() {
        return image;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
