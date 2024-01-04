package AdventureModel;


/**
 * All the information about a door, including
 * its destination, lock, and the player's X Y spawn
 * position after going through.
 */
public class DoorInfo {
    public int destination;
    public String neededItem;
    public int spawnX;
    public int spawnY;

    /**
     * Door Information
     *
     * @param destinationRoom the room number that this door leads to
     * @param neededItem if "key" (if any) for this door, that the player
     *                   needs in their inventory to pass.
     * @param x the x position the player will spawn at on the other side
     * @param y the y position ibid.
     */
    public DoorInfo(int destinationRoom, String neededItem, int x, int y) {
        this.destination = destinationRoom;
        this.neededItem = neededItem;
        this.spawnX = x;
        this.spawnY = y;
    }
}
