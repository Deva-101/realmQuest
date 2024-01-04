package AdventureModel;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class Key extends InventoryItem implements IUsableItem {

    /**
     * A key for a door.
     *
     * @param name Name of the key (important for which door it unlocks)
     * @param model The game model
     */
    public Key(String name, AdventureGame model) {
        super(name, "Games" + File.separator + "TinyGame" + File.separator + "Tiles" + File.separator + "KEY.png");
        this.consumable = false;

        this.setStyle("-fx-background-color: orange");

        this.setOnMouseClicked((event) -> {
            this.use(event, model);
        });

    }

    public void use(MouseEvent event, AdventureGame model) {
        String[][] objectMap = model.getRoom().getObjectMap();
        String[][] tileMap = model.getRoom().getTileMap();
        Player player = model.getPlayer();

        System.out.println(player.getX());
        System.out.println(player.getY());
    }

    public void use() {
        System.out.println("The key doesn't seem to do anything...");
    }
}
