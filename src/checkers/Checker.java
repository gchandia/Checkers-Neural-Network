package checkers;

public class Checker {
  private int row;
  private int col;
  private boolean eaten;
  
  public Checker() {
    row = -1;
    col = -1;
    eaten = false;
  }
  
  public int getRow() {
    return row;
  }
  
  public void setRow(int row) {
    this.row = row;
  }
  
  public int getCol() {
    return col;
  }
  
  public void setCol(int col) {
    this.col = col;
  }
  
  public boolean isEaten() {
    return eaten;
  }
  
  public void getEaten() {
    eaten = true;
  }
}