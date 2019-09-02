package Players;


import java.util.Arrays;

import Utilities.Move;
import Utilities.StateTree;

public class NewPlayer extends Player{

	int turnNum = 0;
	
	public NewPlayer(String n, int t, int l) {
		super(n, t, l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Move getMove(StateTree state) {
		//array with all are heuristic evaluations for each column
		//The size of the array is equal to the number of columns in the board
		int[] hueristicEval = new int[state.columns];
		
		int minimax_val = minimax(state, 2, 1);
		System.out.println("MINIMAX VALUE: " + minimax_val);
		
		//Goes through the array and makes everything 0 unless it is the middle element in which it makes it 1
		for(int j=0; j<state.columns; j++)
		{
			//checks if it is the middle term
			if(j==(state.columns-1)/ 2) {
				//makes it equal to 1 because that is most valuable at the beginning of the game. 
				hueristicEval[j] = 1;
			}
			else
				hueristicEval[j] = 0;
		}

		int max = hueristicEval[0];
		int index = -1000;
		
		for(int j=0; j<state.columns; j++)
		{
			for(int i=0; i<state.rows - 3; i++)
			{
				if((turn == 2)&& (state.getBoardMatrix()[i][j] == 1)&&(state.getBoardMatrix()[i+1][j] == 1)&&(state.getBoardMatrix()[i+2][j]==1)) {
					
					hueristicEval[j] = 2;
					if(state.getBoardMatrix()[i+3][j]==2) {
						hueristicEval[j] = 0;
					}
				}
				
			}
		}
		if(wonGame(state,turn)) {
		//	hueristicEval[winningColumn(state,turn)] += 1000;
		}
		
		
		//goes through the list looking for the maximum value and finds the index of it
		for (int i = 0; i < hueristicEval.length; i++) 
		{
			if (max < hueristicEval[i]) 
			{
				max = hueristicEval[i];
				index = i;
			}
		}
		

		//plays the move with the highest heuristic value
	//	if(state.getBoardMatrix()[i][index] == 0)
	//	{
	//	StateTree test = StateTree(1, 2, 3, 4, true, false, state);
	//state.children.add(test);
		turnNum++;
		
//		System.out.println("MINIMAX: " + minimax(state, turnNum, max));
			
			return new Move(false, index);	
			
		}
		
			
	int evaluateScore(StateTree state, int piece) {
		//horizontal score
		int score = 0;
		int[] rowArray = new int[state.rows];
		int[] columnArray = new int[state.columns];
		int winNum = state.winNumber - 1;

		// score Horizontal
		for(int r=0; r<state.rows; r++){
			rowArray[r] = r;
			for(int c=0; c<state.columns- winNum; c++){
				int[] window = Arrays.copyOfRange(rowArray, c , c + winNum );
				//count the number of piece and see if it is equal to state.winNumber
				score += 100;
				//count num of piece in window and see if it is equal to 3 and one of them is empty == 0
				//might be able to use winning column
				score += 10;	
			}
		}
		//score Vertical
		for(int c=0; c<state.columns; c++){
			columnArray[c] = c;
			for(int r=0; r<state.rows- winNum; r++){
				int[] window = Arrays.copyOfRange(columnArray, r , r + winNum );
				//count the number of piece in window and see if it is equal to state.winNumber
				if(count(window, piece) == 4) {
					score += 100;
				}
				else if((count(window,piece) == 3)&&(count(window,0) == 1)) {
					score+= 10;
				}
				
			}
		}
		//upwards diagonals
		for(int r=0; r<state.rows - winNum; r++){
			for(int c=0; c<state.columns- winNum; c++){
			//	int[] window = 
				//count the number of piece and see if it is equal to state.winNumber
				score += 100;
				//count num of piece in window and see if it is equal to 3 and one of them is empty == 0
				//might be able to use winning column
				score += 10;	
			}
		}
		
		return score;
		
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
					if(state.getBoardMatrix()[r-i][c+1] != piece) {
						badNum = true; 							
					}
					if((badNum == false)&& (i == winNum)) {
						return true;
					}
				}

			}
		}
		return false;
	}

	
	//counts the number of a piece in an array
	int count(int[] array, int piece) {
		int count = 0;
		for(int i = 0; i < array.length; i++)
	        {
	            if(array[i] == piece)
	            {
	                count++;
	            }
	        }
		return count;
	}
		 
	


	
	/**
	* This function will create children for any element in a tree that doent have children
	* and isn't and end condition
	*/
/*	public void makeChildren(StateTree state) 
	{
		if(state.children.isEmpty())  // if this statetree object has no children, make it children
		{
				int nextTurn = (turn == 1) ? 2 : 1;  // this switches the turn so we know whos turn is next
				for(int i=0;  i<state.rows; i++)
				{
					if(state.getBoardMatrix()[i][state.columns] != 0)//if column is completely filled
					{
						int[] childMove = {i, 1};
						state.children.add(StateTree(state.rows, state.columns, state.winNumber, state.pop1, state.pop2, state));
					}
					if(turn == 1 && (state.pop1==false||state.pop2==false))
					{
						int[] childMove = {i, 0};
						state.children.add(StateTree(state.rows, state.columns, state.winNumber, state.pop1, state.pop2, state));
					}
					else if(turn == 2 && (state.pop1==false&&state.pop2==false))
					{
						int[] childMove = {i, 0};
						state.children.add(StateTree(state.rows, state.columns, state.winNumber, state.pop1, state.pop2, state));
					}
				}
			
		}
		else // if it already has children make children for it's children
		{
			for(StateTree child: state.children)
			{
				this.makeChildren(state);
			}
		}
	}
	
	private StateTree StateTree(int i, int j, int l, boolean b, boolean c, StateTree state) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
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

	
	public int minimax(StateTree board, int depth, int playerNum) {
		if(depth==0 || isTerminalNode(board)) {
			if(isTerminalNode(board)) {
				if(wonGame(board, 1)) {
					return 1000000;
				} else if(wonGame(board, 2)) {
					return -1000000;
				} else {
					return 0;
				}
			} else //depth is 0
			{
				return evaluateScore(board, 1);
			}
		}
		
		if(playerNum==1)
		{
			int value = -1000000000;
			for(int i=0; i<board.children.size(); i++)
			{
				value = max(value, minimax(board, (depth-1), 2));
			}
			return value;
		}
		
		if(playerNum==2)
		{
			int value = 1000000000;  
			for(int i=0; i<board.children.size(); i++)
			{
				value = min(value, minimax(board, (depth-1), 1));
			}
			return value; 
		}
		 
		return 0;
	}
		

}

