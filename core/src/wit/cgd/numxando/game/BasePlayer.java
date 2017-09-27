/**
 * @file        BasePlayer.java
 * @author      Aaron Mooney 20072163
 * @assignment  Numerical X and O
 * @brief       The player superclass
 *
 * @notes       
 * 				
 */
package wit.cgd.numxando.game;

public abstract class BasePlayer {
	public boolean human;
	public int mySymbol, opponentSymbol;
	public String name;
	public Board board;
	public int skill;
	public int number;

	
	public BasePlayer(Board board, int symbol) {
		this.board = board;
		setSymbol(symbol);
		human = false;
	}
	
	public void setSymbol(int symbol) {
		mySymbol = symbol;
		opponentSymbol = (symbol == board.EVEN) ? board.ODD : board.EVEN;
	}
	
	public abstract int move ();
	
}
