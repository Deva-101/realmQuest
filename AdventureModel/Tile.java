package AdventureModel;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.io.File;

public class Tile extends StackPane {
    private static String PATH = "Games" + File.separator + "TinyGame"  + File.separator  + "Tiles" + File.separator;
    private boolean isSolid;
    private TileSet tile;
    public ObjectSet object;
    private ImageView backgroundImageView;
    private ImageView objectImageView;

    public enum TileSet {
        GRASS(PATH + "GRASS.png"),
        STONE(PATH + "STONE.png"),
        DIRT(PATH + "DIRT.png"),
        WOOD(PATH + "WOOD.png"),
        LOG(PATH + "LOG.png"),
        BLOCK(PATH + "BLOCK.png"),

        SECRET(PATH + "SECRET.png"),
        FLOOR(PATH + "FLOOR.png"),
        CARPET(PATH + "CARPET.png"),
        NULL(PATH + "NULL.png"),
        BLOCK_DARK(PATH + "BLOCK.png"),
        FLOOR_DARK(PATH + "FLOOR_DARK.png"),
        LOG_DARK(PATH + "LOG_DARK.png"),
        GRASS_DARK(PATH + "GRASS_DARK.png"),
        DIRT_DARK(PATH + "DIRT_DARK.png"),
        STONE_DARK(PATH + "STONE_DARK.png"),

        SECRET_DARK(PATH + "SECRET_DARK.png"),

        CARPET_DARK(PATH + "CARPET_DARK.png"),
        WOOD_DARK(PATH + "WOOD_DARK.png");
        private String imagePath;

        TileSet(String imagePath) {
            this.imagePath = imagePath;
        }

        public Image getImage() {
            return imagePath == null ? null : new Image(imagePath);
        }
    }

    public enum ObjectSet {
        PLAYER_SOUTH(PATH + "player_south.png"),
        PLAYER_NORTH(PATH + "player_north.png"),
        PLAYER_EAST(PATH + "player_east.png"),
        PLAYER_WEST(PATH + "player_west.png"),
        HEALTHPOTION(PATH + "potion.png"),
        SWORD(PATH + "sword.png"),
        KEY(PATH + "key.png"),
        DOOR(PATH + "door.png"),
        SLIME(PATH + "SLIME.png"),
        BOSS(PATH + "BOSS.png"),

        GOLD(PATH + "GOLD.png"),
        DIAMOND(PATH + "DIAMOND.png"),

        NONE(null); // Use NONE when there's no object on the tile

        private String imagePath;

        ObjectSet(String imagePath) {
            this.imagePath = imagePath;
        }

        public Image getImage() {
            return imagePath == null ? null : new Image(imagePath);
        }
    }

    public Tile(boolean isSolid, TileSet tile) {
        this(isSolid, tile, ObjectSet.NONE);
    }

    public Tile(boolean isSolid, TileSet tile, ObjectSet object) {
        this.isSolid = isSolid;
        this.tile = tile;

        backgroundImageView = new ImageView(tile.getImage());
        setupImageView(backgroundImageView);

        getChildren().add(backgroundImageView);

        this.object = object;

        if (object != ObjectSet.NONE) {
            objectImageView = new ImageView(object.getImage());
            setupImageView(objectImageView);
            getChildren().add(objectImageView);
        }
    }

    private void setupImageView(ImageView imageView) {
        imageView.setFitWidth(52);
        imageView.setFitHeight(52);
        imageView.setPreserveRatio(true);
    }

    public boolean isSolid() {
        return isSolid;
    }

    // Method to set or change the object on the tile
    public void setObject(ObjectSet object) {
        this.object = Tile.ObjectSet.NONE;

        if (objectImageView != null) {
            getChildren().remove(objectImageView);
        }

        if (object != ObjectSet.NONE) {
            objectImageView = new ImageView(object.getImage());
            setupImageView(objectImageView);
            getChildren().add(objectImageView);
        } else {
            objectImageView = null;
        }
    }

    public boolean onPlayerMovesHere(AdventureGame model) {
        return true; // By default, no special action needed.
    }
}
