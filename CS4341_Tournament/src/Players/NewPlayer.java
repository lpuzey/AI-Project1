package Players;

import Utilities.Move;
import Utilities.StateTree;

public class NewPlayer extends Player{

	public NewPlayer(String n, int t, int l) {
		super(n, t, l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Move getMove(StateTree state) {
		for(int j=0; j<state.columns; j++)
		{
			for(int i=0; i<state.rows; i++)
			{
				//(0 = empty, 1 = player1, 2 = player2)
				//if the row and column is empty
				if(state.getBoardMatrix()[i][j] == 0)
				{
					return new Move(false, j); // j is the column number starting at 0
				}
				//starting the game we want them to take and control the middle of the board			
				if(this.turn == 1)
					return new Move(false, (state.columns+1)/ 2);
				if(this.turn == 2)
					return new Move(false, (state.columns+1)/2);	
			}
			
		}
		return new Move(false, 100);
	}
	

}
