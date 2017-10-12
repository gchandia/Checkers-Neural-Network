package checkers;

import java.util.Observable;

//import checkers.InactivePlayer;
import checkers.Player;
import checkers.Move;

public abstract class AbstractBoardGame extends Observable implements BoardGame {
  static final int red = 0;
  static final int black = 1;
  private boolean eatenFlag = false;
  
  protected int rows;
  protected int cols;
  protected int pieces;
  
  protected Player[][] gameState;
  protected final Player nobody = new InactivePlayer();
  protected Player winner = nobody;
  protected Player[] player;
  protected int turn = red;
  
  protected Move lastMove = new Move(0, 0, nobody);
  
  public AbstractBoardGame(Player playerRed, Player playerBlack)
  {
    player = new Player[2];
    player[red] = playerRed;
    player[black] = playerBlack;
    playerRed.setGame(this);
    playerBlack.setGame(this);
    
    this.init();
    
    gameState = new Player[rows][cols];
    
    playerRed.setPieces(pieces);
    playerBlack.setPieces(pieces);
    
    for (int col = 0; col < cols; col++) {
      for (int row = 0; row < rows; row++) {
        this.set(col, row, nobody);
      }
    }
    
    this.initPlayers();
  }
  
  public int getCols() {
    return cols;
  }
  
  public int getRows() {
    return rows;
  }
  
  public Player getCurrentPlayer() {
    return player[turn];
  }
  
  public Player getWinner() {
    return winner;
  }
  
  public Player getState(int col, int row) {
    assert inRange(col, row);
    return gameState[col][row];
  }
  
  public boolean notOver() {
    return this.getWinner().isInactive();
  }
  
  public boolean inRange(int col, int row) {
    return ((0<=col) && (col<this.cols)
        && (0<=row) && (row<this.rows));
  }
  
  public void move(Player p, int index, int decision) {
    this.checkWinner();
    if (!this.notOver()) return;
    if (p != getCurrentPlayer()) return;
    if (!p.notEaten(index)) return;
    p.moveChecker(index, decision);
    this.swapTurn();
    this.checkWinner();
  }
  
  public void moveNE(Player p, Checker checker) {
    if (confirmMove(p, checker.getCol() + 1, checker.getRow() + 1, checker.getCol() + 2, checker.getRow() + 2)) {
      if (eatenFlag) {
        checker.setCol(checker.getCol() + 2); checker.setRow(checker.getRow() + 2);
        unSet(checker.getCol() - 1, checker.getRow() - 1);
        unSet(checker.getCol() - 2, checker.getRow() - 2);
        setChanged();
        notifyObservers(new Move(checker.getCol() + 2, checker.getRow() + 2, p));
        eatenFlag = false;
      } else {
        checker.setCol(checker.getCol() + 1); checker.setRow(checker.getRow() + 1);
        unSet(checker.getCol() - 1, checker.getRow() - 1);
        setChanged();
        notifyObservers(new Move(checker.getCol() + 1, checker.getRow() + 1, p));
      }
      
      lastMove = new Move(checker.getCol(), checker.getRow(), p);
      return;
    }
  }
  
  public void moveNW(Player p, Checker checker) {
    if (confirmMove(p, checker.getCol() - 1, checker.getRow() + 1, checker.getCol() - 2, checker.getRow() + 2)) {
      if (eatenFlag) {
        checker.setCol(checker.getCol() - 2); checker.setRow(checker.getRow() + 2);
        unSet(checker.getCol() + 1, checker.getRow() - 1);
        unSet(checker.getCol() + 2, checker.getRow() - 2);
        setChanged();
        notifyObservers(new Move(checker.getCol() - 2, checker.getRow() + 2, p));
        eatenFlag = false;
      } else {
        checker.setCol(checker.getCol() - 1); checker.setRow(checker.getRow() + 1);
        unSet(checker.getCol() + 1, checker.getRow() - 1);
        setChanged();
        notifyObservers(new Move(checker.getCol() - 1, checker.getRow() + 1, p));
      }
      
      lastMove = new Move(checker.getCol(), checker.getRow(), p);
      return;
    }
  }
  
  public void moveSE(Player p, Checker checker) {
    if (confirmMove(p, checker.getCol() + 1, checker.getRow() - 1, checker.getCol() + 2, checker.getRow() - 2)) {
      if (eatenFlag) {
        checker.setCol(checker.getCol() + 2); checker.setRow(checker.getRow() - 2);
        unSet(checker.getCol() - 1, checker.getRow() + 1);
        unSet(checker.getCol() - 2, checker.getRow() + 2);
        setChanged();
        notifyObservers(new Move(checker.getCol() + 2, checker.getRow() - 2, p));
        eatenFlag = false;
      } else {
        checker.setCol(checker.getCol() + 1); checker.setRow(checker.getRow() - 1);
        unSet(checker.getCol() - 1, checker.getRow() + 1);
        setChanged();
        notifyObservers(new Move(checker.getCol() + 1, checker.getRow() - 1, p));
      }
      
      lastMove = new Move(checker.getCol(), checker.getRow(), p);
      return;
    }
  }
  
  public void moveSW(Player p, Checker checker) {
    if (confirmMove(p, checker.getCol() - 1, checker.getRow() - 1, checker.getCol() - 2, checker.getRow() - 2)) {
      if (eatenFlag) {
        checker.setCol(checker.getCol() - 2); checker.setRow(checker.getRow() - 2);
        unSet(checker.getCol() + 2, checker.getRow() + 2);
        unSet(checker.getCol() + 1, checker.getRow() + 1);
        setChanged();
        notifyObservers(new Move(checker.getCol() - 2, checker.getRow() - 2, p));
        eatenFlag = false;
      } else {
        checker.setCol(checker.getCol() - 1); checker.setRow(checker.getRow() - 1);
        unSet(checker.getCol() + 1, checker.getRow() + 1);
        setChanged();
        notifyObservers(new Move(checker.getCol() - 1, checker.getRow() - 1, p));
      }
      
      lastMove = new Move(checker.getCol(), checker.getRow(), p);
      return;
    }
  }
  
  public boolean confirmMove(Player p, int firstCol, int firstRow, int secondCol, int secondRow) {
    if (inRange(firstCol, firstRow)) {
      if (getState(firstCol, firstRow).isInactive()) {
        set(firstCol, firstRow, p);
        return true;
      } else if (getState(firstCol, firstRow) != p) {
        if (inRange(secondCol, secondRow)) {
          getState(firstCol, firstRow).eatChecker(firstCol, firstRow);
          set(secondCol, secondRow, p);
          eatenFlag = true;
          return true;
        }
      }      
    }
    return false;
  }
  
  protected abstract void init();
  
  protected abstract void initPlayers();
  
  protected void set(int col, int row, Player player) {
    assert inRange(col, row);
    gameState[col][row] = player;
  }
  
  protected void unSet(int col, int row) {
    gameState[col][row] = nobody;
  }
  
  protected void swapTurn() {
    turn = (turn == red) ? black : red;
  }
  
  public void skipTurn() {
    this.swapTurn();
  }
  
  protected void checkWinner() {
    if (player[red].allEaten()) {
      this.setWinner(player[black]);
    } else if (player[black].allEaten()) {
      this.setWinner(player[red]);
    }
  }
  
  protected void setWinner (Player player) {
    winner = player;
  }
  
  public Move getLastMove() {
    return lastMove;
  }
  
  public String toString() {
    StringBuffer rep = new StringBuffer();

    for (int row = this.rows - 1; row >= 0; row--) {
        rep.append(1 + row);
        if (row < 9)
            rep.append(' '); // extra space for single digit
        rep.append("  ");
        for (int col=0; col < this.cols; col++) {
            //System.out.println(col + " " + row + " " + this.getState(col,row).getMark());
            rep.append(this.getState(col,row).getMark());
            if (col < this.cols - 1) {
                rep.append(" | ");
            }
        }
        rep.append('\n');
        if (row > 0) {
            rep.append("   ---");
            for (int i = 1; i < this.cols; i++)
                rep.append("+---");
            rep.append("\n");
        }
    }
    rep.append(" ");
    for (int col = 0; col < this.cols; col++) {
        rep.append("   ");
        rep.append((char) ('a' + col));
    }
    rep.append("\n");
    return(rep.toString());
  }
}