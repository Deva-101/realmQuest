package views;

import AdventureModel.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 *
 *
 */
public class AdventureGameView implements IPlayerObserver {

    AdventureGame model; //model of the game
    Stage menuStage;
    Stage stage; //stage on which all is rendered
    Button saveButton, loadButton, helpButton; //buttons
    Boolean helpToggle = false; //is help on display?

    GridPane gridPane = new GridPane(); //to hold images and buttons
    GridPane menuPane = new GridPane();
    GridPane gameOverPane = new GridPane();
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

    HBox healthBox = new HBox();
    HBox inventoryBox = new HBox();

    VBox inventoryWrapper = new VBox();

    GridPane gameGrid = new GridPane();
    Boolean darkMode = true;
    Boolean accessibilityToggle = false;


    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.menuStage = stage;
        this.stage = stage;
        createMainMenu();
    }


    /**Creates a main menu screen before the game begins
     * */
    public void createMainMenu(){
        // Reset the menupane each time
        this.menuPane = new GridPane();

        // Initialize the grid pane for the menu
        menuPane.setPadding(new Insets(0));
        if (darkMode) {
            menuPane.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#000000"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
        } else {
            menuPane.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#ffffff"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
        }
        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(500);
        ColumnConstraints column2 = new ColumnConstraints(500);

        // Row constraints
        RowConstraints row1 = new RowConstraints(125);
        RowConstraints row2 = new RowConstraints(675);

        menuPane.getColumnConstraints().addAll( column1 , column2);
        menuPane.getRowConstraints().addAll( row1 , row2);

        // GAME TITLE
        Label gameLabel = new Label("REALM QUEST");
        if (darkMode) {
            gameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 50px;");
        } else{
            gameLabel.setStyle("-fx-text-fill: black; -fx-font-size: 50px;");
        }

        HBox gameTitle = new HBox();
        gameTitle.getChildren().add(gameLabel);
        gameTitle.setAlignment(Pos.CENTER);

        menuPane.add(gameTitle, 0, 0, 2, 1); // Add game title to screen

        // vbox for menu contents (contains hbox for buttons, and vbox for help text)
        VBox menu = new VBox();
        menu.setSpacing(50);

        // hbox for button collection
        HBox buttons = new HBox();
        buttons.setSpacing(50);
        buttons.setAlignment(Pos.CENTER);

        // start game button
        Button startButton = new Button("Start Game");
        customizeButton(startButton, 200, 50);
        addStartEvent(startButton); // add an event listener
        buttons.getChildren().add(startButton);

        // accessibility button
        Button accessibilityToggle = new Button();
        if (darkMode){
            accessibilityToggle.setText("Light Mode");
        } else {
            accessibilityToggle.setText("Dark Mode");
        }
        customizeButton(accessibilityToggle, 200, 50);
        addLightDarkToggleEvent(accessibilityToggle); // add an event listener
        buttons.getChildren().add(accessibilityToggle);

        menu.getChildren().add(buttons); // add button collection to menu hbox

        // vbox for help text (includes caption and text)
        VBox help = new VBox();
        help.setAlignment(Pos.CENTER);

        // help label
        Label helpTitle = new Label();
        helpTitle.setText("HOW TO PLAY: ");
        if (darkMode) {
            helpTitle.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
        } else {
            helpTitle.setStyle("-fx-text-fill: black; -fx-font-size: 25px;");
        }

        help.getChildren().add(helpTitle);

        // help text
        String helpText = getHelpText();
        Label helpTextLabel = new Label();
        helpTextLabel.setText(helpText);
        helpTextLabel.setAlignment(Pos.CENTER);
        if (darkMode) {
            helpTextLabel.setStyle("-fx-border-color: white; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        } else{
            helpTextLabel.setStyle("-fx-border-color: black; -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10px;");
        }

        helpTextLabel.setMaxWidth(600);
        helpTextLabel.setWrapText(true);
        help.getChildren().add(helpTextLabel);

        menu.getChildren().add(help); // add help text to menu hbox

        menu.setAlignment(Pos.CENTER);
        menuPane.add(menu, 0, 1, 2, 1); // add menu hbox to menu gridpane


        var menuScene = new Scene( menuPane ,  1000, 800);
        menuScene.setFill(Color.BLACK);

        this.menuStage.setScene(menuScene);
        this.menuStage.setResizable(false);
        this.menuStage.show();



    }
    /**
     * This method handles the event related to the
     * start button found in the initial game menu.
     *
     * @param startButton the button to add the event listener on
     */
    public void addStartEvent(Button startButton){
        startButton.setOnAction(e -> {
            menuPane.requestFocus();
            menuPane.getChildren().clear();
            intiUI();
        });
    }

    /**
     * This method handles the event related to the
     * Light/Dark Toggle button found in the initial game menu.
     *
     * @param lightDarkToggle the button to add the event listener on
     */
    public void addLightDarkToggleEvent(Button lightDarkToggle){
        lightDarkToggle.setOnAction(e -> {
            darkMode = !darkMode;
            createMainMenu();
        });
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {
        // Reset everything
        this.gridPane = new GridPane();
        this.inventoryWrapper = new VBox();
        this.healthBox = new HBox();
        this.model.getPlayer().getInventory().clear();
        this.inventoryBox = new HBox();

        // setting up the stage
        this.stage.setTitle("Group 12's Adventure Game"); //Replace <YOUR UTORID> with your UtorID

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(0));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(500);
        ColumnConstraints column2 = new ColumnConstraints(500);

        // Row constraints
        RowConstraints row1 = new RowConstraints(125);
        RowConstraints row2 = new RowConstraints(675);

        gridPane.getColumnConstraints().addAll( column1 , column2);
        gridPane.getRowConstraints().addAll( row1 , row2);

        this.drawHealthBar();

        // Inventory wrapper will have an "INVENTORY" label, and the actual inventory
        inventoryWrapper.setAlignment(Pos.TOP_CENTER);
        inventoryWrapper.setStyle("-fx-background-color: #536878");

        Label inventoryLabel = new Label("INVENTORY");
        inventoryLabel.setTextFill(Color.WHITE);
        inventoryLabel.setFont(new Font(16));
        inventoryLabel.setStyle("-fx-background-color: #36454f;");
        inventoryLabel.setMinWidth(500);
        inventoryLabel.setMaxWidth(500);
        inventoryLabel.setMinHeight(30);
        inventoryLabel.setAlignment(Pos.CENTER); // This will center the label's text

        inventoryBox.setPadding(new Insets(10, 10, 0, 10));
        inventoryBox.setSpacing(10);

        // Allow scrolling if items overflow
        ScrollPane scroller = new ScrollPane();

        // Style the scroller
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        scroller.setMinHeight(100);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Horizontal scrollbar
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // never vertical

        scroller.setStyle("-fx-background-color: #536878; -fx-background: transparent;");
        scroller.setContent(inventoryBox);

        // Add the label and scroller
        inventoryWrapper.getChildren().add(inventoryLabel);
        inventoryWrapper.getChildren().add(scroller);

        gridPane.add(inventoryWrapper, 1, 0);
        gameGrid.setAlignment(Pos.CENTER);

        if (darkMode) {
            gameGrid.setStyle("-fx-background-color: #09143C;");
        } else{
            gameGrid.setStyle("-fx-background-color: #143ccc;");
        }
        gameGrid.setPadding(new Insets(0));
        gridPane.add(gameGrid, 0, 1, 2, 1); // This will span the gameGrid across both columns in the second row.


        VBox bbutton = new VBox(); // HBox for bottom right action buttons (during game play)
        bbutton.setSpacing(10);
        // Instructions button / screen rendering
        Button instructionButton = new Button("Instructions");
        makeButtonAccessible(instructionButton, "Instructions", "View instructions", "Click to view Instructions.");
        customizeButton(instructionButton, 125, 50);
        bbutton.getChildren().add(instructionButton);

        VBox instructionHBox = new VBox();

        instructionHBox.setAlignment(Pos.CENTER);
        instructionHBox.setSpacing(40);
        Label instructionsLabel = new Label("Instructions");
        Button back = new Button("Resume Game");
        makeButtonAccessible(back, "Resume Game", "Return to the game", "return to playing the game.");
        customizeButton(back, 200, 50);
        instructionHBox.getChildren().add(instructionsLabel);
        instructionHBox.getChildren().add(back);

        // help text
        String helpText = getHelpText();
        Label helpTextLabel = new Label();
        helpTextLabel.setText(helpText);
        helpTextLabel.setAlignment(Pos.CENTER);
        helpTextLabel.setMaxWidth(600);
        helpTextLabel.setWrapText(true);

        instructionHBox.getChildren().add(helpTextLabel);

        back.setOnAction(e -> {
            gridPane.getChildren().remove(instructionHBox);
            drawRoom(false);
            updateScene();
        });

        instructionButton.setOnAction(e -> {
            if (darkMode){
                instructionHBox.setStyle("-fx-background-color: black;");
                instructionsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 50px;");
                helpTextLabel.setStyle("-fx-border-color: white; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 10px;");
            } else{
                instructionHBox.setStyle("-fx-background-color: white;");
                instructionsLabel.setStyle("-fx-text-fill: black; -fx-font-size: 50px;");
                helpTextLabel.setStyle("-fx-border-color: black; -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10px;");
            }

            gridPane.add(instructionHBox, 0, 1, 2, 1); // render instructions screen to display
        });


        Button accessibilityButton = new Button();

        if (darkMode){

            accessibilityButton.setText("Light Mode");
        } else{

            accessibilityButton.setText("Dark Mode");
        }
        makeButtonAccessible(accessibilityButton, "Light / Dark mode", "Click to toggle light/dark mode", "Click to toggle light/dark mode");
        customizeButton(accessibilityButton, 125, 50);
        accessibilityButton.setOnAction(e -> {
            darkMode = !darkMode;
            if (darkMode){
                gameGrid.setStyle("-fx-background-color: #09143C;");
                accessibilityButton.setText("Light Mode");
            } else{
                gameGrid.setStyle("-fx-background-color: #143ccc;");
                accessibilityButton.setText("Dark Mode");
            }
            accessibilityToggle = true;
            drawRoom(false);
            updateScene();
            accessibilityToggle = false;
        });

        bbutton.getChildren().add(accessibilityButton);
        bbutton.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.add(bbutton, 1, 1); // 0, 1

        // Add the view to the player's observer list so that
        // when the player's info updates, the UI can be updated
        this.model.getPlayer().registerObserver(this);

        // Render everything
        drawRoom(true);
        updateScene();

        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);

        // Handle player keyboard input
        scene.setOnKeyPressed(event -> {

            // Get the current player
            Player player = model.getPlayer();

            // Get the player information
            int newX = player.getX();
            int newY = player.getY();
            Player.Direction currentlyFacing = player.getFacing();

            // Calculate the next tile the player will move to,
            // or change where the character is facing
            switch (event.getCode()) {
                case W:
                    if(currentlyFacing != Player.Direction.NORTH) {
                        player.setFacing(Player.Direction.NORTH);
                        break;
                    }
                    newY -= 1; // move player up
                    break;
                case A:
                    if(currentlyFacing != Player.Direction.WEST) {
                        player.setFacing(Player.Direction.WEST);
                        break;
                    }
                    newX -= 1; // move player left
                    break;
                case S:
                    if(currentlyFacing != Player.Direction.SOUTH) {
                        player.setFacing(Player.Direction.SOUTH);
                        break;
                    }
                    newY += 1; // move player down
                    break;
                case D:
                    if(currentlyFacing != Player.Direction.EAST) {
                        player.setFacing(Player.Direction.EAST);
                        break;
                    }
                    newX += 1; // move player right
                    break;
            }


            // Check if the tile the player is going to is solid
            // If it is, abort movement
            for (Node child : gameGrid.getChildren()) {
                if (GridPane.getColumnIndex(child) == newX && GridPane.getRowIndex(child) == newY && child instanceof Tile) {

                    Tile theTile = (Tile) child;

                    if(theTile.isSolid()) {
                        return;
                    } else {
                        // Call if there is a specific thing to do on this tile.
                        // (most of the time nothing will happen, that's OK)
                        // If the call gives false, dont move the player or otherwise continue.
                        if(!theTile.onPlayerMovesHere(this.model)) return;
                    }
                }
            }

            // Get the Object
            String object = model.getRoom().getObjectAtTile(newX, newY);

            // Ignore if the item if it's empty or the player
            if(object != null && !object.contains("PLAYER")) {
                try {
                    if(object.equals("DOOR")) {
                        // Get where the door will go to, where to spawn, if an item is needed, etc.
                        DoorInfo theDoor = this.model.getRoom().getDoorDestinationAt(newX, newY);

                        Integer num = theDoor.destination;

                        // Should never happen but just in case!
                        if(num == null) return;

                        // By convention, room #0 signifies a win!
                        if(num == 0) {
                            this.showEndScreen("You win, GG!");
                            return;
                        }

                        this.model.getRoom().setNewRoom(num);

                        // Update everything
                        updateTile(player.getPrevX(), player.getPrevY(), Tile.ObjectSet.NONE);

                        // Get player spawn position
                        newY = theDoor.spawnY;
                        newX = theDoor.spawnX;

                        // Render the room
                        drawRoom(true);

                        // Move the player to the spawn point in the file
                        model.getPlayer().move(newX, newY);
                        updateScene();

                        return;
                    }
                    else if (object.equals("SLIME")){
                        //TASKS:
                        // GET ENEMY AT THE TILE GET THEIR HP AND DMG
                        // IF PLAYER HAS SWORD, AND SWORD DOES MORE DMG THAN ENEMY HP, KILL ENEMY
                        // ELSE PLAYER TAKES DMG AND STAYS THE SAME


                        // first we check if player has sword in inventory

                        if (this.model.player.getInventory().contains("SWORD")){
                            Enemy enemyAtTile = null;
                            boolean enemyPresent = false;
                            for (int i= 0; i < this.model.getRoom().enemyList.size(); i++){
                                //Get enemy at the tile which the player will move to
                                if (newX == this.model.getRoom().enemyList.get(i).x && newY== this.model.getRoom().enemyList.get(i).y){
                                     enemyAtTile = this.model.getRoom().enemyList.get(i);
                                     enemyPresent = true;




                                }
                            }

                            if (enemyPresent){
                                // Sword damage is hard coded to 3

                                enemyAtTile.setHp(enemyAtTile.hp-3);


                                if  (enemyAtTile.hp <=0){
                                    this.updateTile(enemyAtTile.x, enemyAtTile.y, Tile.ObjectSet.NONE);
                                }
                                else {
                                    this.model.player.heal(-(enemyAtTile.dmg));
                                    if (this.model.player.getHp() <= 0){
                                        this.drawLoserScreen();
                                    }
                                    else {
                                        return;

                                    }

                                }

                            }

                        }
                        else{
                            Enemy enemyAtTile = null;
                            boolean enemyPresent = false;
                            for (int i= 0; i < this.model.getRoom().enemyList.size(); i++){
                                //Get enemy at the tile which the player will move to
                                if (newX == this.model.getRoom().enemyList.get(i).x && newY== this.model.getRoom().enemyList.get(i).y){
                                    enemyAtTile = this.model.getRoom().enemyList.get(i);
                                    enemyPresent = true;
                                }
                            }

                            if (enemyPresent){
                                this.model.player.heal(-(enemyAtTile.dmg));

                                if (this.model.player.getHp() <= 0){
                                    this.drawLoserScreen();
                                }
                                else {
                                    return;

                                }


                            }
                        }






                    }

                    else if (object.equals("BOSS")){

                        //TASKS:
                        // GET ENEMY AT THE TILE GET THEIR HP AND DMG
                        // IF PLAYER HAS SWORD, AND SWORD DOES MORE DMG THAN ENEMY HP, KILL ENEMY
                        // ELSE PLAYER TAKES DMG AND STAYS THE SAME


                        // first we check if player has sword in inventory

                        if (this.model.player.getInventory().contains("SWORD")){
                            Enemy enemyAtTile = null;
                            boolean enemyPresent = false;
                            for (int i= 0; i < this.model.getRoom().enemyList.size(); i++){
                                //Get enemy at the tile which the player will move to
                                if (newX == this.model.getRoom().enemyList.get(i).x && newY== this.model.getRoom().enemyList.get(i).y){
                                    enemyAtTile = this.model.getRoom().enemyList.get(i);
                                    enemyPresent = true;




                                }
                            }

                            if (enemyPresent){
                                // Sword damage is hard coded to 3

                                enemyAtTile.setHp(enemyAtTile.hp-3);


                                if (enemyAtTile.hp <=0){
                                    this.showEndScreen("You win, GG!");

                                }
                                else {
                                    this.model.player.heal(-(enemyAtTile.dmg));
                                    if (this.model.player.getHp() <= 0){
                                        this.drawLoserScreen();
                                    }
                                    else {
                                        return;

                                    }


                                }

                            }

                        }
                        else{
                            Enemy enemyAtTile = null;
                            boolean enemyPresent = false;
                            for (int i= 0; i < this.model.getRoom().enemyList.size(); i++){
                                //Get enemy at the tile which the player will move to
                                if (newX == this.model.getRoom().enemyList.get(i).x && newY== this.model.getRoom().enemyList.get(i).y){
                                    enemyAtTile = this.model.getRoom().enemyList.get(i);
                                    enemyPresent = true;
                                }
                            }

                            if (enemyPresent){
                                this.model.player.heal(-(enemyAtTile.dmg));
                                if (this.model.player.getHp() <= 0){
                                    this.drawLoserScreen();
                                }
                                else {
                                    return;

                                }


                            }
                        }






                    }





                    else {
                        // Use ItemFactory to get an Inventory Item
                        InventoryItem item = ItemFactory.getItem(object, this.model);

                        if(item == null) throw new IllegalArgumentException("Illegal type of item, " +  object);

                        // Add to inventory
                        inventoryBox.getChildren().add(item);
                        player.getInventory().add(object);
                        this.model.getRoom().deleteObjectAt(newX, newY);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            model.getPlayer().move(newX, newY); // Update the player's position
            updateScene(); // update scene every time
        });

        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    public void showEndScreen(String topString) {
        this.gameOverPane = new GridPane(); // reset.

        // 2x1 grid
        ColumnConstraints col0 = new ColumnConstraints(1000);
        RowConstraints row0 = new RowConstraints(400);
        RowConstraints row1 = new RowConstraints(400);

        this.gameOverPane.setPrefSize(1000, 800);
        this.gameOverPane.getColumnConstraints().add(col0);
        this.gameOverPane.getRowConstraints().addAll(row0, row1);

        gameOverPane.setStyle("-fx-background: " + (darkMode ? "black" : "white"));

        // The main label at the top
        Label winner = new Label(topString);
        winner.setFont(new Font(48));
        winner.setAlignment(Pos.CENTER);
        winner.setTextAlignment(TextAlignment.CENTER);
        winner.setStyle("-fx-text-fill: " + (darkMode ? "white" : "black"));

        GridPane.setHalignment(winner, HPos.CENTER);
        gameOverPane.getChildren().add(winner);


        // An inner gridpane that is 2x2
        // will have 2 labels on the top row, and 2 buttons on the bottom row
        GridPane inner = new GridPane();

        inner.setMaxHeight(250);
        inner.setMaxWidth(600);
        inner.setStyle("-fx-border-color: " + (darkMode ? "white" : "black"));

        inner.getRowConstraints().addAll(new RowConstraints(50), new RowConstraints(100));
        inner.getColumnConstraints().addAll(new ColumnConstraints(250), new ColumnConstraints(250));

        // Top labels
        Label playAgainLabel = new Label("Play again?");
        Label quitLabel = new Label("..or exit.");

        Font font = new Font(24);
        playAgainLabel.setFont(font);
        playAgainLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(playAgainLabel, HPos.CENTER);


        quitLabel.setFont(font);
        quitLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(quitLabel, HPos.CENTER);


        // Buttons
        Button playAgainBtn = new Button("To Main Menu");
        Button quitBtn = new Button("Quit");

        customizeButton(playAgainBtn, 200, 50);
        customizeButton(quitBtn, 200, 50);
        GridPane.setHalignment(playAgainBtn, HPos.CENTER);
        GridPane.setHalignment(quitBtn, HPos.CENTER);

        // Make the buttons do things

        playAgainBtn.setOnMouseClicked(event -> {
            try {
                this.model = new AdventureGame("TinyGame");
                createMainMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        quitBtn.setOnMouseClicked(event -> Platform.exit());

        // Add them
        inner.add(playAgainLabel, 0, 0);
        inner.add(quitLabel, 1, 0);

        inner.add(playAgainBtn, 0, 1);
        inner.add(quitBtn, 1, 1);

        GridPane.setHalignment(inner, HPos.CENTER);
        inner.setAlignment(Pos.CENTER);
        gameOverPane.add(inner, 0, 1);



        // Display this scene
        var scene = new Scene(gameOverPane, 1000, 800);
        this.stage.setScene(scene);
    }

    public void drawLoserScreen() {
        this.gameOverPane = new GridPane(); // reset.

        // 2x1 grid
        ColumnConstraints col0 = new ColumnConstraints(1000);
        RowConstraints row0 = new RowConstraints(400);
        RowConstraints row1 = new RowConstraints(400);

        this.gameOverPane.setPrefSize(1000, 800);
        this.gameOverPane.getColumnConstraints().add(col0);
        this.gameOverPane.getRowConstraints().addAll(row0, row1);

        gameOverPane.setStyle("-fx-background: " + (darkMode ? "black" : "white"));

        // The main label at the top
        Label winner = new Label("You Lose, GG!");
        winner.setFont(new Font(48));
        winner.setAlignment(Pos.CENTER);
        winner.setTextAlignment(TextAlignment.CENTER);
        winner.setStyle("-fx-text-fill: " + (darkMode ? "white" : "black"));

        GridPane.setHalignment(winner, HPos.CENTER);
        gameOverPane.getChildren().add(winner);


        // An inner gridpane that is 2x2
        // will have 2 labels on the top row, and 2 buttons on the bottom row
        GridPane inner = new GridPane();

        inner.setMaxHeight(250);
        inner.setMaxWidth(600);
        inner.setStyle("-fx-border-color: " + (darkMode ? "white" : "black"));

        inner.getRowConstraints().addAll(new RowConstraints(50), new RowConstraints(100));
        inner.getColumnConstraints().addAll(new ColumnConstraints(250), new ColumnConstraints(250));

        // Top labels
        Label playAgainLabel = new Label("Play again?");
        Label quitLabel = new Label("..or exit.");

        Font font = new Font(24);
        playAgainLabel.setFont(font);
        playAgainLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(playAgainLabel, HPos.CENTER);


        quitLabel.setFont(font);
        quitLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(quitLabel, HPos.CENTER);


        // Buttons
        Button playAgainBtn = new Button("To Main Menu");
        Button quitBtn = new Button("Quit");

        customizeButton(playAgainBtn, 200, 50);
        customizeButton(quitBtn, 200, 50);
        GridPane.setHalignment(playAgainBtn, HPos.CENTER);
        GridPane.setHalignment(quitBtn, HPos.CENTER);

        // Make the buttons do things

        playAgainBtn.setOnMouseClicked(event -> {
            try {
                this.model = new AdventureGame("TinyGame");
                createMainMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        quitBtn.setOnMouseClicked(event -> Platform.exit());

        // Add them
        inner.add(playAgainLabel, 0, 0);
        inner.add(quitLabel, 1, 0);

        inner.add(playAgainBtn, 0, 1);
        inner.add(quitBtn, 1, 1);

        GridPane.setHalignment(inner, HPos.CENTER);
        inner.setAlignment(Pos.CENTER);
        gameOverPane.add(inner, 0, 1);



        // Display this scene
        var scene = new Scene(gameOverPane, 1000, 800);
        this.stage.setScene(scene);
    }



    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }





    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the
     * current room.
     */
    private void showCommands() {
        // Set the text to the commands string.
        updateScene();
    }

    /**
     * Draw the current room. Should only be called once
     * on a room's initial loading.
     *
     * @param hardReDraw: Will it also redraw all objects?
     */
    private void drawRoom(boolean hardReDraw) {
        // Player information
        Player player = model.getPlayer();
        CurrentRoom room = model.getRoom();
        // Room information
        String[][] objectMap = room.getObjectMap();
        String[][] tileMap = room.getTileMap();

        Tile.ObjectSet[][] mem = new Tile.ObjectSet[12][15];

        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                for (Node child : gameGrid.getChildren()) {
                    if (GridPane.getColumnIndex(child) == j && GridPane.getRowIndex(child) == i) {
                        mem[i][j] =  ((Tile) child).object;
                        if(((Tile) child).object == Tile.ObjectSet.SLIME) System.out.println(i + " " +  j + " " + ((Tile) child).object);
                    }
                }
            }
        }


        gameGrid.getChildren().clear(); // Clear the previous tiles

        // Find the player's default position
        if (!accessibilityToggle) {
            for (int i = 0; i < objectMap.length; i++) {
                for (int j = 0; j < objectMap[0].length; j++) {
                    if (objectMap[i][j] == null) continue;

                    if (objectMap[i][j].contains("PLAYER")) {
                        player.setX(j);
                        player.setY(i);
                        player.setPrevY(i);
                        player.setPrevX(j);

                        String direction = objectMap[i][j].split("_")[1]; // PLAYER_XXXX

                        // Find out which way they're facing
                        switch (direction) {
                            case "NORTH" -> player.setFacing(Player.Direction.NORTH);
                            case "SOUTH" -> player.setFacing(Player.Direction.SOUTH);
                            case "EAST" -> player.setFacing(Player.Direction.EAST);
                            case "WEST" -> player.setFacing(Player.Direction.WEST);
                            default -> {
                            }
                        }
                        break;
                    }
                }
            }
        }


        // Putting the tiles objects in the room
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                // The tile image at this x,y position
                String tile_s = tileMap[i][j];
                if (!tile_s.equals("NULL") && darkMode){
                    tile_s += "_DARK";
                }
                Tile.TileSet tileSet = Tile.TileSet.valueOf(tile_s);

                // Object (if any) at this x,y position

                Tile.ObjectSet objectSet = objectMap[i][j] != null ?
                        Tile.ObjectSet.valueOf(objectMap[i][j]) :
                        Tile.ObjectSet.NONE;

                // If it's a soft reset, don't redraw any moving objects.

                // certain tiles are solid, the player can't walk over them.
                boolean isSolid = tileSet == Tile.TileSet.STONE || tileSet == Tile.TileSet.LOG || tileSet == Tile.TileSet.BLOCK || tileSet == Tile.TileSet.STONE_DARK|| tileSet == Tile.TileSet.LOG_DARK || tileSet == Tile.TileSet.BLOCK_DARK;

                // Put it
                Tile tile = new Tile(isSolid, tileSet, objectSet);


                if(!hardReDraw && this.model.getRoom().getObjectAtTile(j, i) != null) {
                    if (List.of(Tile.ObjectSet.PLAYER_EAST, Tile.ObjectSet.PLAYER_WEST, Tile.ObjectSet.PLAYER_NORTH, Tile.ObjectSet.PLAYER_SOUTH)
                            .contains(objectSet)) {
                        objectSet = Tile.ObjectSet.NONE; // reset it so it's not a duplicate player
                    } else  {
                        objectSet = mem[i][j] == null ? Tile.ObjectSet.NONE : mem[i][j];
                    }

                    tile = new Tile(isSolid, tileSet, objectSet);
                }

                // Special case for doors
                if(objectSet == Tile.ObjectSet.DOOR) {
                    tile = new Door(isSolid, tileSet, model.getRoom().getDoorDestinationAt(j, i).neededItem);
                }

                this.gameGrid.add(tile, j, i); //  add(Node, columnIndex, rowIndex)
            }
        }
    }

    /**
     * Update the visuals in the room after the player
     * has moved some direction.
     *
     *
     */
    public void updateScene() {
        // Player info
        Player player = model.getPlayer();
        Player.Direction facing = player.getFacing();
        Tile.ObjectSet playerSprite;

        // Get which way the new sprite will be facing
        switch (facing) {
            case NORTH -> playerSprite = Tile.ObjectSet.PLAYER_NORTH;
            case SOUTH -> playerSprite = Tile.ObjectSet.PLAYER_SOUTH;
            case EAST -> playerSprite = Tile.ObjectSet.PLAYER_EAST;
            case WEST -> playerSprite = Tile.ObjectSet.PLAYER_WEST;
            default -> {
                return; // something went wrong.
            }
        }

        // Paint over the previous tile where the player WAS with nothing
        updateTile(player.getPrevX(), player.getPrevY(), Tile.ObjectSet.NONE);

        // Put the new player where they should be!
        updateTile(player.getX(), player.getY(), playerSprite);

        stage.sizeToScene();
    }

    /**
     * Update the object on a tile somewhere in the game world
     *
     * @param x x coord for the tile
     * @param y y coord for the tile
     * @param object Which new object that will go here
     */
    private void updateTile(int x, int y, Tile.ObjectSet object) {
        for (Node child : gameGrid.getChildren()) {
            if (GridPane.getColumnIndex(child) == x && GridPane.getRowIndex(child) == y && child instanceof Tile) {
                ((Tile) child).setObject(object);  // update object
                break;
            }
        }
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {

        // Clear the objects.
        objectsInRoom.getChildren().clear();

        // Iterate over all the items on the floor and forge buttons for all of them.
        for(AdventureObject obj: this.model.getPlayer().getCurrentRoom().objectsInRoom) {

            Button btn = makeObjectButton(obj);

            // When the item is clicked, move it from the floor to the inventory
            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    moveItemFromRoomToInventory(btn, obj);
                }
            });

            objectsInRoom.getChildren().add(btn);
        }

        objectsInInventory.getChildren().clear();

        // Iterate over all the items in the inventory and forge buttons for all of them.
        for(AdventureObject obj: this.model.getPlayer().inventory) {

            Button btn = makeObjectButton(obj);

            // When the item is clicked, move it from the inventory to the room.
            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    moveItemFromInventoryToRoom(btn, obj);
                }
            });

            objectsInInventory.getChildren().add(btn);
        }

        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);
    }

    /**
     * makeObjectButton
     * __________________________
     *
     * This method makes a button to be used for the inventory and room Scrollbars.
     *
     * The label and image depend on the AdventureObject
     *
     * @param obj The adventure object for this button
     * @return A stylized and customized item Button.
     */
    public Button makeObjectButton(AdventureObject obj) {
        Button btn = new Button();

        ImageView imgView = new ImageView();
        Image img = new Image(this.model.getDirectoryName() + "/objectImages/" + obj.getName() + ".jpg");
        imgView.setImage(img);

        imgView.setFitWidth(100);
        imgView.setPreserveRatio(true);

        Text label = new Text(obj.getName());
        Font font = new Font(16);
        label.setFill(Color.BLUE);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(font);

        VBox buttonInsides = new VBox(imgView, label);
        buttonInsides.setAlignment(Pos.CENTER);

        btn.setGraphic(new VBox(buttonInsides));
        btn.setAccessibleText(obj.getDescription());

        btn.setPrefWidth(100);
        btn.setAccessibleText(obj.getDescription());

        return btn;
    }

    /**
     * moveItemFromRoomToInventory
     * __________________________
     *
     * Removes this item from the current room and adds it to the player's inventory.
     *
     * @param btn A reference to the button to be removed
     * @param obj The accompanying adventure object.
     */
    public void moveItemFromRoomToInventory(Button btn, AdventureObject obj) {
        objectsInRoom.getChildren().remove(btn);
        objectsInInventory.getChildren().add(btn);

        this.model.getPlayer().getCurrentRoom().objectsInRoom.remove(obj);
        this.model.getPlayer().inventory.add(obj);

        updateItems();
    }

    /**
     * moveItemFromInventoryToRoom
     * __________________________
     *
     * Removes this item from the player's inventory and drops it in the current room.
     *
     * @param btn A reference to the button to be removed
     * @param obj The accompanying adventure object.
     */
    public void moveItemFromInventoryToRoom(Button btn, AdventureObject obj) {
        objectsInRoom.getChildren().add(btn);
        objectsInInventory.getChildren().remove(btn);

        this.model.getPlayer().getCurrentRoom().objectsInRoom.add(obj);
        this.model.getPlayer().inventory.remove(obj);

        updateItems();
    }


    /**
     * readFile
     * __________________________
     * <p>
     * Read the help.txt file and return it in a string format
     */
    private String getHelpText(){
        File fr = new File("Games" + File.separator + "TinyGame" + File.separator + "help.txt");
        Scanner reader = null;
        try {
            reader = new Scanner(fr);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = reader.nextLine();
        StringBuilder out_str = new StringBuilder();
        while (reader.hasNextLine()){
            out_str.append(line).append("\n");
            line = reader.nextLine();
        }
        return out_str.toString();
    }


    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        this.helpToggle = !this.helpToggle;

        if(this.helpToggle) {
            // Anonymous arrow function, reminds me of javaSCRIPT............
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);

            // Styling the text and setting it
            Text text = new Text();
            Font font = new Font(16);
            text.setFont(font);
            text.setFill(Color.WHITE);
            text.setText(model.getInstructions());

            text.setWrappingWidth(650); // Width of center column
            text.setTextAlignment(TextAlignment.JUSTIFY);

            // Place the text in (1, 1) of the gridPane
            GridPane.setRowIndex(text, 1);
            GridPane.setColumnIndex(text, 1);
            gridPane.getChildren().add(text);

        } else {
            // Remove the help text
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);

            // Put the images and scene text back.
            updateScene();
            updateItems();
        }
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;

    }

    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }

    /**
     * Draw the health-bar.
     * Displays 1 heart for each HP that the player has.
     *
     */
    public void drawHealthBar() {
        gridPane.getChildren().remove(healthBox);
        healthBox.getChildren().clear();


        Label healthLabel = new Label(" HP: ");
        healthLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        healthBox.getChildren().add(healthLabel);
        for(int i = 0; i < model.player.getHp(); i++) {
            ImageView heart = new ImageView(new Image("Games" + File.separator + "TinyGame" + File.separator + "objectImages" + File.separator + "heart.png"));
            heart.setFitWidth(40);
            heart.setFitHeight(40);
            heart.setPreserveRatio(true);

            healthBox.getChildren().add(heart);
        }

        healthBox.setAlignment(Pos.CENTER_LEFT);

        gridPane.add(healthBox, 0, 0);
    }

    /**
     * This method is called when the player's states are updated
     * and the UI needs to be updated.
     *
     * (Observer Pattern)
     */
    public void updatePlayer() {
        drawHealthBar();
    }
}
