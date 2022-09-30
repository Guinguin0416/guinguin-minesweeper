// Name: Jiayu Pu
// USC NetID: jiayupu
// CS 455 PA3
// Spring 2022

import java.util.*;
/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
/**
      Representation invariant:
      - number of mines should  be non-negative
   */

public class MineField {
   private boolean [][] mineField; // 2D array to represent minefield
   private int numMines; // number of mines
   // <put instance variables here>
   
   
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will corresponds to the number of 'true' values in mineData.
      @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
    */
   public MineField(boolean[][] mineData) {
      numMines = 0;
      int row = mineData.length;
      int col = mineData[0].length;
      mineField = new boolean [row][col];
      for (int i = 0; i < row; i ++) {
         for (int j = 0; j < col; j ++) {
            mineField[i][j] = mineData[i][j];
            if (mineData[i][j] == true) {
               numMines ++;
            }
         }
      }
   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      this.numMines = numMines;
      mineField = new boolean [numRows][numCols];
      resetEmpty();

      
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {
      resetEmpty(); // remove any current mines on the minefield
      Random rand = new Random();
      int rowValue = 0;
      int colValue = 0;
      int countMines = numMines();
      while (countMines > 0) { 
         rowValue = rand.nextInt(mineField.length);
         colValue = rand.nextInt(mineField[0].length);
         if (rowValue != row && colValue != col && mineField[rowValue][colValue] == false ) {
            mineField[rowValue][colValue] = true; // put numMines() mines in random locations on the minefield, avoid [row][col] location
            countMines --;
         }
         
      }
   }
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in 
         at the beginning of a game.
    */
   public void resetEmpty() {
      for (int i = 0; i < mineField.length; i ++) {
         for (int j = 0; j < mineField[0].length; j ++) {
            mineField[i][j] = false;
         }
      }
      
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      int adjMines = 0;
      // Horizontal and Vertical
      // up
      if (inRange(row - 1, col) && hasMine(row - 1, col)) {adjMines ++;}
      // down
      if (inRange(row + 1, col) && hasMine(row + 1, col)) {adjMines ++;}
      // left
      if (inRange(row, col - 1) && hasMine(row, col - 1)) {adjMines ++;}
      // right 
      if (inRange(row, col + 1) && hasMine(row, col + 1)) {adjMines ++;}
      // Diagonal
      // up-left
      if (inRange(row - 1, col - 1) && hasMine(row - 1, col - 1)) {adjMines ++;}
      // up-right
      if (inRange(row + 1, col + 1) && hasMine(row + 1, col + 1)) {adjMines ++;}
      // down-left
      if (inRange(row + 1, col - 1) && hasMine(row + 1, col - 1)) {adjMines ++;}
      // down-right
      if (inRange(row - 1, col + 1) && hasMine(row - 1, col + 1)) {adjMines ++;}
      return adjMines;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      if (row >= 0 && row < mineField.length && col >= 0 && col < mineField[0].length) {
         return true;
      }
      return false;       
   }
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return mineField.length;       // DUMMY CODE so skeleton compiles
   }
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return mineField[0].length;       // DUMMY CODE so skeleton compiles
   }
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      return mineField[row][col];       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
      return numMines;       // DUMMY CODE so skeleton compiles
   }

   
   // <put private methods here>
   
         
}

