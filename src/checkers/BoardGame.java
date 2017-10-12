package checkers;

import java.util.Observer;

import checkers.Player;

public interface BoardGame {
  public int getCols();
  public int getRows();
  public void move(Player p, int index, int decision);
  public Player getCurrentPlayer();
  public Player getWinner();
  public Player getState(int col, int row);
  public boolean notOver();
  public boolean inRange(int col, int row);
  public void addObserver(Observer o);
  public Move getLastMove();
  public void moveNE(Player p, Checker checker);
  public void moveNW(Player p, Checker checker);
  public void moveSE(Player p, Checker checker);
  public void moveSW(Player p, Checker checker);
  public void skipTurn();
}