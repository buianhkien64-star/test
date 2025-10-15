import bagel.Image;
import bagel.Input;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Abstract base class for all game rooms
 */
public abstract class GameRoom {
    protected String roomId;
    protected Image backgroundImage;
    protected ArrayList<Door> doors;
    protected ArrayList<Wall> walls;
    protected ArrayList<River> rivers;
    protected Player player;
    protected boolean stopCurrentUpdateCall = false;

    public GameRoom(String roomId) {
        this.roomId = roomId;
        this.doors = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.rivers = new ArrayList<>();
    }

    /**
     * Initialize entities specific to this room from properties
     */
    public abstract void initEntities(Properties gameProperties);

    /**
     * Update room state
     */
    public abstract void update(Input input);

    /**
     * Render room-specific content
     */
    protected void renderCommonEntities() {
        // Render doors
        for (Door door : doors) {
            door.draw();
        }

        // Render walls
        for (Wall wall : walls) {
            wall.draw();
        }

        // Render rivers
        for (River river : rivers) {
            river.draw();
        }
    }

    /**
     * Update common entities (doors, walls, rivers)
     */
    protected boolean updateCommonEntities() {
        // Update doors
        for (Door door : doors) {
            door.update(player);
            if (stopUpdatingEarlyIfNeeded()) {
                return true;
            }
        }

        // Update walls
        for (Wall wall : walls) {
            wall.update(player);
        }

        // Update rivers
        for (River river : rivers) {
            river.update(player);
        }

        return false;
    }

    protected boolean stopUpdatingEarlyIfNeeded() {
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

    public String getRoomId() {
        return roomId;
    }

    /**
     * Find door by destination room name
     */
    public Door findDoorByDestination(String roomName) {
        for (Door door : doors) {
            if (door.toRoomName != null && door.toRoomName.equals(roomName)) {
                return door;
            }
        }
        return doors.isEmpty() ? null : doors.get(0);
    }

    public Door findDoorByDestination() {
        return doors.isEmpty() ? null : doors.get(0);
    }
}
