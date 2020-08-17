/**
 * urBoiMancalaPlayer - 
 *
 * @author NOT Todd Neller
 * @version 1.1 */
public class urBoiMancalaPlayer implements MancalaPlayer {
	//done from a regression of average game length left at various turn numbers. 
	static int[] avgMovesLeft = {18, 17, 16, 15, 14, 13, 12, 12, 12, 11, 11, 10, 9, 9, 8, 7, 7, 7, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	static int numMovesMade = 0;
	final long START_TIME = 150000L;
	/**
	 * Choose a move for the given game situation given play time
	 * remaining.  */
	public int chooseMove(MancalaNode node, long timeRemaining) {
		numMovesMade++;
		final int TOTAL_PIECES = 48;
		final double BASE_TIME_FRACTION = .08; // rough guess
		final int MIN_DEPTH_LIMIT = 2;
		final int MAX_DEPTH_LIMIT = 100;

		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
////
////		// We want to have a rough idea of how far we are through
////		// the game both in terms of time and pieces.
////
////		double playLeft = (double) piecesRemaining(node) / TOTAL_PIECES;
////		double timeLeft = (double) timeRemaining / TOTAL_TIME;
////
////		// We want to have a measure of whether we're getting
////		// ahead or behind in our use of time.
////		double adjustmentFactor = timeLeft / playLeft;
//
//		// Finally, we use this measure with an approximate
//		// measure of time per turn to compute a time limit for
//		// this decision.
//		double decisionTime 
//		= adjustmentFactor * TOTAL_TIME * BASE_TIME_FRACTION;
//		decisionTime += 8000;  
		//determine how much time you have for a decision based on average turns left
		double decisionTime = timeRemaining;
		if(numMovesMade < avgMovesLeft.length)
			//don't take all your time for a move
			decisionTime = (avgMovesLeft[numMovesMade] != 1) ?  decisionTime/avgMovesLeft[numMovesMade] : decisionTime/2;
		else
			decisionTime /= 2;
			


		// Create a new copy of the input node in my own node
		// type (with my own evaluation function)
		urBoiMancalaNode searchNode = new urBoiMancalaNode(node);

		// Perform successively deeper searches until we believe
		// we'll exceed our decision time in the next iteration.
		boolean done = false;
		int depthLimit = MIN_DEPTH_LIMIT;
		int bestMove = GameNode.UNDEFINED_MOVE;
		long timeTaken;
		double branchingFactor = 6;
		//less pieces left mean smaller game boad
		if(48 / piecesRemaining(node) < 2)
			branchingFactor = 3;
		else if(48 / piecesRemaining(node) < 3)
			branchingFactor = 2;
		else
			branchingFactor = 1;
		
		while (!done && depthLimit <= MAX_DEPTH_LIMIT) {
			urBoiAlphaBetaSearcher searcher = new urBoiAlphaBetaSearcher(depthLimit);
			searcher.eval(searchNode);
			bestMove = searcher.getBestMove();
			timeTaken = stopwatch.lap();
			// Since the branching factor is <= 6, we conservatively
			// estimate that the next search will take 6 times as long
			// as the searches that have already been done.  If the
			// next search is thus estimated to exceed our allotted
			// search time, we terminate iterative-deepening.
			if (decisionTime / timeTaken < branchingFactor){
//				System.out.println("Time Exceeded!!!!!");
				done = true;
			}
			else{
				depthLimit++;
			}
		}
		
//		System.out.println("Best Depth: " + bestDepth);
//		System.out.println("Decision time: " + (int) decisionTime);
//		System.out.println("Used time: " + stopwatch.lap());
//		System.out.println("Depth Limit: " + depthLimit);
		
		return bestMove;	
	}
	/**
	 * Returns the number of pieces not yet captured.
	 * @return int - uncaptured pieces
	 * @param node MancalaNode - node to check
	 */
	public int piecesRemaining(MancalaNode node) {
		int pieces = 0;
		for (int i = 0; i < 6; i++) pieces += node.state[i];
		for (int i = 7; i < 13; i++) pieces += node.state[i];
		return pieces;
	}

}
