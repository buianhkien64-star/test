import bagel.Image;
import bagel.util.Point;

/**
 * Stationary enemy that shoots fireballs at the player periodically
 */
public class BulletKin {
    private final Point position;
    private final Image image;
    private double health;
    private boolean active = false;
    private boolean dead = false;
    private final int shootFrequency;
    private int framesSinceLastShot = 0;
    private final int coinValue;

    public BulletKin(Point position) {
        this.position = position;
        this.image = new Image("res/bullet_kin.png");
        this.health = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinHealth"));
        this.shootFrequency = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletKinShootFrequency"));
        this.coinValue = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletKinCoin"));
    }

    public void update(Player player) {
        if (!active || dead) {
            return;
        }

        // Increment shooting cooldown counter
        framesSinceLastShot++;
    }

    public void draw() {
        if (active && !dead) {
            image.draw(position.x, position.y);
        }
    }

    /**
     * Try to shoot a fireball towards the player
     * @param player The player to shoot at
     * @return Fireball object if shooting is successful, null if still on cooldown
     */
    public Fireball shoot(Player player) {
        if (!active || dead || player == null) {
            return null;
        }

        // Check if enough frames have passed since last shot
        if (framesSinceLastShot >= shootFrequency) {
            framesSinceLastShot = 0;
            return new Fireball(position, player.getPosition());
        }

        return null;
    }

    public void takeDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            dead = true;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDead() {
        return dead;
    }

    public Point getPosition() {
        return position;
    }

    public Image getImage() {
        return image;
    }

    public int getCoinValue() {
        return coinValue;
    }
}
