package checkers;

public class InactivePlayer implements Player {
  private final char mark;
  private int initialPositionIndex;
  protected BoardGame game;
  protected Checker[] checkers;
  
  public InactivePlayer(char initMark) {
    mark = initMark;
    initialPositionIndex = 0;
  }
  
  public InactivePlayer() {
    this(' ');
  }

  public char getMark() {
    return mark;
  }

  public boolean isInactive() {
    return this.getMark() == ' ';
  }

  public void setGame(BoardGame game) {
    this.game = game; //possible error
  }
  
  public Checker[] getCheckers() {
    return checkers;
  }
  
  public void moveChecker(int index, int decision) {
    assert inRange(index);
    
    if (decision == 1) {
      game.moveNE(this, checkers[index]);
    } else if (decision == 2) {
      game.moveNW(this, checkers[index]);
    } else if (decision == 3) {
      game.moveSE(this, checkers[index]);
    } else {
      game.moveSW(this, checkers[index]);
    }
  }
  
  public void eatChecker(int col, int row) {
    for (Checker checker : checkers) {
      if (checker.getCol() == col && checker.getRow() == row) {
        checker.getEaten();
      }
    }
  }
  
  public boolean allEaten() {
    for (Checker checker : checkers) {
      if (!checker.isEaten()) {return false;}
    }
    return true;
  }
  
  public void setPieces(int pieces) {
    checkers = new Checker[pieces];
    for (int i = 0; i < pieces; i++) {
      checkers[i] = new Checker();
    }
  }
  
  public void givePosition(int col, int row) {
    checkers[initialPositionIndex].setCol(col);
    checkers[initialPositionIndex].setRow(row);
    ++initialPositionIndex;
  }
  
  private boolean inRange(int index) {
    return index > 0 && index < checkers.length;
  }

  public String toString() {
    if (this.isInactive()) {
      return "nobody";
    } else {
      return new Character(this.getMark()).toString();
    }
  }
  
  public void debugPositions() {
    int i = 0;
    for (Checker checker : checkers) {
      System.out.println("Checker with index: " + i++ 
          + " has dimensions col: " + checker.getCol() + " row: " + checker.getRow());
    }
  }

  public boolean notEaten(int index) {
    return !checkers[index].isEaten();
  }
}