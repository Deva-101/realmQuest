package AdventureModel;

import javafx.scene.input.MouseEvent;

/**
 * An interface that a usable item will implement.
 * (Strategy design pattern)
 */
public interface IUsableItem {
    /**
     * Different items will have different implementations
     * for what happens when they are used. But each item can be
     * used (by clicking on the button), which will call this use() method.
     */
    void use(MouseEvent event, AdventureGame model);

}
