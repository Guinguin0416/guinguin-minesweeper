// Name: Jiayu Pu 
// USC NetID: jiayupu
// CS 455 PA3
// Spring 2022


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield). Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // The following are the covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // The following are the uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
   // ----------------------------------------------------------   
   /**
        Representation invariant:
        - number of how many guesses should be non-negative
        - MineField object should not be null
     */
  
   // <put instance variables here>
   private MineField visibleMineField; // MineField object to represent visible minefield
   private int[][] mineState; // 2D array to represent squares' states
   private int mineGuess; // number of how many guesses
   private boolean isGameOver; // a flag to show whether the game is over or not
   

   

   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the mines covered up, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
      visibleMineField = mineField;
      mineState = new int[visibleMineField.numRows()][visibleMineField.numCols()];
      resetGameDisplay();
   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
      //  The initial state will have all the mines covered up, no mines guessed, and the game not over.
      // all the mines covered up
      for (int i = 0; i < mineState.length; i ++) {
         for (int j = 0; j < mineState[0].length; j ++) {
            mineState[i][j] = COVERED;
         }
      }
      // no mines guessed
      mineGuess = 0;
      // the game not over
      isGameOver = false;
      
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return visibleMineField;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return mineState[row][col];       // DUMMY CODE so skeleton compiles
   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value can
      be negative, if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      return visibleMineField.numMines() - mineGuess;       // DUMMY CODE so skeleton compiles

   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      if (mineState[row][col] == COVERED) {
         mineState[row][col] = MINE_GUESS;
         mineGuess ++;
      } else if (mineState[row][col] == MINE_GUESS) {
         mineState[row][col] = QUESTION;
         mineGuess --;
      } else if (mineState[row][col] == QUESTION) {
         mineState[row][col] = COVERED;
      }
      
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
      if (visibleMineField.hasMine(row, col) == true) { // return false iff the player uncover a mine
         mineState[row][col] = EXPLODED_MINE;
         isGameOver = true;
         return false;
      } else { 
         floodFill(row, col);
         return true;
         
      }
            // DUMMY CODE so skeleton compiles
   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game over
    */
   public boolean isGameOver() {
      int totalSquares = visibleMineField.numRows() * visibleMineField.numCols();
      int nonmineLocs = totalSquares - visibleMineField.numMines();
      isGameOver = false;
      // situation 1: explode a mine
      for (int i = 0; i < mineState.length; i ++) {
         for (int j = 0; j < mineState[0].length; j ++) {
            if (mineState[i][j] == EXPLODED_MINE) { // game over when a player opens a mine location 
               loseGame();// lose game display
               return !isGameOver;
            } else if (mineState[i][j] >= 0 && mineState[i][j] <= 8) { // uncover a non-mine location
               nonmineLocs --;
            }
         }
      }
      // situation 2: open all non-mine locations
      if (nonmineLocs == 0) {
         winGame();// win game display
         return !isGameOver;
      }
      return isGameOver;       // DUMMY CODE so skeleton compiles
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) { 
      if (mineState[row][col] >= 0) {
         return true;
      }
      return false;       // DUMMY CODE so skeleton compiles
   }
   
 
   // <put private methods here>
   /**
    * Floodfill algorithm to recursively open the empty region
    @param row of the square
    @param col of the square
    PRE: getMineField().inRange(row, col)
    */
   private void floodFill(int row, int col) {
      if (visibleMineField.inRange(row, col) == false || isUncovered(row, col)) { // if the square has been uncovered, no action needed
         return;
      }
      else if (mineState[row][col] != MINE_GUESS && visibleMineField.numAdjacentMines(row, col) > 0) { // if the square has some adjacent mines, update the squre accordingly
         mineState[row][col] = visibleMineField.numAdjacentMines(row, col);
         return;
      }
      else if (mineState[row][col] != MINE_GUESS && visibleMineField.numAdjacentMines(row, col) == 0) {
         mineState[row][col] = visibleMineField.numAdjacentMines(row, col);
         // recursively open empty regions
         // Horizontal and Vertical
         // up
         floodFill(row - 1, col);
         // down 
         floodFill(row + 1, col);
         // left
         floodFill(row, col - 1);
         // right
         floodFill(row, col + 1);
         // Diagonal
         // up-left
         floodFill(row - 1, col - 1);
         // up-right
         floodFill(row + 1, col + 1);
         // down-left
         floodFill(row + 1, col - 1);
         // down-right
         floodFill(row - 1, col + 1);

      }
      
   }


   /**
    * Set the losing game display: if there is no mine but the player make a guess, set the state to INCORRECT_GUESS;
      if there is a mine but the player didn't make a guess, set the state to MINE.       
    */
   private void loseGame() {
      for (int i = 0; i < mineState.length; i ++) {
         for (int j = 0; j < mineState[0].length; j ++) {
            if (mineState[i][j] == MINE_GUESS && visibleMineField.hasMine(i, j) == false) { // any previously made incorrect guess
               mineState[i][j] = INCORRECT_GUESS;
            } else if ((mineState[i][j] == COVERED || mineState[i][j] == QUESTION) && visibleMineField.hasMine(i, j) == true) { // there is a mine, but the player didn't make a guess
               mineState[i][j] = MINE;
            }
         }
      }
   }
   /**
    * Set the winning game display: when a player successfullly opens all of the non-mine locations, the field display
      changes to show where the other mines are (it shows them as guesses).
    */
   private void winGame() {
      for (int i = 0; i < mineState.length; i ++) {
         for (int j = 0; j < mineState[0].length; j ++) {
            if (visibleMineField.hasMine(i, j) == true && mineState[i][j] != MINE_GUESS && mineState[i][j] != EXPLODED_MINE) { // the field display changes to show where the other mines are, show them ias guesses
               mineState [i][j] = MINE_GUESS;
            }

         }
      }
      

   }
   
}
