package AdventureModel;

import com.sun.source.tree.Tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CurrentRoom {
    public String[][] tileMap;
    public String[][] objectMap;

    public ArrayList<Enemy> enemyList = new ArrayList<>();
    Map<Integer[], DoorInfo> doors = new TreeMap<>(Arrays::compare);

    /**
        The current room the player is in, and all of it's
        tiles and objects.
     */
    public CurrentRoom() throws IOException {
        setNewRoom(1); // Initialize room number to 1
    }

    public String[][] getTileMap() {
        return tileMap;
    }
    public String[][] getObjectMap() {
        return objectMap;
    }

    /**
        Update the current room the player is in.

        Reads from the files for the TileSet and the ObjectSet

        @param roomNumber: next room number
     */
    public void setNewRoom(int roomNumber) throws IOException {
        this.tileMap = readRoomFromFile(roomNumber);
        this.objectMap = readObjectsFromFile(roomNumber);
    }



    /**
        Get an object at an x,y position in the current room.

        @param x, y: the coordinates
        @return a String that is the object
     */
    public String getObjectAtTile(int x, int y) {
        return this.getObjectMap()[y][x];
    }

    /**
     * Get a door's destination at an x,y position.
     * If there is not a door at the x,y position, returns null.
     *
     * @param x,y: the coordinates
     * @return an integer which is the room number to go to.
     */
    public DoorInfo getDoorDestinationAt(int x, int y) {
        Integer[] tuple = new Integer[]{y, x};

        return this.doors.get(tuple);
    }

    /*
        Delete an object at an x,y position in the current room.

        @param x,y: the coordinates
     */
    public void deleteObjectAt(int x, int y) {
        objectMap[y][x] = null;
    }

    /**
        Extract the room's tileset data from a file.

        The first 12 lines in a room's file dictates the tileset of the room.
        Each line has 15 comma-separated columns, for a 15x12 room.

        @param roomNumber: the room file to be read.
     */
    public String[][] readRoomFromFile(int roomNumber) throws IOException {
        String[][] out = new String[12][15];

        BufferedReader buff = new BufferedReader(new FileReader("Games" + File.separator + "TinyGame" + File.separator + "Rooms" + File.separator + roomNumber + ".txt"));

        // First 12 lines will be the room tileset
        for(int i = 0; i < 12; i++) {
            String line = buff.readLine().replace(" ", "");
            out[i] = line.split(",");
        }

        return out;
    }

    /**
        Extract the room's initial object data from a file.

        Lines 14+ in a room's file dictates the tileset of the room.
        Each line has an object's type, and the X Y coordinates
        separated by spaces.

        @param roomNumber: the room file to be read.
     */
    public String[][] readObjectsFromFile(int roomNumber) throws IOException {
        String[][] out = new String[12][15];

        BufferedReader buff = new BufferedReader(new FileReader("Games" + File.separator + "TinyGame" + File.separator + "Rooms" + File.separator + roomNumber + ".txt"));

        // 12 lines starting from line 14 will be the room object set
        for(int i = 0; i < 13; i++) {
            String line = buff.readLine();
        }
        while(buff.ready()) {
            String line = buff.readLine();
            String[] temp = line.split(" ");

            int x = Integer.parseInt(temp[1]);
            int y = Integer.parseInt(temp[2]);

            out[y][x] = temp[0];

            // If there is a DOOR on this line, add it to the passage
            if(temp[0].equals("DOOR")) {

                // A door in a room file has the following format:
                // DOOR x y roomnumber spawnx spawny requireditem

                // required item is optional

                Integer destination = Integer.parseInt(temp[3]);
                Integer spawnX = Integer.parseInt(temp[4]);
                Integer spawnY = Integer.parseInt(temp[5]);

                String neededItem = null;
                if (temp.length > 6) { // Optional required item to pass through this door
                    neededItem = temp[6];
                }

                this.doors.put(new Integer[]{y, x}, new DoorInfo(destination, neededItem, spawnX, spawnY));
            }

            else if (temp[0].equals("SLIME")){
                Slime e = new Slime(10,1);
                e.setX(Integer.parseInt(temp[1]));
                e.setY(Integer.parseInt(temp[2]));
                this.enemyList.add(e);


            }

            else if (temp[0].equals("BOSS")){
                Boss e = Boss.getInstance(5, 3);
                e.setX(Integer.parseInt(temp[1]));
                e.setY(Integer.parseInt(temp[2]));
                this.enemyList.add(e);


            }

        }

        return out;
    }
}
