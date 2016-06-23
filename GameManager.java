//------------------------------------------------------------------//
// GameManager.java                                                 //
//                                                                  //
// Game Manager for 2048                                            //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    1/17/16                                                 //
//------------------------------------------------------------------//
/*Name: Victory Perkins
 * Login: cs8bwahb
 * Date: January 28, 2016
 * File: GameManager.java
 * Sources of help: Textbook, many tutors, algorithms from stackoverflow.com
 *
 * Purpose: To create a game board and then play the 2048 game
 */
import java.util.*;
import java.io.*;

public class GameManager {
   // Instance variables
   private Board board; // The actual 2048 board
   private String outputFileName; // File to save the board to when exiting


   // GameManager Constructor
   // Generate new game
   public GameManager(int boardSize, String outputBoard, Random random) {
      this.board = new Board(boardSize, random);
      this.outputFileName = outputBoard;
   }


   // GameManager Constructor
   // Load a saved game
   public GameManager(String inputBoard, String outputBoard, Random random) throws IOException {
      this.board = new Board(inputBoard, random);
      this.outputFileName = outputBoard;

      System.out.println("Loading Board from " + inputBoard);
   }

   //Name: play
   //Purpose: 
   // Main play loop
   // Takes in input from the user to specify moves to execute
   // valid moves are:
   //      k - Move up
   //      j - Move Down
   //      h - Move Left
   //      l - Move Right
   //      q - Quit and Save Board
   //
   //  If an invalid command is received then print the controls
   //  to remind the user of the valid moves.
   //
   //  Once the player decides to quit or the game is over,
   //  save the game board to a file based on the outputFileName
   //  string that was set in the constructor and then return
   //
   //  If the game is over print "Game Over!" to the terminal
   //  Parameters: This method has no parameters
   //  Return: This method returns void
   public void play() throws IOException {
      Scanner input = new Scanner(System.in);

      this.printControls();//starts game by printing controls
      System.out.println(board.toString());

      Direction dir = Direction.UP; 
      System.out.println("Please enter valid control letter");//prompts user for input   
      String move = input.next();

      //Main loop of the game. Will only stop when q is pressed on keyboard, or 
      //method "isGameOver" returns true
      while(!board.isGameOver())
      {  
         if(move.equals("h") || move.equals("k") || move.equals("j") 
               || move.equals("l"))
         {  
            switch(move)//assigning direction enums to user input move keys
            {
               case "h": dir = Direction.LEFT; break;
               case "k": dir = Direction.UP; break; 
               case "j": dir = Direction.DOWN; break;
               case "l": dir = Direction.RIGHT; break;
            }
            //if move is valid, execute move and reprint board to console
            if(board.canMove(dir))
            {
               board.move(dir);
               board.addRandomTile();
               System.out.print(board.toString());
            }
         }
         else if(move.equals("q"))//if user input is q, game will quit
         {
            board.saveBoard(this.outputFileName);
            System.out.println("Saving game to file: " + this.outputFileName);
            return;
         }
         else
         {  //print valid controls to console every time user input is not 
            //valid move key (h, k, j, l)
            this.printControls();
            System.out.println("Enter valid control letter");
         }
         move = input.next();
      }

      if(board.isGameOver())//Game over when all spaces on board are filled
      {
         System.out.println("Game Over!");
         return;
      }
   }

   // Print the Controls for the Game
   private void printControls() {
      System.out.println("  Controls:");
      System.out.println("    k - Move Up");
      System.out.println("    j - Move Down");
      System.out.println("    h - Move Left");
      System.out.println("    l - Move Right");
      System.out.println("    q - Quit and Save Board");
      System.out.println();
   }
}
