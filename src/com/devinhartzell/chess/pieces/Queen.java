package com.devinhartzell.chess.pieces;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.devinhartzell.chess.board.Board;
import com.devinhartzell.chess.board.Coordinate;

public class Queen extends ChessPiece {
	
	private final String WHITE_PATH = "/resources/pieces/q_w.png";
	private final String BLACK_PATH = "/resources/pieces/q_b.png";
	
	public Queen(int x, int y, boolean color, Board board) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.type = 'q';
		this.board = board;
		if (board.isMainBoard())
			try {
				if (color)
					this.image = ImageIO.read(getClass().getResource(BLACK_PATH));
				else
					this.image = ImageIO.read(getClass().getResource(WHITE_PATH));
			} catch (Exception e){
				System.out.println("Error: Could not load piece resource! " + this.type);
			}
		board.getBoardArray()[x][y].setPiece(this);
	}
	
	@Override
	public ArrayList<Coordinate> getPossibleMoves() {
		ArrayList<Coordinate> movesList = new ArrayList<Coordinate>();
		
		int[] xc = {0, 1, 1, 1, 0, -1, -1, -1};
		int[] yc = {1, 1, 0, -1, -1, -1, 0, 1};
		
		for (int i = 0; i <= xc.length-1; i++) {
			int testx = this.x;
			int testy = this.y;
			
			int xmod = xc[i];
			int ymod = yc[i];
			
			while (true) {
				testx += xmod;
				testy += ymod;
				
				if (testx <= 8 && testx >= 1 && testy <=8 && testy >= 1) {
					if (!board.getBoardArray()[testx][testy].hasPiece()) {
						movesList.add(new Coordinate(testx, testy));
					} else {
						if (board.getBoardArray()[testx][testy].getPiece().getColor() != this.color)
							movesList.add(new Coordinate(testx, testy));
						break;
					}
				} else {
					break;
				}
			}
		} return movesList;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}

	@Override
	public boolean isNull() {
		return false;
	}
	
}