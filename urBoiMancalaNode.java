import java.util.ArrayList;
import java.util.Comparator;

public class urBoiMancalaNode extends MancalaNode {

	
	private class mancalaComp implements Comparator<Integer>{
		public int compare(Integer o1, Integer o2) {
			int result = 0;
			if(state[o1] +  o1 == 6)
				result += 30;
			else if(state[(state[o1] + o1) % 13] == 0 && state[(state[o1] + o1) % 13] < 6 )
				result += 10 + state[(state[o1] + o1) % 13] + 1;
			else
				result += o1;
			if(state[o2] +  o2 == 6)
				result -= 30;
			else if(state[(state[o2] + o2) % 13] == 0 && state[(state[o2] + o2) % 13] < 6 )
				result += 10 + state[(state[o2] + o2) % 13] + 1;
			else
				result -= o2;
			return result;
		}
	}


	/**
	 * TnellerMancalaNode constructor.
	 */
	Comparator<Integer> compare = new mancalaComp();
	public urBoiMancalaNode() {
		super();
	}

	public urBoiMancalaNode(MancalaNode node) {
		super(node);
	}

	public urBoiMancalaNode(int stateIndex) {
		super(stateIndex);
	}

	@Override
	public ArrayList<Integer> getLegalMoves() 
	{
		ArrayList<Integer> legalMoves = new ArrayList<Integer>();
		final int PLAYER_OFFSET = (player == MAX) ? 0 : MAX_SCORE_PIT + 1;
		for (int i = PLAYER_OFFSET; i < PLAYER_OFFSET + PLAY_PITS; i++)
			if (state[i] > 0)
				legalMoves.add(i);
		legalMoves.sort(compare);
		return legalMoves;
	}

	/**
	 * My simple utility method returns different in MAX/MIN score.
	 */
	public double utility(){
		//How close is our player to winning
		double closeToWinning = (state[MAX_SCORE_PIT] >= 25) ? 48 : state[MAX_SCORE_PIT];
		double oppWinning = (state[MIN_SCORE_PIT] >= 25) ? -48 : 0;
		//How of our pieces can be captured by other player
		double captures = -canCaptureByMin();
		//How many possible moves do we have (Old Best Weight = 1.3)
		double possibleMoves = 1.5*numMovesMax();
		//The possible moves that lead to an extra move
		double chainMoves =  2*possibleChainMax();
		//Difference of two player scores
		double maxScore = state[MAX_SCORE_PIT];
		double minScore = state[MIN_SCORE_PIT];
		double scoreDiff = 2*(maxScore - minScore);

		return chainMoves + scoreDiff + captures + possibleMoves + closeToWinning + oppWinning;
	}

	private double possibleChainMax(){
		double chain = 0;
		for(int i = 0; i < 6; i++){
			if(state[i] == 6 - i) chain++;
		}
		return chain;
	}

	private double possibleChainMin() {
		double chain = 0;
		for (int i = 7; i < 13; i++) {
			if (state[i] == 13 - i) chain++;
		}
		return chain;
	}
	private double numMovesMin(){
		double numMoves = 0;
		for(int i = 7; i < 13; i++){
			if(state[i] != 0) numMoves += 13-i;
		}
		return numMoves;
	}

	private int canCaptureByMax(){
		int possibleCaptures = 0;
		for(int j = 0; j < 6; j++){
			if(state[j] == 0){
				for(int i = 0; i < 6; i++){
					if((state[i] + i) % 13 == j) possibleCaptures += state[12-j] + 1;
				}
			}
		}
		return possibleCaptures;
	}

	private int canCaptureByMin() {
		int canBeCaptured = 0;
		for (int j = 7; j < 13; j++) {
			if (state[j] == 0) {
				for (int i = 7; i < 13; i++){
					if ((state[i] + i) % 13 == j) canBeCaptured += state[12-j] + 1;
				}
			}
		}
		return canBeCaptured;
	}

	private double numMovesMax(){
		double possibleMoves = 0;
		for(int i = 0; i < 6; i++){
			if(state[i] != 0) possibleMoves += 6-i;
		}
		return possibleMoves;
	}

	private double furthestMove(){
		for (int i = 0; i < 6; i++) {
			if (state[i] != 0) return 6-i;
		}
		return 0.0;
	}
}

