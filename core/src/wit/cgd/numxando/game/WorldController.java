/**
 * @file        WorldController.java
 * @author      Aaron Mooney 20072163
 * @assignment  Numerical X and O
 * @brief       handles player input
 *
 * @notes       
 * 				
 */
package wit.cgd.numxando.game;
import wit.cgd.numxando.game.ai.MinimaxPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Game;
import wit.cgd.numxando.screens.MenuScreen;

public class WorldController extends InputAdapter {

	private static final String TAG = WorldRenderer.class.getName();

	public float viewportWidth;
	public int width, height;
	public Board board;
	private Game game;
	boolean dragging = false;
	int dragX, dragY;
	public TextureRegion dragRegion;
	public TextureRegion undoButton;
	final float TIME_LEFT_GAME_OVER_DELAY = 2;
	float timeLeftGameOverDelay;

	final int GAME_COUNT = 100;
	public int gameCount = 0;
	public int win = 0, draw = 0, loss = 0;

	public WorldController(Game game) {
		this.game = game;
		init();
	}

	private void backToMenu() {
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}

	private void init() {
		Gdx.input.setInputProcessor(this);
		board = new Board();

		// Players:
		// MinimaxPlayer
		board.firstPlayer = new HumanPlayer(board, board.ODD);
		board.secondPlayer = new MinimaxPlayer(board, board.EVEN);

		timeLeftGameOverDelay = TIME_LEFT_GAME_OVER_DELAY;
		board.start();
	}

	public void update(float deltaTime) {
		if (board.gameState == Board.GameState.PLAYING) {
			board.move();
		} else {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) {
				gameCount++;
				if (board.gameState == Board.GameState.EVEN_WON) {
					win++;
				} else if (board.gameState == Board.GameState.ODD_WON) {
					loss++;
				} else {
					assert (board.gameState == Board.GameState.DRAW);
					draw++;
				}

				if (gameCount == GAME_COUNT) {
					Gdx.app.log(TAG,
							"\nPlayeed " + gameCount + " games \t" + board.firstPlayer.name + " vs "
									+ board.secondPlayer.name + "\n\t Even  win \t" + win + "\t draw \t" + draw
									+ "\t Odd win \t" + loss);
					Gdx.app.exit();
				}
				board.start();
				timeLeftGameOverDelay = TIME_LEFT_GAME_OVER_DELAY;
				board.gameState = Board.GameState.PLAYING;
			}
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			backToMenu();
		}
		if (keycode == Keys.R){
			undo();
		}
		if (keycode == Keys.H){
			hint();
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (board.gameState == Board.GameState.PLAYING && board.currentPlayer.human) {

			// convert to cell position
			int row = 5 * (height - screenY) / height;
			int col = (int) (viewportWidth * (screenX - 0.5 * width) / width) + 1;

			dragX = screenX;
			dragY = screenY;

			// check if valid start of a drag for second player
			if (col == -3 && board.currentPlayer == board.secondPlayer) {
				for (int i = 0; i < 4; i++) {
					if (row == i) {
						dragging = true;
						dragRegion = Assets.instance.numbers.get((i * 2 + 1)).region;
						return true;
					}
				}
			}
			// check if valid start of a drag for first player
			if (col == -1 && board.currentPlayer == board.firstPlayer) {
				for (int i = 0; i < 5; i++) {
					if (row == i) {
						dragging = true;
						dragRegion = Assets.instance.numbers.get(i * 2).region;
						return true;
					}
				}
			}

		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		dragX = screenX;
		dragY = screenY;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		dragging = false;

		// convert to cell position
		int row = 4 * (height - screenY) / height;
		int col = (int) (viewportWidth * (screenX - 0.5 * width) / width) + 1;
		int value = -1;

		for (int i = 0; i < 9; i++) {
			if (dragRegion.equals(Assets.instance.numbers.get(i).region)) {
				value = i + 1;
			}
		}

		// if a valid board cell then place piece
		if (row >= 0 && row < 3 && col >= 0 && col < 3) {
			if (!board.numsUsed.contains(value)) {
				board.move(row, col, value);
				return true;
			}
		}

		return true;
	}
	
	private void undo(){
		if(board.moves.size() < 2) return;
		for (int i = 0; i < 2; i++){
			int pos = board.moves.pop();
			board.cells[pos/3][pos%3] = board.EMPTY;
			board.numsUsed.remove(board.numsUsed.size()-1);
		}
	}
	
	private void hint(){
		MinimaxPlayer hint = new MinimaxPlayer(board,board.currentPlayer.mySymbol);
		hint.skill=3;
		int move = hint.move();
		int row = move/3;
		int col = move%3;
		String hintMessage = "Put " + hint.number + " in row " + row + " , col " + col;
		System.out.println(hintMessage);
	}

}
