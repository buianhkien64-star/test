import bagel.Input;
import bagel.MouseButtons;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Room with doors that are locked until the plaer defeats all enemies
 */
public class BattleRoom {
    private Player player;
    private Door primaryDoor;
    private Door secondaryDoor;
    private KeyBulletKin keyBulletKin;
    private ArrayList<TreasureBox> treasureBoxes;
    private ArrayList<Wall> walls;
    private ArrayList<River> rivers;
    private ArrayList<Table> tables;
    private ArrayList<Basket> baskets;
    private boolean stopCurrentUpdateCall = false; // this determines whether to prematurely stop the update execution
    private ArrayList<Bullet> bullets;
    private ArrayList<Key> keys;
    private boolean isComplete = false;
    private final String nextRoomName;
    private final String roomName;

    public BattleRoom(String roomName, String nextRoomName) {
        walls = new ArrayList<>();
        rivers = new ArrayList<>();
        treasureBoxes = new ArrayList<>();
        bullets = new ArrayList<>();
        keys = new ArrayList<>();
        tables = new ArrayList<>();
        baskets = new ArrayList<>();
        this.roomName = roomName;
        this.nextRoomName = nextRoomName;
    }

    public void initEntities(Properties gameProperties) {
        // find the configuration of game objects for this room
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", roomName);

            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString()
                        .substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();

                // ignore if the value is 0
                if (propertyValue.equals("0")) {
                    continue;
                }

                String[] coordinates;
                switch (objectType) {
                    case "primarydoor":
                        coordinates = propertyValue.split(",");
                        primaryDoor = new Door(IOUtils.parseCoords(propertyValue), coordinates[2], this);
                        break;
                    case "secondarydoor":
                        coordinates = propertyValue.split(",");
                        secondaryDoor = new Door(IOUtils.parseCoords(propertyValue), coordinates[2], this);
                        break;
                    case "keyBulletKin":
                        keyBulletKin = new KeyBulletKin(propertyValue);
                        break;
                    case "wall":
                        for (String coords: propertyValue.split(";")) {
                            Wall wall = new Wall(IOUtils.parseCoords(coords));
                            walls.add(wall);
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
                            River river = new River(IOUtils.parseCoords(coords));
                            rivers.add(river);
                        }
                        break;
                    case "table":
                        for (String coords: propertyValue.split(";")) {
                            Table table = new Table(IOUtils.parseCoords(coords));
                            tables.add(table);
                        }
                        break;
                    case "basket":
                        for (String coords: propertyValue.split(";")) {
                            Basket basket = new Basket(IOUtils.parseCoords(coords));
                            baskets.add(basket);
                        }
                        break;
                    default:
                }
            }
        }
    }

    public void update(Input input) {
        // update and draw all active game objects in this room
        primaryDoor.update(player);
        primaryDoor.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }

        secondaryDoor.update(player);
        secondaryDoor.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }

        if (keyBulletKin.isActive()) {
            keyBulletKin.update(player);
            keyBulletKin.draw();
        } else if (keyBulletKin.isDead() && !keyBulletKin.hasDroppedKey()) {
            // Drop key when KeyBulletKin dies
            Key droppedKey = new Key(keyBulletKin.getPosition());
            keys.add(droppedKey);
            keyBulletKin.setKeyDropped();
        }

        // Update and draw keys
        for (Key key : keys) {
            key.update(player);
            key.draw();
        }

        for (Wall wall: walls) {
            wall.update(player);
            wall.draw();
        }

        for (River river: rivers) {
            river.update(player);
            river.draw();
        }

        for (Table table : tables) {
            table.update(player);
            table.draw();
        }

        for (Basket basket : baskets) {
            basket.update(player);
            basket.draw();
        }

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

        // Update and draw bullets
        updateBullets();

        if (noMoreEnemies() && !isComplete()) {
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
            bullet.draw();

            // Check collision with walls
            for (Wall wall : walls) {
                if (bullet.isActive() && bullet.getBoundingBox().intersects(wall.getBoundingBox())) {
                    bullet.setActive(false);
                    break;
                }
            }

            // Check collision with KeyBulletKin
            if (bullet.isActive() && keyBulletKin.isActive()) {
                if (bullet.getBoundingBox().intersects(keyBulletKin.getImage().getBoundingBoxAt(keyBulletKin.getPosition()))) {
                    keyBulletKin.takeDamage(bullet.getDamage());
                    bullet.setActive(false);
                }
            }

            // Check collision with tables
            for (Table table : tables) {
                if (bullet.isActive() && !table.isDestroyed() && bullet.getBoundingBox().intersects(table.getBoundingBox())) {
                    table.destroy();
                    bullet.setActive(false);
                    break;
                }
            }

            // Check collision with baskets
            for (Basket basket : baskets) {
                if (bullet.isActive() && !basket.isDestroyed() && bullet.getBoundingBox().intersects(basket.getBoundingBox())) {
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

    private boolean stopUpdatingEarlyIfNeeded() {
        if (stopCurrentUpdateCall) {
            player = null;
            stopCurrentUpdateCall = false;
            return true;
        }
        return false;
    }

    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Door findDoorByDestination(String roomName) {
        if (primaryDoor.toRoomName.equals(roomName)) {
            return primaryDoor;
        } else {
            return secondaryDoor;
        }
    }

    private void unlockAllDoors() {
        primaryDoor.unlock(false);
        secondaryDoor.unlock(false);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void activateEnemies() {
        keyBulletKin.setActive(true);
    }

    public boolean noMoreEnemies() {
        return keyBulletKin.isDead();
    }
}
