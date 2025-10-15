import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

/**
 * Moving enemy that follows a predefined path and drops a key when defeated
 */
public class KeyBulletKin extends Enemy {
    private ArrayList<Point> movementPath;
    private int currentPathIndex = 0;
    private final double speed;
    private boolean keyDropped = false;

    public KeyBulletKin(String pathCoordinates) {
        super(new Point(0, 0),
              new Image("res/key_bullet_kin.png"),
              EnemyType.KEY_BULLET_KIN,
              GameConstants.KEY_BULLET_KIN_HEALTH,
              0,  // KeyBulletKin doesn't shoot
              0); // KeyBulletKin doesn't give coins directly

        this.speed = GameConstants.KEY_BULLET_KIN_SPEED;
        this.movementPath = new ArrayList<>();

        // Parse path coordinates
        String[] waypoints = pathCoordinates.split(";");
        for (String waypoint : waypoints) {
            movementPath.add(IOUtils.parseCoords(waypoint));
        }

        if (!movementPath.isEmpty()) {
            this.position = movementPath.get(0);
            currentPathIndex = 1; // Start moving towards second waypoint
        }
    }

    @Override
    public void update() {
        if (!active || defeated || movementPath.isEmpty()) {
            return;
        }

        // Get current target waypoint
        Point target = movementPath.get(currentPathIndex);

        // Calculate direction to target
        double dx = target.x - position.x;
        double dy = target.y - position.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Move towards target if not reached
        if (distance > speed) {
            double moveX = (dx / distance) * speed;
            double moveY = (dy / distance) * speed;
            position = new Point(position.x + moveX, position.y + moveY);
        } else {
            // Reached waypoint, move to next one (loop back to start)
            currentPathIndex = (currentPathIndex + 1) % movementPath.size();
        }
    }

    @Override
    public Fireball shoot(Player player) {
        // KeyBulletKin doesn't shoot
        return null;
    }

    @Override
    protected void onDeath() {
        // Key will be dropped by BattleRoom
    }

    public boolean hasDroppedKey() {
        return keyDropped;
    }

    public void setKeyDropped() {
        this.keyDropped = true;
    }
}
