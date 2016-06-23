//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    2/4/16                                                 //
//------------------------------------------------------------------//
/* Name: Victory Perkins
 * Login: cs8bwahb
 * Date: February 4, 2016
 * File: Board.java
 * Sources of Help: Textbook, Algorithms from stackoverflow.com, many tutors
 *
 * This program creates a board object for use in the 2048 Game. See sample
 * board below. 
 */
////////CLASS HEADER\\\\\\\
 /*Name: Board
 * Purpose: To create a board object for use in the 2048 Game. 
 * Number of data fields: 6
 * Type of data fields: int, int [][]
 * Number of Constructors: 2
 * Number of Methods: 17
 *
 */
/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

public class Board {
   public final int NUM_START_TILES = 2;
   public final int TWO_PROBABILITY = 90;
   public final int GRID_SIZE;


   private final Random random;
   private int[][] grid;
   private int score;


   // Constructor to creat a fresh board with random tiles
   // using addRandomTile() method below
   // Parameters: Takees 2 parameters
   //    int boardSize: This parameter initializes public GRID_SIZE instance variable
   //    Random random: This parameter initializes private random instance
   //    variable
   public Board(int boardSize, Random random) {
      this.random = random; 
      GRID_SIZE = boardSize; 
      this.grid = new int[GRID_SIZE][GRID_SIZE];
      this.score = 0; 

      for(int i = 0; i < NUM_START_TILES; i++)
      {
         this.addRandomTile();
      }
   }   

   // Constructer to create a board based on input file
   // Parameters: Takes in 2 parameters
   //    String inputBoard: Is the filename of the board to load
   //    Random random: Is the random generator object to initialize 
   //    random data field
   public Board(String inputBoard, Random random) throws IOException {
      this.random = random; 

      File file = new File(inputBoard);
      Scanner input = new Scanner(file);

      GRID_SIZE = input.nextInt();//filling in grid size
      this.score = input.nextInt();//filling in score

      grid = new int[GRID_SIZE][GRID_SIZE];

      while(input.hasNext())//filling in grid with input from file
      {
         int next;

         for(int r = 0; r < GRID_SIZE; r++)
         {
            for(int c = 0; c < GRID_SIZE; c++)
            {  
               next = input.nextInt();
               grid[r][c] = next; 
            }
         }
      }         
   }

   /*Method Name: saveBoard
    * Purpose: Saves the current board to a file
    * Parameters: 
    *    String outputBoard: This parameter is simply a user specified
    *    filename which the board is saved under.
    * Return: This method returns void
    */
   public void saveBoard(String outputBoard) throws IOException {

      File file = new File(outputBoard);
      PrintWriter output = new PrintWriter(file);

      int score = this.getScore();
      int [][] currentBoard = this.getGrid();

      output.println(this.GRID_SIZE);//printing board size to file
      output.println(score);//printing score to file

      for(int r = 0; r < currentBoard.length; r++) // printing board to file
      {
         for(int c = 0;  c < currentBoard[0].length; c++)
         {
            output.print(currentBoard[r][c]);
            output.print(" ");
         }
         output.println();
      }    

      output.close();
   }

   /*Method Name: addRandomTile
    * Purpose: Adds a random tile (of value 2 or 4) to a 
    * random empty space on the board
    * Paramaters: 
    *     This method takes no parameters
    * Return: This method returns void
    */
   public void addRandomTile() {

      int location;
      int value;

      int count = 0;

      //loop through board to find empty spaces
      for(int r = 0; r < this.grid.length; r++)
      {
         for(int c = 0; c < this.grid[0].length; c++)
         {
            if(this.grid[r][c] == 0)
            {
               count++;
            }        
         }
      }
      if(count == 0)//if no empty spaces, exit method
      {return;}

      location = random.nextInt(count) + 1;//get random location for next tile
      value = random.nextInt(100);

      count = 0;
      //loop through board to find random location and assign int 2 or 4 randomly
      for(int r = 0; r < this.grid.length; r++)
      {
         for(int c = 0; c < this.grid[0].length; c++)
         {
            if(this.grid[r][c] == 0)
            { 
               count++;
               if(count == location)
               {
                  if(value < TWO_PROBABILITY)//randomly place 2 or 4 in random location 
                  {
                     this.grid[r][c] = 2;
                  }
                  else{this.grid[r][c] = 4;}            
               }
            }
         }
      }
   }
   /*Method name: rotate
    * Purpose: Rotates the board by 90 degrees clockwise or 90 degrees
    * counter-clockwise.
    * Parameters: 
    *       boolean rotateClockwise: if rotateClockwise == true, method rotates
    *       the board clockwise, else rotates the board counter-clockwise
    * Return: this method returns void
    */
   public void rotate(boolean rotateClockwise) {

      // first transpose grid      
      for(int r = 0; r < this.grid.length; r++)
      {
         for(int c = r+1; c < this.grid[0].length; c++)
         { 
            int temp = this.grid[r][c];
            this.grid[r][c] = this.grid[c][r];
            this.grid[c][r] = temp;
         }
      }

      //if rotateClockwise == true, reverse rows
      //to rotate 90 degrees clockwise
      if(rotateClockwise)
      {
         for(int r = 0; r < this.grid.length; r++)
         {
            for(int c = 0; c < this.grid[0].length/2; c++)
            {
               int temp = this.grid[r][c];
               this.grid[r][c] = this.grid[r][this.grid.length-1-c];
               this.grid[r][this.grid.length-1-c] = temp;
            }
         }
      }//if rotateClockwise == false, revers columns
      //to rotate 90 degrees counter clockwise
      else if(!rotateClockwise)
      {
         for(int r = 0; r < this.grid.length/2; r++)
         {
            for(int c = 0; c < this.grid[0].length; c++)
            {
               int temp = this.grid[r][c];
               this.grid[r][c] = this.grid[this.grid[0].length-1-r][c];
               this.grid[this.grid[0].length-1-r][c] = temp;
            }
         }
      }

   }

   //Complete this method ONLY if you want to attempt at getting the extra credit
   //Returns true if the file to be read is in the correct format, else return
   //false
   public static boolean isInputFileCorrectFormat(String inputFile) {
      //The try and catch block are used to handle any exceptions
      //Do not worry about the details, just write all your conditions inside the
      //try block
      try {
         //write your code to check for all conditions and return true if it satisfies
         //all conditions else return false
         return true;
      } catch (Exception e) {
         return false;
      }
   }
   /* Method Name: move
    * Purpose: Performs a move Operation
    * Parameters: takes in one Direction enum parameter
    *    Uses this parameter to execute a move in the 
    *    specified direction
    * Returns: Returns a boolean. True if move was executed
    * properly. False otherwise
    */
   public boolean move(Direction direction) {
      boolean move = false; 
      if(direction == Direction.RIGHT)
      {move =  this.moveRight();}
      if(direction == Direction.UP)
      {move = this.moveUp();}
      if(direction == Direction.DOWN)
      {move = this.moveDown();}
      if(direction == Direction.LEFT)
      {move = this.moveLeft();}
      return move;
   }

   /*Method Name: isGameOver
    * Purpose: Simple method to check if the game is over
    * Parameters: takes no parameters
    * Returns: Returns a boolean
    */
   public boolean isGameOver() {
      boolean gameOver = false;
      if(!this.canMove(Direction.LEFT) && !this.canMove(Direction.RIGHT)
            && !this.canMove(Direction.UP) && !this.canMove(Direction.DOWN))
      {gameOver = true;
      }
      return gameOver;
   }
   /*Method Name: canMove
    * Purpose: calls appropriate methods to determine if 
    * game can move in a given direction
    * Parameters: Takes one parameter
    *       Direction enum - uses this to check if direction is plausible
    * Returns: Boolean: true if move can be executed, false otherwise
    */
   public boolean canMove(Direction direction) {
      boolean canMove = false;  

      switch(direction)
      {
         case LEFT: canMove = canMoveLeft(); break;
         case RIGHT: canMove = canMoveRight(); break;
         case UP: canMove = canMoveUp(); break; 
         case DOWN: canMove = canMoveDown(); break;
         default: canMove = false; break; 
      }

      return canMove;
   }

   // Return the reference to the 2048 Grid
   public int[][] getGrid() {
      return grid;
   }

   // Return the score
   public int getScore() {
      return score;
   }

   @Override
      public String toString() {
         StringBuilder outputString = new StringBuilder();
         outputString.append(String.format("Score: %d\n", score));
         for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
               outputString.append(grid[row][column] == 0 ? "    -" :
                     String.format("%5d", grid[row][column]));

            outputString.append("\n");
         }
         return outputString.toString();
      }

   //Helper method to determine if move to LEFT is valid
   //called in canMove() method above 
   private boolean canMoveLeft()
   {  boolean move = false; 
      for(int r = 0; r < this.grid.length; r++)
      {
         for(int c = 0; c < this.grid[0].length-1; c++)
         {
            Integer spaceOne = new Integer(Integer.valueOf(this.grid[r][c]));
            Integer spaceTwo = new Integer(Integer.valueOf(this.grid[r][c+1]));

            if(spaceOne.equals(spaceTwo) && !spaceOne.equals(0) && !spaceTwo.equals(0) )
            {return true;}
            else if(spaceOne.equals(0) && !spaceTwo.equals(0))
            {return true;}
            else if(spaceTwo.equals(0) && !spaceOne.equals(0))
            { for(int i = c+1; i<this.grid.length; i++)
               { 
                  if(this.grid[r][i] != 0)
                  {return true;}
               }
            }         }
      }
      return move;
   }

   //Helper method to determine if move to RIGHT is valid
   //called in canMove()  method above
   private boolean canMoveRight()
   {
      for(int r = 0; r < this.grid.length; r++)
      {
         for(int c = this.grid[0].length-1; c > 0; c--)
         {
            Integer spaceOne = new Integer(Integer.valueOf(this.grid[r][c]));
            Integer spaceTwo = new Integer(Integer.valueOf(this.grid[r][c-1]));

            if(spaceOne.equals(spaceTwo) && !spaceOne.equals(0) && !spaceTwo.equals(0) )
            {return true;}
            else if(spaceOne.equals(0) && !spaceTwo.equals(0))
            {return true;}
            else if(spaceTwo.equals(0) && !spaceOne.equals(0))
            { for(int i = c-1; i >=0; i--)
               { 
                  if(this.grid[r][i] != 0)
                  {return true;}
               }
            }
         }
      }
      return false;
   }

   //Helper method to determine if move DOWN is valid
   //called in canMove() method above
   private boolean canMoveDown()
   {
      for(int c = 0; c < this.grid[0].length; c++)
      {
         for(int r = this.grid.length-1; r > 0; r--)
         {
            Integer spaceOne = new Integer(Integer.valueOf(this.grid[r][c]));
            Integer spaceTwo = new Integer(Integer.valueOf(this.grid[r-1][c]));

            if(spaceOne.equals(spaceTwo) && !spaceOne.equals(0) && !spaceTwo.equals(0) )
            {return true;}
            else if(spaceOne.equals(0) && !spaceTwo.equals(0))
            {return true;}
            else if(spaceTwo.equals(0) && !spaceOne.equals(0))
            { for(int i = r-1; i >=0; i--)
               { 
                  if(this.grid[i][c] != 0)
                  {return true;}
               }
            }
         }
      }

      return false;
   }

   //Helper method to determine if move UP is valid
   //called in canMove() method above
   private boolean canMoveUp()
   {
      for(int c = 0; c < this.grid[0].length; c++)
      {
         for(int r = 0; r < this.grid.length-1; r++)
         {
            Integer spaceOne = new Integer(Integer.valueOf(this.grid[r][c]));
            Integer spaceTwo = new Integer(Integer.valueOf(this.grid[r+1][c]));

            if(spaceOne.equals(spaceTwo) && !spaceOne.equals(0) && !spaceTwo.equals(0) )
            {return true;}
            else if(spaceOne.equals(0) && !spaceTwo.equals(0))
            {return true;}
            else if(spaceTwo.equals(0) && !spaceOne.equals(0))
            { for(int i = r+1; i < this.grid.length-1; i++)
               { 
                  if(this.grid[i][c] != 0)
                  {return true;}
               }
            }
         }
      }
      return false;
   }

   /* Method Name: moveRight
    * Purpose: Executes a move to the RIGHT when called
    * Parameters: takes no parameters
    * Return: Boolean - always returns true if method is called
    * */
   private boolean moveRight() {
      ArrayList<Integer> rows = new ArrayList<Integer>();
      ArrayList<Integer> rows2 = new ArrayList<Integer>();

      Integer spaceOne;
      Integer spaceTwo;
      Integer space;
      Integer zero = Integer.valueOf(0); 

      //loop through rows of board grid
      for(int r = 0; r < this.grid.length; r++) {
         //If space is a non-zero integer, add integer into ArrayList
         //in same order on grid from left to right
         for(int c = 0; c < this.grid[0].length;  c++) { 
            space = Integer.valueOf(this.grid[r][c]);
            if(!space.equals(zero)) { 
               rows.add(space);
               this.grid[r][c] = 0;
            }
         }
         //If only one space with number, just slide that to the right
         if(rows.size() == 1){
            this.grid[r][this.grid[0].length-1] = rows.get(0).intValue();
         }
         //Call addArrayList method to add spaces in ArrayList
         else {
            rows2 = addArrayList(rows);

            //Add vales from ArrayList back into original grid row
            int up = 0; 
            int down = this.grid[0].length-1;

            while(up < rows2.size()) {
               this.grid[r][down] = rows2.get(up).intValue();
               up++;
               down--;
            }
         }
         rows.clear();
         rows2.clear();
      }
      return true;
   }       

   /* Method Name: moveLeft
    * Purpose: Executes a move to the LEFT when called
    * Parameters: takes no parameters
    * Return: Boolean - always returns true if method is called
    * */
   private boolean moveLeft() {
      ArrayList<Integer> rows = new ArrayList<Integer>();
      ArrayList<Integer> rows2 = new ArrayList<Integer>();

      Integer spaceOne;
      Integer spaceTwo;
      Integer space;
      Integer zero = Integer.valueOf(0); 

      //loop through rows of board grid
      for(int r = 0; r < this.grid.length; r++) {
         //If space is a non-zero integer, add integer into ArrayList
         //in same order on grid from left to right
         for(int c = this.grid.length-1; c >= 0 ;  c--) { 
            space = Integer.valueOf(this.grid[r][c]);
            if(!space.equals(zero)) { 
               rows.add(space);
               this.grid[r][c] = 0;
            }
         }
         //If only one space with number, just slide that to the left
         if(rows.size() == 1){
            this.grid[r][0] = rows.get(0).intValue();
         }
         //Calls addArrayList() method to add board spaces in Arraylist
         else {
            rows2 = addArrayList(rows);

            //Add vales from ArrayList back into original grid row
            int up = 0; 
            int down = 0;

            while(up < rows2.size()) {
               this.grid[r][down] = rows2.get(up).intValue();
               up++;
               down++;
            }
         }
         rows.clear();
         rows2.clear();
      }
      return true;
   }       


   /* Method Name: moveDown
    * Purpose: Executes a move Down  when called
    * Parameters: takes no parameters
    * Return: Boolean - always returns true if method is called
    * */
   private boolean moveDown() {
      ArrayList<Integer> rows = new ArrayList<Integer>();
      ArrayList<Integer> rows2 = new ArrayList<Integer>();

      Integer spaceOne;
      Integer spaceTwo;
      Integer space;
      Integer zero = Integer.valueOf(0); 

      //loop through columns of board grid
      for(int c = 0; c < this.grid[0].length; c++) {
         //If space is a non-zero integer, add integer into ArrayList
         //in same order on grid from bottom to top
         for(int r = 0; r < this.grid.length;  r++) { 
            space = Integer.valueOf(this.grid[r][c]);
            if(!space.equals(zero)) { 
               rows.add(space);
               this.grid[r][c] = 0;
            }
         }
         //If only one space with number, just slide that down
         if(rows.size() == 1){
            this.grid[this.grid.length-1][c] = rows.get(0).intValue();
         }
         //Calls addArrayList method to add board spaces in Arraylist
         else{
            rows2 = addArrayList(rows);

            //Add vales from ArrayList back into original grid row
            int up = 0; 
            int down = this.grid.length-1;

            while(up < rows2.size()) {
               this.grid[down][c] = rows2.get(up).intValue();
               up++;
               down--;
            }
         }
         rows.clear();
         rows2.clear();
      }
      return true;
   }        


   /* Method Name: moveUp
    * Purpose: Executes a move UP  when called
    * Parameters: takes no parameters
    * Return: Boolean - always returns true if method is called
    * */
   private boolean moveUp() {
      ArrayList<Integer> rows = new ArrayList<Integer>();
      ArrayList<Integer> rows2 = new ArrayList<Integer>();

      Integer spaceOne;
      Integer spaceTwo;
      Integer space;
      Integer zero = Integer.valueOf(0); 

      //loop through columns of board grid
      for(int c = 0; c < this.grid[0].length; c++) {
         //If space is a non-zero integer, add integer into ArrayList
         //in same order on grid from bottom to top
         for(int r = this.grid.length-1; r >= 0;  r--) { 
            space = Integer.valueOf(this.grid[r][c]);
            if(!space.equals(zero)) { 
               rows.add(space);
               this.grid[r][c] = 0;
            }
         }
         //If only one space with number, just slide that up
         if(rows.size() == 1){
            this.grid[0][c] = rows.get(0).intValue();
         }
         //Calls addArrayList method to add board spaces in Arraylist
         else {
            rows2 = addArrayList(rows);

            //Add vales from ArrayList back into original grid row
            int up = 0; 
            int down = 0;

            while(up < rows2.size()) {
               this.grid[down][c] = rows2.get(up).intValue();
               up++;
               down++;
            }
         }
         rows.clear();
         rows2.clear();
      }
      return true;
   }        


   /* Method Name: addArrayList
    * Purpose: Helper method for Move methods. Will add up spaces in 
    * ArrayList that equal each other 
    * Parameters: Takes in one Parameter
    *       ArrayList<Integer>: adds up the spaces in the arrayList 
    *       that equal eachother
    * Returns: an new arrayList made up of the added spaces from the 
    * passed in arrayList, as well as any other spaces that did
    * not equal another space. Spaces are kept in the same order they
    * were in the original ArrayList
    */
   public ArrayList<Integer> addArrayList(ArrayList<Integer> rows)
   {        ArrayList<Integer> rows2 = new ArrayList<Integer>();
      int i = rows.size()-1;
      Integer spaceOne;
      Integer spaceTwo;
      Integer space;

      while(i > 0) {  
         spaceOne = rows.get(i);
         spaceOne = rows.get(i);
         spaceTwo = rows.get(i-1);

         if(spaceOne.equals(spaceTwo)) {
            space = new Integer(spaceOne.intValue() + spaceTwo.intValue());
            rows2.add(space);
            score += space.intValue();
            i-=2;
         }
         else{ 
            rows2.add(spaceOne);
            i--;
         }
         if(i==0) {
            space = rows.get(0);
            rows2.add(space);
         }
      }
      return rows2;
   }









}
