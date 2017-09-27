/**
 * @file        Board.java
 * @author      Aaron Mooney 20072163
 * @assignment  Numerical X and O
 * @brief       Handles all data related to the board such as win/loss conditions and moves
 *
 * @notes       
 * 				
 */
package wit.cgd.numxando.game;

import wit.cgd.numxando.game.util.AudioManager;
import wit.cgd.numxando.game.util.Constants;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Board {

	@SuppressWarnings("unused")
	private static final String TAG = WorldRenderer.class.getName();

	public static enum GameState {
		PLAYING, DRAW, EVEN_WON, ODD_WON
	}

	public GameState gameState;
	public final int EMPTY = 0;
	public final int EVEN = 2;
	public final int ODD = 1;
	
	public int[][] cells = new int[3][3];

	public BasePlayer firstPlayer, secondPlayer;
	public BasePlayer currentPlayer;
	public ArrayList<Integer> numsUsed = new ArrayList<Integer>();
	public Stack<Integer> moves;

	public Board() {
		init();
	}

	public void init() {
		moves = new Stack<Integer>();
		start();
	}

	public void start() {
		for (int r = 0; r < 3; r++)
			for (int c = 0; c < 3; c++)
				cells[r][c] = EMPTY;
		numsUsed = new ArrayList<Integer>();

		gameState = GameState.PLAYING;
		currentPlayer = firstPlayer;
	}

	public boolean move() {
		return move(-1, -1, -1);
	}

	public boolean move(int row, int col, int value) {

		if (currentPlayer.human) {
			if (row < 0 || col < 0 || row > 2 || col > 2 || cells[row][col] != EMPTY)
				return false;
		} else { // computer player
			int pos = currentPlayer.move();
			value = currentPlayer.number;
			AudioManager.instance.play(Assets.instance.sounds.second);
			col = pos % 3;
			row = pos / 3;
		}

		System.out.println(" " + currentPlayer.human + " " + row + " " + col);
		// store move
		cells[row][col] = value;
		moves.push(row*3+col);
		numsUsed.add(value);
		if (currentPlayer.equals(firstPlayer)){
			AudioManager.instance.play(Assets.instance.sounds.first);
		}
		else {
			AudioManager.instance.play(Assets.instance.sounds.second);
		}
		if (hasWon(currentPlayer.mySymbol, row, col)) {
			AudioManager.instance.play(Assets.instance.sounds.win);
			gameState = currentPlayer.mySymbol == EVEN ? GameState.EVEN_WON : GameState.ODD_WON;
		} else if (isDraw()) {
			AudioManager.instance.play(Assets.instance.sounds.draw);
			gameState = GameState.DRAW;
		}

		// switch player
		if (gameState == GameState.PLAYING) {
			currentPlayer = (currentPlayer.equals(firstPlayer) ? secondPlayer : firstPlayer);
		}

		return true;
	}

	public boolean isDraw() {
		for (int r = 0; r < 3; ++r) {
			for (int c = 0; c < 3; ++c) {
				if (cells[r][c] == EMPTY) {
					return false; // an empty seed found, not a draw, exit
				}
			}
		}
		return true; // no empty cell, it's a draw
	}

	public boolean hasWon(int symbol, int row, int col) {
		return (
				// 3-in-the-row
				cells[row][0] + cells[row][1] + cells[row][2] == Constants.WINNING_NUMBER &&
				isOccupied(cells[row][0],cells[row][1],cells[row][2])
				||  // 3-in-the-column
				cells[0][col] + cells[1][col] + cells[2][col] == Constants.WINNING_NUMBER &&
				isOccupied(cells[0][col],cells[1][col],cells[2][col])
				||  // 3-in-the-diagonal
				cells[0][0] + cells[1][1] + cells[2][2] == Constants.WINNING_NUMBER &&
				isOccupied(cells[0][0],cells[1][1],cells[2][2])
				|| // 3-in-the-opposite-diagonal
				cells[0][2] + cells[1][1] + cells[2][0] == Constants.WINNING_NUMBER &&
				isOccupied(cells[0][2],cells[1][1],cells[2][0])
				);
	}
	
	public boolean isOccupied(int first,int second,int third){
		if(first == EMPTY)return false;
		else if(second == EMPTY)return false;
		else if(third == EMPTY)return false;
		else return true;
	}

	public void render(SpriteBatch batch) {
		TextureRegion region = Assets.instance.board.region;
		batch.draw(region.getTexture(), -2, -Constants.VIEWPORT_HEIGHT / 2 + 0.1f, 0, 0, 4, 4, 1, 1, 0,
				region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false,
				false);

		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++) {
				if (cells[row][col] == EMPTY)
					continue;
				region = Assets.instance.numbers.get(cells[row][col]-1).region;
				batch.draw(region.getTexture(), col * 1.4f - 1.9f, row * 1.4f - 2.3f, 0, 0, 1, 1, 1, 1, 0,
						region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
						false, false);
			}

		
		//drag and drop pieces
		int rowPos = -2;
		//even
		for (int i = 0; i < 9; i++) {
			if (i % 2 == 0) {
				rowPos ++;
				if (numsUsed.contains(i+1)) continue;
				region = Assets.instance.numbers.get(i).region;
				batch.draw(region.getTexture(), (-1) * 1.4f - 1.9f, rowPos - 1.5f, 0, 0, 1, 1, 1, 1, 0,
						region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
						false, false);
			}
		}
		//odd
		rowPos = -2;
		
		for (int i = 0; i < 9; i ++){
			if(i%2!=0){
				rowPos ++;
				if (numsUsed.contains(i+1)) continue;
				region = Assets.instance.numbers.get(i).region;
				batch.draw(region.getTexture(), (3) * 1.4f - 1.9f, rowPos - 1.5f, 0, 0, 1, 1, 1, 1, 0, region.getRegionX(),
						region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
			}
		}

	}
	
	public ArrayList<Integer> available(int symbol){
		ArrayList<Integer> notUsed = new ArrayList<Integer>();
		int[] even = {2,4,6,8};
		int[]odd = {1,3,5,7,9};
		
		if(symbol == EVEN){
			for (int i : even){
				if (!numsUsed.contains(i)){
					notUsed.add(i);
				}
			}
		} else{
			for (int i : odd){
				if (!numsUsed.contains(i)){
					notUsed.add(i);
				}
			}
		}
		return notUsed;
	}

}
