package AdventureModel;

import views.AdventureGameView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public class Player implements Serializable, IObservablePlayer {
    /**
     * The current room that the player is located in.
     */
    private Room currentRoom;

    /**
     * The list of items that the player is carrying at the moment.
     */
    public ArrayList<AdventureObject> inventory;
    private ArrayList<String> playerInventory;

    // Observers will know when the player stats change, so that the UI can update
    // (Observer pattern)
    private ArrayList<IPlayerObserver> observers = new ArrayList<>();
    private int x;
    private int y;
    private int hp;
    private int maxHp;
    private int prevX;
    private int prevY;
    public Direction facing;

    public enum Direction {
        NORTH, EAST, SOUTH, WEST
    }
    /**
     * Adventure Game Player Constructor
     */
    public Player(Room currentRoom) {
        this.playerInventory = new ArrayList<>();
        this.currentRoom = currentRoom;

        this.hp = 4;
        this.maxHp = 10;

        // These are default backup values so that JUST IN CASE
        // the file doesn't have the proper information (for some reason)
        // then it doesn't crash.
        this.x = 2;
        this.y = 2;
        this.prevX = 2;
        this.prevY = 2;
        this.facing = Direction.NORTH;
    }

    /**
     * Move the player to an x, y position in the room.
     *
     * @param nextX The next x-coordinate for the player.
     * @param nextY The next y-coordinate for the player.
     */
    public void move(int nextX, int nextY) {
        prevX = x;
        prevY = y;
        x = nextX;
        y = nextY;
    }

    /**
     * Set the x-coordinate of the player.
     *
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y-coordinate of the player.
     *
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the current x-coordinate of the player.
     *
     * @return The current x-coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the current y-coordinate of the player.
     *
     * @return The current y-coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set the previous x-coordinate of the player.
     *
     * @param x The previous x-coordinate.
     */
    public void setPrevX(int x) {
        this.prevX = x;
    }

    /**
     * Set the previous y-coordinate of the player.
     *
     * @param y The previous y-coordinate.
     */
    public void setPrevY(int y) {
        this.prevY = y;
    }

    /**
     * Get the previous x-coordinate of the player.
     *
     * @return The previous x-coordinate.
     */
    public int getPrevX() {
        return this.prevX;
    }

    /**
     * Get the previous y-coordinate of the player.
     *
     * @return The previous y-coordinate.
     */
    public int getPrevY() {
        return this.prevY;
    }

    /**
     * Get the direction the player is facing.
     *
     * @return The direction the player is facing.
     */
    public Direction getFacing() {
        return this.facing;
    }

    /**
     * Set the direction the player is facing.
     *
     * @param direction The new direction.
     */
    public void setFacing(Direction direction) {
        this.facing = direction;
    }

    /**
     * Heal the player by a specified amount.
     *
     * @param amount The amount to heal.
     */
    public void heal(int amount) {
        if (this.hp + amount >= this.maxHp) {
            this.hp = this.maxHp;
        } else {
            this.hp += amount;
        }
        this.notifyObservers();
    }

    /**
     * Get the player's current health.
     *
     * @returns The HP of the player
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * Register an observer to monitor the player.
     *
     * @param o The observer to register.
     */
    public void registerObserver(IPlayerObserver o) {
        this.observers.add(o);
    }

    /**
     * Unregister an observer monitoring the player.
     *
     * @param o The observer to unregister.
     */
    public void dropObserver(IPlayerObserver o) {
        this.observers.remove(o);
    }

    /**
     * Notify all registered observers that the player has been updated.
     */
    public void notifyObservers() {
        for (IPlayerObserver o : this.observers) {
            o.updatePlayer();
        }
    }

    /**
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void dropObject(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) {
                this.currentRoom.addGameObject(this.inventory.get(i));
                this.inventory.remove(i);
            }
        }
    }

    /**
     * Setter method for the current room attribute.
     *
     * @param currentRoom The location of the player in the game.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * This method adds an object to the inventory of the player.
     *
     * @param object Prop or object to be added to the inventory.
     */
    public void addToInventory(AdventureObject object) {
        this.inventory.add(object);
    }


    /**
     * Getter method for the current room attribute.
     *
     * @return current room of the player.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Getter method for string representation of inventory.
     *
     * @return ArrayList of names of items that the player has.
     */
    public ArrayList<String> getInventory() {
        return this.playerInventory;
    }


}
