package AdventureModel;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 *  An item in the player's inventory.
 *  To use it, the item button will be clicked.
 *  When used, the use() method in the Item is called.
 */
public abstract class InventoryItem extends Button implements IUsableItem {
    public String name;
    public String image;
    public boolean consumable;

    /**
     * An item in the player's inventory.
     * To use it, the item will be clicked.
     *
     * @param name The name of the item
     * @param image The sprite that the item uses.
     */
    public InventoryItem(String name, String image) {

        ImageView btnImg = new ImageView(new Image(image)); // image is a path

        // Style the button
        btnImg.setPreserveRatio(true);
        btnImg.setFitHeight(40);
        btnImg.setFitWidth(40);

        Text label = new Text(name);
        Font font = new Font(12);
        label.setFill(Color.BLACK);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(font);

        VBox vbox = new VBox(btnImg, label);

        vbox.setPrefWidth(80);
        vbox.setMinWidth(80);

        vbox.setPrefHeight(53);
        vbox.setMinHeight(53);

        vbox.setAlignment(Pos.CENTER);

        this.setGraphic(vbox);
        this.setStyle("-fx-background-color: white");

        // Cool hover effect
        this.setOnMouseEntered((event) -> this.setOpacity(0.75));
        this.setOnMouseExited((event) -> this.setOpacity(1));
    }

    /**
     * When the button is clicked, this method will fire.
     * By default, all it does is check if the item is consumable,
     * and if so, it self-destructs on usage.
     *
     * @param event The mouse event
     * @param model The game model
     */
    public void use(MouseEvent event, AdventureGame model) {
        // Delete this button if it's a single use item.
        if(this.consumable) {
            HBox parent = (HBox) this.getParent();
            parent.getChildren().remove(this);
        }
    }

}
