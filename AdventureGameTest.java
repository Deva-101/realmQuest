import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import AdventureModel.*;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.Test;
import views.AdventureGameView;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class AdventureTest.
 * Course code from the CSC207 instructional
 * team at UTM, contributors to tests include:
 *
 * @author iselein
 * @author anshag01
 *  */
public class AdventureGameTest {
    @Test
    void initialSetupGameTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");

        assertNotNull(game.getRoom());

        // Check if rooms are correct size
        assertEquals(12, game.getRoom().getObjectMap().length);
        assertEquals(15, game.getRoom().getObjectMap()[0].length);

        assertEquals(12, game.getRoom().getTileMap().length);
        assertEquals(15, game.getRoom().getTileMap()[0].length);

    }
}

