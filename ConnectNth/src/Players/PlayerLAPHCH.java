package Players;


import java.util.ArrayList;


import Utilities.Move;
import Utilities.StateTree;
import Referee.RefereeBoard;

public class PlayerLAPHCH extends Player{
	
	StateTree optimalState;
	
	
	public PlayerLAPHCH(String n, int t, int l) {
		super(n, t, l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Move getMove(StateTree state) {
		
		//gets the optimal state of the board from the minimax
		state.children = new ArrayList<StateTree>();
		makeChildren(state, 6);
		StateTree OPTIMAL_STATE = getOptimalState();

		//goes through the board and finds the new move that minimax decided to make
		int index = -1000;
		for(int r=0; r<state.rows; r++)
		{
			for(int c=0; c<state.columns; c++)
			{
				if(state.getBoardMatrix()[r][c] != OPTIMAL_STATE.getBoardMatrix()[r][c]) {
					index = c;
				}
				
			}
		}

		//makes the move with the index given
		return new Move(false, index);	
			
		}
		
			
	
	//function that goes through the state tree and evaluates the point totals
	int evaluateScore(StateTree state, int piece) {
		int score = 0;
		int winNum = state.winNumber - 1;
		//was supposed to always favor the middle column
//		if (!columnFull(state,((state.columns-1)/ 2))){
//			score += 3;
//		}
		
	
		// score Horizontal
		//check in windows of 4 and looks horizontally at the board
		for(int r=0; r<state.rows; r++){
			int[] rowArray = new int[state.winNumber];
			
			for(int c=0; c<(state.columns-winNum); c++){
				
	
				rowArray[c] = state.getBoardMatrix()[r][c];
		}
			//helper function that tells you how much each point piece in a row is worth
			score += computeScore(rowArray, state, piece,score);
	}
		
		//Our old attempt to do score Horizontal
	/*	for(int r=0; r<state.rows; r++){
			rowArray[r] = r;
			for(int c=0; c<state.columns- winNum; c++){
				window = Arrays.copyOfRange(rowArray, c , c + state.winNumber );
				score += computeScore(window, state, piece,score);
				System.out.println("current score H:" + score);
	
			}
		}*/
		//score Vertical
		//check in windows of 4 and looks vertically at the board
			for(int c=0; c<state.columns; c++){
				int[] columnArray = new int[state.winNumber];
				
				for(int r=0; r<(state.rows-winNum); r++){
					
	
					columnArray[r] = state.getBoardMatrix()[r][c];
			}
				//helper function that tells you how much each point piece in a row is worth
				score += computeScore(columnArray, state, piece,score);
		}
		//upwards diagonals
		//this was our attempt to check diagonallys facing upwards, but we could not get it to work the way we wanted
			/*	for(int c=0; c<state.columns - winNum; c++){
			for(int r=0; r<state.rows- winNum; r++){
				for(int i = 0; i < state.winNumber; i++) {
					 window[i] = state.getBoardMatrix()[r+i][c+i];
				}
				score += computeScore(window, state, piece,score);
				System.out.println("current score UD:" + score);
			}
		}
		//downwards diagonals
	    //this was our attempt to check diagonallys facing upwards, but we could not get it to work the way we wanted
		for(int c=0; c<state.columns - winNum; c++){
			for(int r=0; r<state.rows- winNum; r++){
				for(int i = 0; i < state.winNumber; i++) {
					 window[i] = state.getBoardMatrix()[r+winNum-i][c+i];
				}
				score += computeScore(window, state, piece,score);
				System.out.println("current score DD:" + score);
			}
		}*/
		
		//returns the score of the board evaluation
		return score;
		
	}

	//helper function for evaluate score
	//goes through and counts the pieces in the windows and scores them accordingly
	int computeScore(int[] window, StateTree state,int piece,int score) {
		int initScore = score;
		int winNum = state.winNumber - 1;
		int oppPiece = -1;
		if(piece == 1 ) {
			 oppPiece = 2;
		}
		else {
			oppPiece = 1; 
		}
		
		if(count(window,piece) == state.winNumber) {
			initScore += 100;
		}
		else if((count(window,piece) == winNum)&&(count(window, 0)==1)) {
			initScore += 10;
		}
		else if((count(window,piece) == winNum - 1)&&(count(window, 0) == 2)) {
			initScore += 5;
		}
	//was supposed to help block an opponent that had 3 in a row, but didn't work
	//	if((count(window,oppPiece) == winNum)&&(count(window,0) == 1)) {
	//		initScore -= 80;
	//	}
		
		return initScore;
	}
	
	//returns the Column number for a winning 4 in a row
	boolean wonGame(StateTree state, int piece){ 
		int winNum = state.winNumber - 1;
		boolean badNum = false;
		//check Horizontal
		for(int c=0; c<state.columns - winNum; c++)
		{
			for(int r=0; r<state.rows; r++)
			{
				for(int i =0; i<=winNum; i++) {
					if(state.getBoardMatrix()[r][c+i] != piece) {
						badNum = true; 							
					}
					if((badNum == false)&& (i == winNum)) {
						return true;
					}
				}

			}
		}
		//check Vertical
		for(int c=0; c<state.columns; c++)
		{
			for(int r=0; r<state.rows - winNum; r++)
			{
				for(int i =0; i<=winNum; i++) {
					if(state.getBoardMatrix()[r+i][c] != piece) {
						badNum = true; 							
					}
					if((badNum == false)&& (i == winNum)) {
						return true;
					}
				}

			}
		}
		//check diagonals going up
		for(int c=0; c<state.columns - winNum; c++)
		{
			for(int r=0; r<state.rows - winNum; r++)
			{
				for(int i =0; i<=winNum; i++) {
					if(state.getBoardMatrix()[r+i][c+i] != piece) {
						badNum = true; 							
					}
					if((badNum == false)&& (i == winNum)) {
						return true;
					}
				}

			}
		}
		//check diagonals going down
		for(int c=0; c<state.columns - winNum; c++)
		{
			for(int r=0; r<state.rows; r++)
			{
				for(int i =0; i<=winNum; i++) {
					if((r-i)>=0) {
					if(state.getBoardMatrix()[r-i][c+i] != piece) {
						badNum = true; 							
					}
					}
					if((badNum == false)&& (i == winNum)) {
						return true;
					}
				}

			}
		}
		return false;
	}

	
	//counts the number of a pieces in an array
	int count(int[] array, int piece) {
		int count = 0;
		for(int i = 0; i < array.length; i++)
	        {
	            if(array[i] == piece)
	            {
	            	count = count + 1;
	            	
	            }
	        }
		return count;
	}

	//casts statetree as a refereeboard so we can make an instance of a statetree
	public RefereeBoard getBoard(StateTree state) {
		return (RefereeBoard) state.parent;
	}
	
	//Copies the statetree board into a new double array so we don't screw up the original
	public RefereeBoard copyBoard(RefereeBoard oldBoard, RefereeBoard newBoard) {
		
		for(int i=0; i<oldBoard.rows; i++) {
			for(int j=0; j<oldBoard.columns; j++) {
				newBoard.getBoardMatrix()[i][j] = oldBoard.getBoardMatrix()[i][j];
			}
		}
		return newBoard;
	}
	
	/*
	* This function will create children for any element in a tree that doesn't have children
	* and isn't and end condition
	*/
	public void makeChildren(StateTree state, int depth) 
	{
		
		if(depth==0 && state.children.size()==0)  // if this statetree object has no children, make it children
		{
			
			for(int i=0; i<state.rows; i++) 
			{
				for(int j=0;  j<state.columns; j++)
				{
					if(state.getBoardMatrix()[i][j] == 0)//if column is not completely filled, drop it
					{
						RefereeBoard newBoard = new RefereeBoard(state.rows, state.columns, state.winNumber, state.turn, state.pop1, state.pop2, state.parent);
						
						RefereeBoard updatedBoard = copyBoard((RefereeBoard) state, newBoard);
						Move possibleDropMove = new Move(false, j);
						if(updatedBoard.validMove(possibleDropMove))
						{
						updatedBoard.makeMove(possibleDropMove);
						state.children.add(updatedBoard);
						}
					}
				}
			}
		}
		
		if(depth!=0) 
		{
				makeChildren(state, (depth-1));
		}
	}
	
	
	
	//checks if the board is full with pieces
    public static boolean checkFull(StateTree board)
    {
        for(int i=0; i<board.rows; i++)
        {
            for(int j=0; j<board.columns; j++)
            {
                if(board.getBoardMatrix()[i][j] == 0)
                    return false;
            }
        }
        return true;
    }
	
	//tells if we are on the last set of children in the minimax tree
	public boolean isTerminalNode(StateTree board) {
		return wonGame(board, 1) || wonGame(board, 2) || checkFull(board);
	}
	
	//returns the maximum of two integers
	public int max(int a, int b) {
		if(a>b) {
			return a;
		} else {
			return b;
		}
	}
	
	//returns the minimum of two integers
	public int min(int a, int b) {
		if(a<b) {
			return a;
		} else {
			return b;
		}
	}
	
	//check if a column is full with pieces
	boolean columnFull(StateTree board, int columnNum){
		for(int r = 0; r < board.rows; r++) {
			if(board.getBoardMatrix()[r][columnNum] == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	//sets the optimal statetree from the minimax function
	public void setOptimalState(StateTree state) {
		this.optimalState = state;
	}

	//gets the optimal statetree from the minimax function
	public StateTree getOptimalState() {
		return this.optimalState;
	}
	
	//Does the minimax algorithim on our statetree
	public int minimax(StateTree board, int depth, int playerNum, int alpha, int beta) {
		if(depth==0 || isTerminalNode(board)) {
			if(isTerminalNode(board)) {
				if(wonGame(board, 1)) {
					return 1000000;
				} else if(wonGame(board, 2)) {
					return -1000000;
				} else {
					System.out.println("Reached last base case");
					return 0;
				}
			} else //depth is 0
			{
				int test = evaluateScore(board, board.turn);
				return test;
			}
		}
		
		if(playerNum==1)
		{
			int value = -1000000000;
			for(int i=0; i<board.children.size(); i++)
			{
				value = max(value, minimax(board, (depth-1), 2, alpha, beta));
				setOptimalState(board.children.get(i));
				alpha = max(alpha, value);
				if(alpha >= beta) {
					break;
				}
			}
			return value;
		}
		
		if(playerNum==2)
		{
			int value = 1000000000;  
			for(int i=0; i<board.children.size(); i++)
			{
				value = min(value, minimax(board, (depth-1), 1, alpha, beta));
				setOptimalState(board.children.get(i));
				beta = min(beta, value);
				if(alpha >= beta) {
					break;
				}
			}
			return value; 
		}
		 
		return 0;
	}
		

}

