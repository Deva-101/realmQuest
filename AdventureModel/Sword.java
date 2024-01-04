package AdventureModel;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class Sword extends InventoryItem implements IUsableItem {
    private int damage;

    /**
     * A sword wielded by the player.
     *
     * @param name The name of this sword
     * @param model The game model
     */
    public Sword(String name, AdventureGame model) {
        super(name, "Games" + File.separator + "TinyGame" + File.separator + "Tiles" + File.separator + "sword.png");
        this.consumable = false;
        this.damage = 1;

        this.setStyle("-fx-background-color: #CBC3E3");

        this.setOnMouseClicked((event) -> {
            this.use(event, model);
        });
    }

    /**
     * Get the damage of this weapon
     *
     * @return The damage
     */
    public int getDamage() {
        return this.damage;
    }


    /**
     * Slashes the sword, and hits a tile.
     *
     * @param event The mouse event
     * @param model The game model
     */
    public void use(MouseEvent event, AdventureGame model) {
        Player player = model.getPlayer();

        int x = player.getX();
        int y = player.getY();

        Player.Direction facing = player.getFacing();

        switch(facing) {
            case NORTH -> y--;
            case SOUTH -> y++;
            case EAST -> x++;
            case WEST -> x--;
        }

        String hittingTile = model.getRoom().getTileMap()[y][x];

        System.out.println("You slash your SWORD, hitting the " + hittingTile + " in front of you.");
    }

}
