package checkers;

import checkers.Player;

public class Move {
  public final int col;
  public final int row;
  public final Player player;
  
  public Move(int col, int row, Player player) {
    this.col = col;
    this.row = row;
    this.player = player;
  }
  
  public double getMoveCol() {
    return 1.0 * col;
  }
  
  public double getMoveRow() {
    return 1.0 * row;
  }
  
  public Player getMovePlayer() {
    return player;
  }
  
  public String toString() {
    return "Move(" + col + "," + row + "," + player + ")";
  }
}