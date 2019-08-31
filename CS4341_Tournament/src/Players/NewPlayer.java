package Players;

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
		System.out.println("hello");
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
			for(int i=0; i<state.rows; i++)
			{
				if((turn == 2)&& (state.getBoardMatrix()[i][j] == 1)&&(state.getBoardMatrix()[i+1][j] == 1)&&(state.getBoardMatrix()[i+2][j]==1)) {
					
					hueristicEval[j] = 2;
					System.out.println(hueristicEval[j]);
				}
				
			}
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
			return new Move(false, index);	
	//	}
		
			
	}

}
