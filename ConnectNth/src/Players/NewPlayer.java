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
		if(winningColumn(state,turn) != -1) {
			hueristicEval[winningColumn(state,turn)] += 1000;
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
	int winningColumn(StateTree state, int piece){ 
		int winNum = state.winNumber - 1;
		//check Horizontal
		for(int c=0; c<state.columns - winNum; c++)
		{
			for(int r=0; r<state.rows; r++)
			{
				if((state.getBoardMatrix()[r][c] == piece)&&(state.getBoardMatrix()[r][c+1] == piece)&&(state.getBoardMatrix()[r][c+2] == piece)&&(state.getBoardMatrix()[r][c+3] == 0)) {	
					return c+3; 
				}
					
			}
		}
		//check Vertical
		for(int c=0; c<state.columns; c++)
		{
			for(int r=0; r<state.rows - winNum; r++)
			{
				if((state.getBoardMatrix()[r][c] == piece)&&(state.getBoardMatrix()[r+1][c] == piece)&&(state.getBoardMatrix()[r+2][c] == piece)&&(state.getBoardMatrix()[r+3][c] == 0)) {
					return c; 
				}
					
			}
		}
		//check diagonals going up
		for(int c=0; c<state.columns - winNum; c++)
		{
			for(int r=0; r<state.rows - winNum; r++)
			{
				if((state.getBoardMatrix()[r][c] == piece)&&(state.getBoardMatrix()[r+1][c+1] == piece)&&(state.getBoardMatrix()[r+2][c+2] == piece)&&(state.getBoardMatrix()[r+3][c+3] == 0)) {
					return c+3; 
				}
					
			}
		}
		//check diagonals going down
		for(int c=0; c<state.columns - winNum; c++)
		{
			for(int r=winNum; r<state.rows ; r++)
			{
				if((state.getBoardMatrix()[r][c] == piece)&&(state.getBoardMatrix()[r-1][c+1] == piece)&&(state.getBoardMatrix()[r-2][c+2] == piece)&&(state.getBoardMatrix()[r-3][c+3] == 0)) {
					return c+3; 
				}
					
			}
		}
		return -1;
	}
	
	//count the number of a piece in an array
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

	public int minimax(StateTree state,int depth, int heuristic_eval) {
		 if(depth==0) {
		  return heuristic_eval;
		 }else {
		  int eval = -10000000;
		  for(int i=0; i<state.children.size(); i++) {
		  eval= minimax(state.children.get(i), (depth-1), heuristic_eval);
		  }
		  return eval;
		 }
		}
*/
}

