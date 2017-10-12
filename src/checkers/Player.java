package checkers;

public interface Player {
  public char getMark();
  public boolean isInactive();
  public void setGame(BoardGame game);
  public Checker[] getCheckers();
  public void moveChecker(int index, int decision);
  public void eatChecker(int col, int row);
  public boolean allEaten();
  public void setPieces(int pieces);
  public void givePosition(int col, int row);
  public void debugPositions();
  public boolean notEaten(int index);
}