import java.util.Scanner;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MinesweeperGame {
	private int cols;
	private int rows;
	private int[][] board;
	private boolean[][] mines;
	
	private static Scanner in = new Scanner(System.in);

	MinesweeperGame(String seed) {
		/**
		 * 0 = not revealed
		 * -1 = revealed by "0" bombs around the field
		 * 1-8 is reserved for the number of bombs around the field
		 * 9 = potential bomb
		 * 10 = for sure bomb
		 */
		
		try {
			  File configFile = new File(seed);
			  Scanner configScanner = new Scanner(configFile);
			  
			  int rows = configScanner.nextInt();
			  int cols = configScanner.nextInt();
			  int numMines = configScanner.nextInt();

			  this.rows = rows;
			  this.cols = cols;
			  
			  if (rows < 0 || cols < 0) {
				  System.err.println("\nSeedfile Value Error: Cannot create a mine field with that many rows and/or columns!");
				  System.exit(3);
			  }
			  
			  // number of mines is more than the number of total gris
			  if (numMines > rows * cols) {
				  System.err.println("\nSeedfile Value Error: Number of mines exceeds the number of squares!");
				  System.exit(3);
			  }
			  
			  board = new int[rows][cols];
			  this.mines = new boolean[rows][cols];
			  
			  for (int i = 0; i < numMines; i++) {
				  int mineRow = configScanner.nextInt();
				  int mineCol = configScanner.nextInt();
				  
				  if (mineRow < 0 || mineCol < 0 || mineRow > rows || mineCol > cols) {
					  System.err.println("\nSeedfile Value Error: Mine is out of bounds!");
					  System.exit(3);
				  }
				  mines[mineRow][mineCol] = true;
			  }
		} catch (FileNotFoundException | InputMismatchException e) {
			System.err.println("\nSeedfile Format Error: Cannot create game with " + seed + ", because it is not formatted correctly.");
			System.exit(1);
		} // try
	}
	
	private String[] promptUser() {
		System.out.print("minesweeper-alpha: ");
		
		String command = in.nextLine().trim();
		// if the command was empty then the split method
		// would return an array with an empty string ([""])
		// which we do not want
		if (command.equals("")) {
			return new String[0];
		}
		
		return command.split("\\s+");
	}
	
	
	private String adjustPlayingField(String[] input) throws Exception {
		if (input.length == 0) {
			throw new Exception("\nInput Error: Command not recognized!");
		}
		
		String command = input[0];

		switch(command) {
			case "quit": case "q":
				quitGame();
				
				return command;
			case "reveal": case "r":
				int[] rcoords = validateCoords(input);
				reveal(rcoords[0], rcoords[1]);
				
				return command;
			case "mark": case "m":
				int[] mcoords = validateCoords(input);
				mark(mcoords[0], mcoords[1]);
				
				return command;
			case "guess": case "g":
				int[] gcoords = validateCoords(input);
				guess(gcoords[0], gcoords[1]);
				
				return command;
			case "help": case "h":
				showHelpMenu();
				
				return command;
			case "nofog": 
				return "nofog";
		}
		throw new Exception("\nInput Error: Command not recognized!");
	}
	
	private void showHelpMenu() {
		System.out.println("\nCommands Available...\n" + 
				" - Reveal: r/reveal row col\n" + 
				" -   Mark: m/mark   row col\n" + 
				" -  Guess: g/guess  row col\n" + 
				" -   Help: h/help\n" + 
				" -   Quit: q/quit");
	}
	
	private void quitGame() {
		System.out.println("\nQuitting the game...\n" + 
				"Bye!");
		System.exit(0);
	}
	
	private void guess(int row, int col) {
		board[row][col] = 9;
	}
	
	private void mark(int row, int col) {
		board[row][col] = 10;
	}
	
	private void reveal(int row, int col) throws Exception {
		int numberOfMines = findNumberOfNeighboringMines(row, col);

		board[row][col] = numberOfMines == 0 ? -1 : numberOfMines;
	}
	
	private int findNumberOfNeighboringMines(int row, int col) {
		int count = 0;
		
		int rowStart = Math.max(0, row - 1);
		int rowEnd = Math.min(rows - 1, row + 1);
		
		int colStart = Math.max(0, col - 1);
		int colEnd = Math.min(cols - 1, col + 1);
		
		for (int i = rowStart; i <= rowEnd; i++) {
			for (int j = colStart; j <= colEnd; j++) {
				// skip self
				if (i == row && j == col) continue;

				boolean isMine = mines[i][j];
				if (isMine) {
					count++;
				}
			}
		}
		return count;
	}
	
	private int[] validateCoords (String[] input) throws Exception {
		if (input.length != 3) {
			throw new Exception("\nInput Error: Command not recognized!");
		}
		
		int row, col;
		try {
			row = Integer.parseInt( input[1] );
	        col = Integer.parseInt( input[2] );
		} catch (NumberFormatException e) {
			throw new Exception("\nInput Error: Command not recognized!");
		}
        
        if (row < 0 || col < 0 || row > rows || col > cols) {
        		throw new Exception("\nInput Error: Command not recognized!");
        }
        return new int[] { row, col };
	}

	private int numOfDigits(int num) {
		int count = 0;
		while(num != 0)
		{
			// num = num/10
			num /= 10;
			++count;
		}
		return count;
	}

	private void printMineField(int round, boolean nofog) {
		/*
		 5 => 1
		 12 => 2
		 101 =>

		 */

		int rowSpacesNeeded = numOfDigits(rows) + 1;
		int colSpacesNeeded = numOfDigits(cols) + 1;

		System.out.println(colSpacesNeeded + " << spaces");
		String rowNumberFormat = "%" + rowSpacesNeeded + "d |";
		String colNumberFormat = "%" + colSpacesNeeded + "d  ";
		String characterFormat = "%" + colSpacesNeeded + "s |";
		
		System.out.printf("\nRounds Completed: %d \n\n", round);
		
		for (int i = 0; i < rows; i++) {
			System.out.printf(rowNumberFormat, i);
			
			for (int j = 0; j < cols; j++) {
				int value = board[i][j];
				String character = "";
				
				if (value == -1) {
					character = "0";
				} else if (value >= 1 && value <= 8) {
					character = value + "";
				} else if (value == 9) {
					character = "?";
				} else if (value == 10) {
					character = "F";
				}
				
				if (nofog) {
					boolean isMine = mines[i][j];

					if (isMine) {
						String characterFormatWithNoFog = "<%" + (colSpacesNeeded - 1) + "s>|";
						System.out.printf(characterFormatWithNoFog, character);
					} else {
						System.out.printf(characterFormat, character);
					}
				} else {
					System.out.printf(characterFormat, character);
				}
			}
			
			System.out.println();
		}

		System.out.printf("%"+(rowSpacesNeeded+1)+"s ", "");
		for (int j = 0; j < cols; j++) {
			System.out.printf(colNumberFormat, j);
		}
		System.out.println("\n");
	}
	
	private void printWelcome () {
		System.out.println("\n        _\n" + 
				"  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __\n" + 
				" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n" + 
				"/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |\n" + 
				"\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|\n" + 
				"                 A L P H A   E D I T I O N |_| v2019.fa");
	}
	
	private void printLoss() {
		System.out.println("\n Oh no... You revealed a mine!\n" + 
				"  __ _  __ _ _ __ ___   ___    _____   _____ _ __\n" + 
				" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|\n" + 
				"| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |\n" + 
				" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|\n" + 
				" |___/");
	}
	
	private void printWin(int rounds) {
		double score = 100.0 * rows * cols / rounds;
		
		System.out.println("\n ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"\n" + 
				" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░\n" + 
				" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"\n" + 
				" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░\n" + 
				" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"\n" + 
				" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░\n" + 
				" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"\n" + 
				" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░\n" + 
				" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░\n" + 
				" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░\n" + 
				" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░\n" + 
				" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌\n" + 
				" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░\n" + 
				" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░\n" + 
				" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░\n" + 
				" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░\n" + 
				" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!\n" + 
				" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!\n" + 
				" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + score);
	}
	
	private boolean isWon () {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				boolean isIncomplete = board[i][j] == 0 || board[i][j] == 9;
				boolean isMarked = board[i][j] == 10;
				boolean mine = mines[i][j];
				
				if (!isMarked && mine || isMarked && !mine || isIncomplete) {
					return false;
				} 
			}
		}
		return true;
	}
	
	private boolean isLost () {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int value = board[i][j];
				boolean mine = mines[i][j];
				
				if ((value >= 1 && value <= 8 || value == -1) && mine) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void play () {
		int rounds = 0;
		
		printWelcome ();
		printMineField(rounds, false);
		
		while(true) {
			String[] askForInput = promptUser();
			String command = "";
			
			try {
				command = adjustPlayingField(askForInput);
			} catch (Exception e) {
				String msg = e.getMessage();
				System.out.println(msg);
			}
			rounds++;
			
			if (isWon()) {
				printWin(rounds);
				break;
			}
			
			if (isLost()) {
				printLoss();
				break;
			}
			
			printMineField(rounds, command == "nofog");
		}
	}
}
