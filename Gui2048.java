/** Gui2048.java */
/** PSA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
/* Name: Victory Perkins
 * Login: cs8bwahb
 * Date: March 3, 2016
 * File: Gui2048.java
 * Sources of Help: Textbook, Algorithms from stackoverflow.com, many tutors
 *
 * This program creates a Graphical User Interface for use in the 2048 Game. 
 */
////////CLASS HEADER\\\\\\\
/* Name: Board
 * Purpose: To create a board object for use in the 2048 Game. 
 * Number of data fields: 28
 * Type of data fields: int, Color, Text, Rectangle, ArrayList<Text>,
 * ArrayList<Rectangle>, String, Board, GridPane
 * Number of Methods: 17
 *
 */
public class Gui2048 extends Application
{
   private String outputBoard; // The filename for where to save the Board
   private Board board; // The 2048 Game Board

   private static final int TILE_WIDTH = 106;

   private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
   private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
   //(128, 256, 512)
   private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
   //(1024, 2048, Higher)

   // Fill colors for each of the Tile values
   private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
   private static final Color COLOR_2 = Color.rgb(238, 228, 218);
   private static final Color COLOR_4 = Color.rgb(237, 224, 200);
   private static final Color COLOR_8 = Color.rgb(242, 177, 121);
   private static final Color COLOR_16 = Color.rgb(245, 149, 99);
   private static final Color COLOR_32 = Color.rgb(246, 124, 95);
   private static final Color COLOR_64 = Color.rgb(246, 94, 59);
   private static final Color COLOR_128 = Color.rgb(237, 207, 114);
   private static final Color COLOR_256 = Color.rgb(237, 204, 97);
   private static final Color COLOR_512 = Color.rgb(237, 200, 80);
   private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
   private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
   private static final Color COLOR_OTHER = Color.BLACK;
   private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

   private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
   // For tiles >= 8

   private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
   // For tiles < 8

   private GridPane pane;

   /** Add your own Instance Variables here */

   private ArrayList<Rectangle> tile = new ArrayList<Rectangle>();
   private ArrayList<Text> number = new ArrayList<Text>();
   private static final int TEXT_SIZE_HIGHEST = 70; // High value tiles  
   private static Text scoreString; 
   private int score; 



   @Override
      public void start(Stage primaryStage)
      {
         // Process Arguments and Initialize the Game Board
         processArgs(getParameters().getRaw().toArray(new String[0]));

         // Create the pane that will hold all of the visual objects
         pane = new GridPane();
         pane.setAlignment(Pos.CENTER);
         pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
         pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
         // Set the spacing between the Tiles
         pane.setHgap(15); 
         pane.setVgap(15);

         /** Add your Code for the GUI Here */
         //set scene
         Scene scene = new Scene(pane);
         scene.setOnKeyPressed(new myKeyHandler());
         primaryStage.setMinWidth((int)600);
         primaryStage.setMinHeight((int)700);
         primaryStage.setTitle("Victory's 2048 GUI");
         primaryStage.setScene(scene);
         primaryStage.show();
 
         //Set text for game header
         Text gameName = new Text();
         gameName.setText("2048");
         gameName.setFont(Font.font("Calibre", FontWeight.BOLD, TEXT_SIZE_HIGHEST));
         gameName.setFill(Color.BLACK);
         pane.add(gameName, 1,0, 2, 1);
         GridPane.setHalignment(gameName, HPos.CENTER);
         //set score for game header
         score = board.getScore();
         scoreString = new Text();
         scoreString.setText("Score: " + Integer.toString(score));
         scoreString.setFont(Font.font("Calibre", FontWeight.BOLD, TEXT_SIZE_HIGH));
         scoreString.setFill(Color.WHITE);
         pane.add(scoreString, 3,0, 2, 1);
         GridPane.setHalignment(scoreString, HPos.CENTER);

         //Loop to set up starting game board
         int gridSize = board.getGrid().length;
         int tileAmount = 0; 

         for(int r=0; r<gridSize; r++){
            for(int c=0; c<gridSize; c++){
               int[][] grid = board.getGrid();
               tileAmount = grid[r][c];

               //build ArrayList of Rectangles for board tiles
               Rectangle boardTile = new Rectangle();
               boardTile.setWidth(TILE_WIDTH);
               boardTile.setHeight(TILE_WIDTH);
               if(tileAmount == 2){
                  boardTile.setFill(COLOR_2);
               }
               else if(tileAmount == 4){
                  boardTile.setFill(COLOR_4);
               }
               else if(tileAmount == 0){
                  boardTile.setFill(COLOR_EMPTY);
               }
               tile.add(boardTile);

               //build ArrayList of Text fields for board tile numbers
               Text tileNumber = new Text();
               if(tileAmount != 0){
               tileNumber.setText(Integer.toString(tileAmount));}
               tileNumber.setFont(Font.font("Calibre", FontWeight.BOLD, TEXT_SIZE_LOW));
               tileNumber.setFill(COLOR_VALUE_DARK);
               
               number.add(tileNumber);
               
               //Add tiles and numbers to GUI
               pane.add(boardTile, c+1, r+1);
               pane.add(tileNumber, c+1, r+1);
               GridPane.setHalignment(tileNumber, HPos.CENTER);
            }
         }
      }

   /** Add your own Instance Methods Here */


   /* Method Name: changeBoard 
    * Purpose: To update the GUI tiles and numbers to reflect
    * the progression of the game
    * Parameters: Takes no parameters
    * Returns: void
    */
   public void changeBoard(){
      int gridSize = board.getGrid().length;
      int tileAmount = 0; 
      int i = 0;
      //update game score
      score = board.getScore();
      scoreString.setText("Score: " + Integer.toString(score));
      
      //loop through board tiles get new values and positions
      for(int r=0; r<gridSize; r++){
         for(int c=0; c<gridSize; c++){
            int[][] grid = board.getGrid();
            tileAmount = grid[r][c];
            
            //change tile colors based on number amount
            Rectangle boardTile = tile.get(i);
            switch(tileAmount){
               case 0: boardTile.setFill(COLOR_EMPTY); break; 
               case 2: boardTile.setFill(COLOR_2); break;
               case 4: boardTile.setFill(COLOR_4); break;
               case 8: boardTile.setFill(COLOR_8); break;
               case 16: boardTile.setFill(COLOR_16); break;
               case 32: boardTile.setFill(COLOR_32); break;
               case 64: boardTile.setFill(COLOR_64); break;
               case 128: boardTile.setFill(COLOR_128); break;
               case 256: boardTile.setFill(COLOR_256); break;
               case 512: boardTile.setFill(COLOR_512); break;
               case 1024: boardTile.setFill(COLOR_1024); break;
               case 2048: boardTile.setFill(COLOR_2048); break;
               default: boardTile.setFill(COLOR_OTHER); break;
            }
            
            //change tile numbers, unless tile number is zero
            Text tileNumber = number.get(i);
            if(tileAmount == 0){
               tileNumber.setText(null);
            }
            else{tileNumber.setText(Integer.toString(tileAmount));}
            
            i++;
         }
      }
      //Check if game is over. IF it is, add game over screen
      if(board.isGameOver()){
         Rectangle over = new Rectangle();
         over.setWidth(pane.getWidth());
         over.setHeight(pane.getHeight());
         over.setFill(COLOR_GAME_OVER);

         Text gameOver = new Text();
         gameOver.setText("GAME OVER!");
         gameOver.setFont(Font.font("Calibre", FontWeight.BOLD, TEXT_SIZE_HIGHEST));
         gameOver.setFill(Color.BLACK);

         pane.add(over, 0,0, gridSize+1, gridSize+1);
         pane.add(gameOver, 0,0, gridSize+1, gridSize+1);
         GridPane.setHalignment(over, HPos.CENTER);
         GridPane.setValignment(over, VPos.CENTER);
         GridPane.setHalignment(gameOver, HPos.CENTER);
         GridPane.setValignment(gameOver, VPos.CENTER);
      }
   }

////////CLASS HEADER\\\\\\\
/* Name: myKeyHandler
 * Purpose: Creates an event handler for when the arrow keys, or S or R
 * are pressed
 */
   private class myKeyHandler implements EventHandler<KeyEvent>{

      @Override
         public void handle(KeyEvent e){
            //Up arrow key moves UP
            if(e.getCode() == KeyCode.UP){
               boolean move = board.canMove(Direction.UP);
               if(move){
                  board.move(Direction.UP);
                  board.addRandomTile();
                  changeBoard();
                  System.out.println("Moving UP");
               }
               else{return;}
            }//Down Arrow key moves DOWN
            else if(e.getCode() == KeyCode.DOWN){
               boolean move = board.canMove(Direction.DOWN);
               if(move){
                  board.move(Direction.DOWN);
                  board.addRandomTile();
                  changeBoard();
                  System.out.println("Moving Down");
               }
               else{return;}
            }//Right arrow key moves RIGHT
            else if(e.getCode() == KeyCode.RIGHT){
               boolean move = board.canMove(Direction.RIGHT);
               if(move){
                  board.move(Direction.RIGHT);
                  board.addRandomTile();
                  changeBoard();
                  System.out.println("Moving Right");
               }
               else{return;}
            }//Left Arrow key moves LEFT
            else if(e.getCode() == KeyCode.LEFT){
               boolean move = board.canMove(Direction.LEFT);
               if(move){
                  board.move(Direction.LEFT);
                  board.addRandomTile();
                  changeBoard();
                  System.out.println("Moving Left");
               }
               else{return;}
            }//pressing "S" key saves board to file
            else if(e.getCode() == KeyCode.S){
               try{
                  board.saveBoard("Game.txt");
                  System.out.println("Saving board to fileName Game.txt");
               }catch (IOException m ){
                  System.out.println("saveBoard threw an Exception");
               }
            }//pressing "R" key rotates board clockwise
            else if(e.getCode() == KeyCode.R){
               if(!board.isGameOver()){
               board.rotate(true);
               changeBoard();
               System.out.println("Rotating board clockwise");
               }
            }
         }
   }


   /** DO NOT EDIT BELOW */

   // The method used to process the command line arguments
   private void processArgs(String[] args)
   {
      String inputBoard = null;   // The filename for where to load the Board
      int boardSize = 0;          // The Size of the Board

      // Arguments must come in pairs
      if((args.length % 2) != 0)
      {
         printUsage();
         System.exit(-1);
      }

      // Process all the arguments 
      for(int i = 0; i < args.length; i += 2)
      {
         if(args[i].equals("-i"))
         {   // We are processing the argument that specifies
            // the input file to be used to set the board
            inputBoard = args[i + 1];
         }
         else if(args[i].equals("-o"))
         {   // We are processing the argument that specifies
            // the output file to be used to save the board
            outputBoard = args[i + 1];
         }
         else if(args[i].equals("-s"))
         {   // We are processing the argument that specifies
            // the size of the Board
            boardSize = Integer.parseInt(args[i + 1]);
         }
         else
         {   // Incorrect Argument 
            printUsage();
            System.exit(-1);
         }
      }

      // Set the default output file if none specified
      if(outputBoard == null)
         outputBoard = "2048.board";
      // Set the default Board size if none specified or less than 2
      if(boardSize < 2)
         boardSize = 4;

      // Initialize the Game Board
      try{
         if(inputBoard != null)
            board = new Board(inputBoard, new Random());
         else
            board = new Board(boardSize, new Random());
      }
      catch (Exception e)
      {
         System.out.println(e.getClass().getName() + 
               " was thrown while creating a " +
               "Board from file " + inputBoard);
         System.out.println("Either your Board(String, Random) " +
               "Constructor is broken or the file isn't " +
               "formated correctly");
         System.exit(-1);
      }
   }

   // Print the Usage Message 
   private static void printUsage()
   {
      System.out.println("Gui2048");
      System.out.println("Usage:  Gui2048 [-i|o file ...]");
      System.out.println();
      System.out.println("  Command line arguments come in pairs of the "+ 
            "form: <command> <argument>");
      System.out.println();
      System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
            "should be loaded");
      System.out.println();
      System.out.println("  -o [file]  -> Specifies a file that should be " + 
            "used to save the 2048 board");
      System.out.println("                If none specified then the " + 
            "default \"2048.board\" file will be used");  
      System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
            "board if an input file hasn't been"); 
      System.out.println("                specified.  If both -s and -i" + 
            "are used, then the size of the board"); 
      System.out.println("                will be determined by the input" +
            " file. The default size is 4.");
   }
}


