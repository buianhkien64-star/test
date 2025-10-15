import bagel.Input;
import bagel.MouseButtons;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Room with doors that are locked until the player defeats all enemies
 */
public class BattleRoom extends GameRoom {
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<Fireball> fireballs;
    private ArrayList<TreasureBox> treasureBoxes;
    private ArrayList<Table> tables;
    private ArrayList<Basket> baskets;
    private ArrayList<Key> keys;
    private boolean isComplete = false;
    private final String nextRoomName;

    public BattleRoom(String roomName, String nextRoomName) {
        super(roomName);
        this.nextRoomName = nextRoomName;
        this.enemies = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.fireballs = new ArrayList<>();
        this.treasureBoxes = new ArrayList<>();
        this.tables = new ArrayList<>();
        this.baskets = new ArrayList<>();
        this.keys = new ArrayList<>();
    }

    @Override
    public void initEntities(Properties gameProperties) {
        // Parse configuration for this room
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", roomId);

            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString()
                        .substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();

                // Ignore if value is 0
                if (propertyValue.equals("0")) {
                    continue;
                }

                String[] coordinates;
                switch (objectType) {
                    case "primarydoor":
                        coordinates = propertyValue.split(",");
                        doors.add(new Door(IOUtils.parseCoords(propertyValue), coordinates[2], this));
                        break;
                    case "secondarydoor":
                        coordinates = propertyValue.split(",");
                        doors.add(new Door(IOUtils.parseCoords(propertyValue), coordinates[2], this));
                        break;
                    case "keyBulletKin":
                        enemies.add(new KeyBulletKin(propertyValue));
                        break;
                    case "bulletKin":
                        for (String coords: propertyValue.split(";")) {
                            enemies.add(new BulletKin(IOUtils.parseCoords(coords)));
                        }
                        break;
                    case "ashenBulletKin":
                        for (String coords: propertyValue.split(";")) {
                            enemies.add(new AshenBulletKin(IOUtils.parseCoords(coords)));
                        }
                        break;
                    case "wall":
                        for (String coords: propertyValue.split(";")) {
                            walls.add(new Wall(IOUtils.parseCoords(coords)));
                        }
                        break;
                    case "treasurebox":
                        for (String coords: propertyValue.split(";")) {
                            TreasureBox treasureBox = new TreasureBox(IOUtils.parseCoords(coords),
                                    Double.parseDouble(coords.split(",")[2]));
                            treasureBoxes.add(treasureBox);
                        }
                        break;
                    case "river":
                        for (String coords: propertyValue.split(";")) {
                            rivers.add(new River(IOUtils.parseCoords(coords)));
                        }
                        break;
                    case "table":
                        for (String coords: propertyValue.split(";")) {
                            tables.add(new Table(IOUtils.parseCoords(coords)));
                        }
                        break;
                    case "basket":
                        for (String coords: propertyValue.split(";")) {
                            baskets.add(new Basket(IOUtils.parseCoords(coords)));
                        }
                        break;
                    default:
                }
            }
        }
    }

    @Override
    public void update(Input input) {
        // Update and draw doors
        for (Door door : doors) {
            door.update(player);
            door.draw();
            if (stopUpdatingEarlyIfNeeded()) {
                return;
            }
        }

        // Update and draw all enemies (polymorphic)
        for (Enemy enemy : enemies) {
            if (enemy.isActive() && !enemy.isDefeated()) {
                enemy.update();
                enemy.render();

                // Try to shoot fireballs (only for enemies that can shoot)
                Fireball newFireball = enemy.shoot(player);
                if (newFireball != null) {
                    fireballs.add(newFireball);
                }
            } else if (enemy.isDefeated() && enemy instanceof KeyBulletKin) {
                // Drop key when KeyBulletKin is defeated
                KeyBulletKin keyEnemy = (KeyBulletKin) enemy;
                if (!keyEnemy.hasDroppedKey()) {
                    keys.add(new Key(enemy.getPosition()));
                    keyEnemy.setKeyDropped();
                }
            }
        }

        // Update and draw keys
        for (Key key : keys) {
            key.update(player);
            key.draw();
        }

        // Update and draw walls
        for (Wall wall: walls) {
            wall.update(player);
            wall.draw();
        }

        // Update and draw rivers
        for (River river: rivers) {
            river.update(player);
            river.draw();
        }

        // Update and draw tables
        for (Table table : tables) {
            table.update(player);
            table.draw();
        }

        // Update and draw baskets
        for (Basket basket : baskets) {
            basket.update(player);
            basket.draw();
        }

        // Update and draw treasure boxes
        for (TreasureBox treasureBox: treasureBoxes) {
            if (treasureBox.isActive()) {
                treasureBox.update(input, player);
                treasureBox.draw();
            }
        }

        if (player != null) {
            // Handle shooting
            if (input.isDown(MouseButtons.LEFT)) {
                Bullet newBullet = player.shoot(input.getMouseX(), input.getMouseY());
                if (newBullet != null) {
                    bullets.add(newBullet);
                }
            }

            player.update(input);
            player.draw();
        }

        // Update bullets and check collisions
        updateBullets();

        // Update fireballs and check collisions
        updateFireballs();

        // Check if all enemies defeated
        if (areAllEnemiesDefeated() && !isComplete()) {
            setComplete(true);
            unlockAllDoors();
        }

        // Update and draw store
        Store store = ShadowDungeon.getStore();
        if (store != null) {
            store.update(input, player);
            store.draw();
        }
    }

    private void updateBullets() {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            bullet.update();
            bullet.render();

            // Check collision with walls
            for (Wall wall : walls) {
                if (bullet.isActive() && bullet.intersects(wall.getBoundingBox())) {
                    bullet.setActive(false);
                    break;
                }
            }

            // Check collision with all enemies (polymorphic)
            for (Enemy enemy : enemies) {
                if (bullet.isActive() && enemy.isActive() && !enemy.isDefeated() && bullet.intersects(enemy)) {
                    boolean wasAlive = !enemy.isDefeated();
                    enemy.takeDamage(bullet.getDamage());

                    // Award coins if enemy was just killed
                    if (wasAlive && enemy.isDefeated() && player != null) {
                        int coins = enemy.getCoinReward();
                        // Robot gets bonus coins
                        if (player.getPlayerType() == PlayerType.ROBOT) {
                            coins += 5;
                        }
                        player.earnCoins(coins);
                    }

                    bullet.setActive(false);
                    break;
                }
            }

            // Check collision with tables
            for (Table table : tables) {
                if (bullet.isActive() && !table.isDestroyed() && bullet.intersects(table.getBoundingBox())) {
                    table.destroy();
                    bullet.setActive(false);
                    break;
                }
            }

            // Check collision with baskets
            for (Basket basket : baskets) {
                if (bullet.isActive() && !basket.isDestroyed() && bullet.intersects(basket.getBoundingBox())) {
                    basket.destroy(player);
                    bullet.setActive(false);
                    break;
                }
            }

            if (!bullet.isActive()) {
                bulletsToRemove.add(bullet);
            }
        }

        bullets.removeAll(bulletsToRemove);
    }

    private void updateFireballs() {
        ArrayList<Fireball> fireballsToRemove = new ArrayList<>();

        for (Fireball fireball : fireballs) {
            fireball.update();
            fireball.render();

            // Check collision with walls
            for (Wall wall : walls) {
                if (fireball.isActive() && fireball.intersects(wall.getBoundingBox())) {
                    fireball.setActive(false);
                    break;
                }
            }

            // Check collision with tables
            for (Table table : tables) {
                if (fireball.isActive() && !table.isDestroyed() && fireball.intersects(table.getBoundingBox())) {
                    table.destroy();
                    fireball.setActive(false);
                    break;
                }
            }

            // Check collision with player
            if (fireball.isActive() && player != null && fireball.getBoundingBox().intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()))) {
                player.receiveDamage(fireball.getDamage());
                fireball.setActive(false);
            }

            if (!fireball.isActive()) {
                fireballsToRemove.add(fireball);
            }
        }

        fireballs.removeAll(fireballsToRemove);
    }

    private void unlockAllDoors() {
        for (Door door : doors) {
            door.unlock(false);
        }
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void activateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.setActive(true);
        }
    }

    public boolean areAllEnemiesDefeated() {
        for (Enemy enemy : enemies) {
            if (!enemy.isDefeated()) {
                return false;
            }
        }
        return true;
    }
}
