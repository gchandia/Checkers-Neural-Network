package prediction;

//import java.util.Arrays;
import java.util.Random;

import checkers.BoardGame;
import checkers.CheckersGame;
import checkers.InactivePlayer;
import checkers.Move;
import checkers.Player;
import network.NeuralNetwork;
import network.Neuron;
import network.NeuronLayer;

public class Game {
  public static int randomInInterval(int min, int max) {
    Random random = new Random();
    return random.nextInt(max + 1 - min) + min;
  }
  
  public double[] generateWeights(int base, int length) {
    double[] weights = new double[length];
    for (int i = 0; i < weights.length; i++) {
      weights[i] = base * i;
    }
    return weights;
  }
  
  public NeuralNetwork generateMoveNetwork() {
    NeuralNetwork network;
    NeuronLayer firstLayer = new NeuronLayer(), secondLayer = new NeuronLayer();
    
    for (int i = 0; i < 4; i++) {
      Neuron actual = new Neuron(1.0, 1.0);
      actual.setWeights(generateWeights(4 - i, 8));
      firstLayer.addNeuron(actual);
    }
    
    for (int i = 0; i < 2; i++) {
      Neuron actual = new Neuron(1.0, 1.0);
      actual.setWeights(generateWeights(2 - i, 4));
      secondLayer.addNeuron(actual);
    }
    
    network = new NeuralNetwork(firstLayer);
    network.addLayer(secondLayer);
    
    return network;
  }
  
  public double[][] generateMoveInputs() {
    double[][] inputs = new double[1680][8];
    int i = 0;
    
    for (int firstEnemyCol = 0; firstEnemyCol < 4; firstEnemyCol++) {
      for (int firstEnemyRow = 0; firstEnemyRow < 4; firstEnemyRow++) {
        if (!inTable(firstEnemyCol, firstEnemyRow)) {
          continue;
        }
        for (int myFirstCol = 0; myFirstCol < 4; myFirstCol++) {
          for (int myFirstRow = 0; myFirstRow < 4; myFirstRow++) {
            if (!inTable(myFirstCol, myFirstRow) || (myFirstCol == firstEnemyCol && myFirstRow == firstEnemyRow)) {
              continue;
            }
            for (int mySecondCol = 0; mySecondCol < 4; mySecondCol++) {
              for (int mySecondRow = 0; mySecondRow < 4; mySecondRow++) {
                if (!inTable (mySecondCol, mySecondRow) || (mySecondCol == myFirstCol && mySecondRow == myFirstRow)
                    || (mySecondCol == firstEnemyCol && mySecondRow == firstEnemyRow)) {
                  continue;
                }
                for (int secondEnemyCol = 0; secondEnemyCol < 4; secondEnemyCol++) {
                  for (int secondEnemyRow = 0; secondEnemyRow < 4; secondEnemyRow++) {
                    if (!inTable (secondEnemyCol, secondEnemyRow) 
                        || (secondEnemyCol == firstEnemyCol && secondEnemyRow == firstEnemyRow)
                        || (secondEnemyCol == myFirstCol && secondEnemyRow == myFirstRow)
                        || (secondEnemyCol == mySecondCol && secondEnemyRow == mySecondRow)) {
                      continue;
                    }
                    double[] actual = {firstEnemyCol, firstEnemyRow, 
                                       secondEnemyCol, secondEnemyRow,
                                       myFirstCol, myFirstRow, 
                                       mySecondCol, mySecondRow};
                    inputs[i++] = actual;
                  }                
                }
              }
            }
          }
        }
      }
    }
    return inputs;
  }
  
  public double[][] generateMoveOutputs(double[][] inputs) {
    double[][] outputs = new double[inputs.length][2];
    
    for (int i = 0; i < inputs.length; i++) {
      double[] actual = new double[2];
      
      double min0 = Math.min((Math.abs(inputs[i][0] - inputs[i][4]) + Math.abs(inputs[i][1] - inputs[i][5])) , 
          (Math.abs(inputs[i][2] - inputs[i][4]) + Math.abs(inputs[i][3] - inputs[i][5])));
      double min1 = Math.min((Math.abs(inputs[i][0] - inputs[i][6]) + Math.abs(inputs[i][1] - inputs[i][7])) , 
          (Math.abs(inputs[i][2] - inputs[i][6]) + Math.abs(inputs[i][3] - inputs[i][7])));
      
      if (min0 <= min1) {
        actual[0] = 0;
        actual[1] = desiredDecision(inputs[i][0], inputs[i][1], inputs[i][2], inputs[i][3], inputs[i][4], inputs[i][5]);
      } else {
        actual[0] = 1;
        actual[1] = desiredDecision(inputs[i][0], inputs[i][1], inputs[i][2], inputs[i][3], inputs[i][6], inputs[i][7]);
      }
    }
    return outputs;
  }
  
  public double desiredDecision(double firstEnemyCol, double firstEnemyRow,
                                  double secondEnemyCol, double secondEnemyRow,
                                  double myCol, double myRow) {
    
    double[] inputs = new double[4];
    
    if ((Math.abs(firstEnemyCol - myCol) + Math.abs(firstEnemyRow - myRow))
        <= (Math.abs(secondEnemyCol - myCol) + Math.abs(secondEnemyRow - myRow))) {
      inputs[0] = firstEnemyCol;
      inputs[1] = firstEnemyRow;
    } else {
      inputs[0] = secondEnemyCol;
      inputs[1] = secondEnemyRow;
    }
    
    inputs[2] = myCol;
    inputs[3] = myRow;
    
    if (inputs[0] - inputs[2] <= 0 && inputs[1] - inputs[3] <= 0) {
      return 1.0; //4
    } else if (inputs[0] - inputs[2] <= 0 && inputs[1] - inputs[3] > 0) {
      return 0.75; //3
    } else if (inputs[0] - inputs[2] > 0 && inputs[1] - inputs[3] <= 0) {
      return 0.5; //2
    } else {
      return 0.0; //1
    }
  }
  
  private boolean inTable(int enemyCol, int enemyRow) {
    return (enemyCol % 2 == 0 && enemyRow % 2 != 0) || (enemyCol % 2 != 0 && enemyRow % 2 == 0);
  }
  
  private static int getApproxIndex(double index) {
    if (index <= 0.99995) {
      return 0;
    }
    return 1;
  }
  
  private static int getApproxDecision(double decision) {
    if (decision <= 0.0011) {
      return 1;
    } else if (decision <= 0.0042) {
      return 2;
    } else if (decision <= 0.0042647865986) {
      return 3;
    } else {
      return 4;
    }
  }

  public static void main(String[] args) {
    Game driver = new Game();
    int turns = 50;
    
    NeuralNetwork network = driver.generateMoveNetwork();
    
    double[][] myInputs = driver.generateMoveInputs();
    double[][] myOutputs = driver.generateMoveOutputs(myInputs);
    
    
    Player red = new InactivePlayer('r');
    Player black = new InactivePlayer('b');
    BoardGame game = new CheckersGame(red, black);
    
    while (turns > 0) {
      game.move(red, randomInInterval(0, 1), randomInInterval(1, 4));
      
      Move lastMove = game.getLastMove();
      
      if (lastMove.getMovePlayer() == red) {
        double[] moveInput = {lastMove.getMoveCol(), lastMove.getMoveRow()};
        game.move(black, getApproxIndex(network.feed(moveInput)[0]), 
                  getApproxDecision(network.feed(moveInput)[1]));
      } else {
        game.skipTurn();
      }
      --turns;
    }
    
    System.out.println("FIRST GAME:");
    System.out.println(game.toString());
    System.out.println("");
    
    for (int j = 1; j < 100; j++) {
      for (int i = 0; i < 1680; i++) {
        network.train(myInputs[i], myOutputs[i]);
      }
    }
    
    
    Player redTwo = new InactivePlayer('r');
    Player blackTwo = new InactivePlayer('b');
    BoardGame gameTwo = new CheckersGame(redTwo, blackTwo);
    turns = 50;
    
    while (turns > 0) {
      gameTwo.move(redTwo, randomInInterval(0, 1), randomInInterval(1, 4));
      
      Move lastMove = gameTwo.getLastMove();
      
      if (lastMove.getMovePlayer() == redTwo) {
        double[] moveInput = {lastMove.getMoveCol(), lastMove.getMoveRow()};
        gameTwo.move(blackTwo, getApproxIndex(network.feed(moveInput)[0]), 
                     getApproxDecision(network.feed(moveInput)[1]));
      } else {
        gameTwo.skipTurn();
      }
      --turns;
    }
    
    System.out.println("SECOND GAME:");
    System.out.println(gameTwo.toString());
  }
}