package checkers;

public class CheckersGame extends AbstractBoardGame {
  public CheckersGame(Player playerRed, Player playerBlack) {
    super(playerRed, playerBlack);
  }
  
  protected void init() {
    rows = cols = 4;
    pieces = 2;
  }
  
  protected void initPlayers() {    
    super.set(1, 0, player[red]);
    gameState[1][0].givePosition(1, 0);
    super.set(3, 0, player[red]);
    gameState[3][0].givePosition(3, 0);
    super.set(0, 3, player[black]);
    gameState[0][3].givePosition(0, 3);
    super.set(2, 3, player[black]);
    gameState[2][3].givePosition(2, 3);
  }
}