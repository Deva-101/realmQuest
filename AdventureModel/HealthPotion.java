package AdventureModel;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class HealthPotion extends InventoryItem implements IUsableItem {
    private int healing;

    /**
     * A health potion.
     * This will heal the player.
     *
     * @param name: name of the item
     * @param model: The game's model
     */
    public HealthPotion(String name, AdventureGame model) {
        super(name, "Games" + File.separator + "TinyGame" + File.separator + "Tiles" + File.separator + "potion.png");
        this.consumable = true;
        this.healing = 3;
        this.setStyle("-fx-background-color: #FF7377");

        this.setOnMouseClicked((event) -> {
            this.use(event, model);
        });

    }

    /**
     *
     * @return Get how much this will heal by
     */
    public int getHealing() {
        return this.healing;
    }


    /**
     * Heals the player for the amount that this potion heals by.
     * Also notifies all observers that a healing has happened.
     *
     * @param event The mouse event
     * @param model The game model
     */
    public void use(MouseEvent event, AdventureGame model) {
        super.use(event, model);

        Player player = model.getPlayer();
        player.heal(this.getHealing());
    }
}
