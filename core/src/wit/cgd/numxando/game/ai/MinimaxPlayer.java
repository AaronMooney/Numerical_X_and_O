/**
 * @file        MinimaxPlayer.java
 * @author      Aaron Mooney 20072163
 * @assignment  Numerical X and O
 * @brief       A player that chooses moves using the minimax algorithm
 *
 * @notes       
 * 				
 */
package wit.cgd.numxando.game.ai;

import java.util.Random;

import wit.cgd.numxando.game.BasePlayer;
import wit.cgd.numxando.game.Board;

public class MinimaxPlayer extends BasePlayer {

	private Random randomGenerator;

	public MinimaxPlayer(Board board, int symbol) {
		super(board, symbol);
		name = "MinimaxPlayer";

		skill = 3; // skill is measure of search depth

		randomGenerator = new Random();
	}

	@Override
	public int move() {
		return (int) minimax(mySymbol, opponentSymbol, 0);
	}

	private float minimax(int p_mySymbol, int p_opponentSymbol, int depth) {

		final float WIN_SCORE = 100;
		final float DRAW_SCORE = 0;

		float score;
		float maxScore = -10000;
		int maxPos = -1;
		int numberChosen = -1;

		// for each number
		for (int i : board.available(p_mySymbol)) {
			for (int r = 0; r < 3; ++r) {
				for (int c = 0; c < 3; ++c) {

					// skip over used positions
					if (board.cells[r][c] != board.EMPTY)
						continue;

					// place move
					board.cells[r][c] = i;

					// evaluate board (recursively)
					if (board.hasWon(p_mySymbol, r, c)) {
						score = WIN_SCORE;
					} else if (board.isDraw()) {
						score = DRAW_SCORE;
					} else {
						if (depth < skill) {
							score = -minimax(p_opponentSymbol, p_mySymbol, depth + 1);
						} else {
							score = 0;
						}
					}

					// update ranking

					if (Math.abs(score - maxScore) < 1.0E-5 && randomGenerator.nextDouble() < 0.1) {
						maxScore = score;
						maxPos = 3 * r + c;
						numberChosen = i;

					} else if (score > maxScore) { // clear
						maxScore = score;
						maxPos = 3 * r + c;
						numberChosen = i;
					}


					// undo move
					board.cells[r][c] = board.EMPTY;

				}
			}
		}
		number = numberChosen;

		// on uppermost call return move not score
		return (depth == 0 ? maxPos : maxScore);

	};

}
