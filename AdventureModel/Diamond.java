package AdventureModel;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class Diamond extends InventoryItem implements IUsableItem {

    /**
     * A key for a door.
     *
     * @param name Name of the key (important for which door it unlocks)
     * @param model The game model
     */
    public Diamond(String name, AdventureGame model) {
        super(name, "Games" + File.separator + "TinyGame" + File.separator + "Tiles" + File.separator + "DIAMOND.png");
        this.consumable = false;

        this.setStyle("-fx-background-color: lightblue");

        this.setOnMouseClicked((event) -> {
            this.use(event, model);
        });

    }

    public void use(MouseEvent event, AdventureGame model) {
        System.out.println("The sparkling DIAMOND is a spectacle to behold.");
    }

}
