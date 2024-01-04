package AdventureModel;

import java.util.ArrayList;


/**
 * A special tile that has a door on it.
 * Doors move the player from room to room.
 */
public class Door extends Tile {
    public boolean locked = false;
    public String neededItem = null;

    public Door(boolean isSolid, TileSet tile, String neededItem) {
        super(isSolid, tile, ObjectSet.DOOR); // Make the image a DOOR

        if(neededItem != null) {
            this.locked = true;
            this.neededItem = neededItem;
        }
    }

    @Override
    public boolean onPlayerMovesHere(AdventureGame model) {
        if(!this.locked) return true; // don't need to check if it's not locked!

        // If the door is locked, player needs an item to pass.
        Player player = model.getPlayer();

        ArrayList<String> inv = player.getInventory();

        // If the item is in the inventory, great! The event function continues
        // If it doesn't... too bad!
        boolean willPass = inv.contains(this.neededItem) || this.neededItem == null;

        if(!willPass) System.out.println("The door is LOCKED! It looks like it needs a " + this.neededItem + ".");

        return willPass;

    }
}
