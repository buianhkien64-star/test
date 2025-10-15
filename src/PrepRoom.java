import bagel.Input;
import bagel.Keys;

import java.util.Map;
import java.util.Properties;

/**
 * Room where the game starts - handles character selection
 */
public class PrepRoom extends GameRoom {
    private RestartArea restartArea;
    private CharacterSprite robotSprite;
    private CharacterSprite marineSprite;

    public PrepRoom() {
        super(ShadowDungeon.PREP_ROOM_NAME);
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

                switch (objectType) {
                    case "door":
                        String[] coordinates = propertyValue.split(",");
                        doors.add(new Door(IOUtils.parseCoords(propertyValue), coordinates[2]));
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

    @Override
    public void update(Input input) {
        UserInterface.drawStartMessages();
        UserInterface.drawCharacterDescriptions();

        // Draw character selection sprites
        robotSprite.draw();
        marineSprite.draw();

        // Update and draw door
        for (Door door : doors) {
            door.update(player);
            door.draw();
            if (stopUpdatingEarlyIfNeeded()) {
                return;
            }
        }

        restartArea.update(input, player);
        restartArea.draw();

        if (player != null) {
            // Handle character selection
            if (input.wasPressed(Keys.R)) {
                player.setPlayerType(PlayerType.ROBOT);
                // Unlock door after character selection
                if (!doors.isEmpty() && !doors.get(0).isUnlocked()) {
                    doors.get(0).unlock(false);
                }
            } else if (input.wasPressed(Keys.M)) {
                player.setPlayerType(PlayerType.MARINE);
                // Unlock door after character selection
                if (!doors.isEmpty() && !doors.get(0).isUnlocked()) {
                    doors.get(0).unlock(false);
                }
            }

            player.update(input);
            player.draw();
        }

        // Update and draw store
        Store store = ShadowDungeon.getStore();
        if (store != null) {
            store.update(input, player);
            store.draw();
        }
    }

    public Door findDoor() {
        return doors.isEmpty() ? null : doors.get(0);
    }
}
