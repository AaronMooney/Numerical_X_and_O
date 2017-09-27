/**
 * @file        HumanPlayer.java
 * @author      Aaron Mooney 20072163
 * @assignment  Numerical X and O
 * @brief       Human player object
 *
 * @notes       
 * 				
 */
package wit.cgd.numxando.game;

public class HumanPlayer extends BasePlayer {

	@SuppressWarnings("unused")
	private static final String	TAG	= WorldRenderer.class.getName(); 

	public HumanPlayer(Board board, int symbol) {
		super(board, symbol);
		human = true;
		name = "Electrified Meat";
	}

	@Override
	public int move() {
		// human move handled in worldController
		return 0;
	}

}
