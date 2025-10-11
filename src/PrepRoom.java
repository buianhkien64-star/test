import bagel.Input;
import bagel.Keys;

import java.util.Map;
import java.util.Properties;

/**
 * Room where the game starts
 */
public class PrepRoom {
    private Player player;
    private Door door;
    private RestartArea restartArea;
    private CharacterSprite robotSprite;
    private CharacterSprite marineSprite;
    private boolean stopCurrentUpdateCall = false; // this determines whether to prematurely stop the update execution

    public void initEntities(Properties gameProperties) {
        // find the configuration of game objects for this room
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", ShadowDungeon.PREP_ROOM_NAME);
            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString().substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();

                switch (objectType) {
                    case "door":
                        String[] coordinates = propertyValue.split(",");
                        door = new Door(IOUtils.parseCoords(propertyValue), coordinates[2]);
                        break;
                    case "restartarea":
                        restartArea = new RestartArea(IOUtils.parseCoords(propertyValue));
                        break;
                    default:
                }
            }
        }

        // Initialize character selection sprites
        robotSprite = new CharacterSprite(IOUtils.parseCoords(gameProperties.getProperty("Robot")), "res/robot_sprite.png");
        marineSprite = new CharacterSprite(IOUtils.parseCoords(gameProperties.getProperty("Marine")), "res/marine_sprite.png");
    }

    public void update(Input input) {
        UserInterface.drawStartMessages();
        UserInterface.drawCharacterDescriptions();

        // Draw character selection sprites
        robotSprite.draw();
        marineSprite.draw();

        // update and draw all game objects in this room
        door.update(player);
        door.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }

        restartArea.update(input, player);
        restartArea.draw();

        if (player != null) {
            // Handle character selection
            if (input.wasPressed(Keys.R)) {
                player.setPlayerType(PlayerType.ROBOT);
                // Unlock door after character selection
                if (!findDoor().isUnlocked()) {
                    findDoor().unlock(false);
                }
            } else if (input.wasPressed(Keys.M)) {
                player.setPlayerType(PlayerType.MARINE);
                // Unlock door after character selection
                if (!findDoor().isUnlocked()) {
                    findDoor().unlock(false);
                }
            }

            player.update(input);
            player.draw();
        }
    }

    private boolean stopUpdatingEarlyIfNeeded() {
        if (stopCurrentUpdateCall) {
            player = null;
            stopCurrentUpdateCall = false;
            return true;
        }
        return false;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }

    public Door findDoor() {
        return door;
    }

    public Door findDoorByDestination() {
        return door;
    }
}
