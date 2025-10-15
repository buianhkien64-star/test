import bagel.Input;

import java.util.Map;
import java.util.Properties;

/**
 * Room where the game ends when the player either completes all rooms or dies
 */
public class EndRoom extends GameRoom {
    private RestartArea restartArea;
    private boolean isGameOver = false;

    public EndRoom() {
        super(ShadowDungeon.END_ROOM_NAME);
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
    }

    @Override
    public void update(Input input) {
        UserInterface.drawEndMessage(!isGameOver);

        // Door should be locked if player got to this room by dying
        if (isGameOver && !doors.isEmpty()) {
            doors.get(0).lock();
        }

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

    public void isGameOver() {
        isGameOver = true;
    }
}
