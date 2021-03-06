package com.devinhartzell.chess.board;

import javax.swing.JOptionPane;

import com.devinhartzell.chess.ChessGame;
import com.devinhartzell.chess.gui.BoardPanel;
import com.devinhartzell.chess.gui.ChessGameWindow;
import com.devinhartzell.chess.pieces.Bishop;
import com.devinhartzell.chess.pieces.ChessPiece;
import com.devinhartzell.chess.pieces.King;
import com.devinhartzell.chess.pieces.Knight;
import com.devinhartzell.chess.pieces.NullPiece;
import com.devinhartzell.chess.pieces.Pawn;
import com.devinhartzell.chess.pieces.Queen;
import com.devinhartzell.chess.pieces.Rook;

/*
 * Wrapper for the boardArray squares
 */
public class Board {
	
	private Square[][] boardArray = new Square[9][9];
	
	private King whiteKing;
	private King blackKing;

	private BoardPanel boardPanel;
	
	private boolean check_w = false;
	private boolean check_b = false;
	
	private boolean mainBoard = false;
	
	/*
	 * Tracks who has the next move
	 * False = white
	 * True = black
	 */
	public  boolean currentMove = false;
	
	public Board() {
		this(false);
	}
	
	public Board(Board base) {
		this(false);
		for (int i = 1; i<=8; i++) {
			for (int j = 1; j<=8; j++) {
				if (base.getBoardArray()[i][j].getPiece() instanceof Pawn) 
					new Pawn(i,j, base.getBoardArray()[i][j].getPiece().getColor(), this);
				else if (base.getBoardArray()[i][j].getPiece() instanceof Knight)
					new Knight(i,j, base.getBoardArray()[i][j].getPiece().getColor(), this);
				else if (base.getBoardArray()[i][j].getPiece() instanceof Bishop)
					new Bishop(i,j, base.getBoardArray()[i][j].getPiece().getColor(), this);
				else if (base.getBoardArray()[i][j].getPiece() instanceof Rook)
					new Rook(i,j, base.getBoardArray()[i][j].getPiece().getColor(), this);
				else if (base.getBoardArray()[i][j].getPiece() instanceof Queen)
					new Queen(i,j, base.getBoardArray()[i][j].getPiece().getColor(), this);
				else if (base.getBoardArray()[i][j].getPiece() instanceof King)
					if (!base.getBoardArray()[i][j].getPiece().getColor())
						whiteKing = new King(i,j, base.getBoardArray()[i][j].getPiece().getColor(), this);
					else
						blackKing = new King(i,j, base.getBoardArray()[i][j].getPiece().getColor(), this);
			}
		}
	}
	
	public Board(boolean mainBoard) {
		
		this.mainBoard = mainBoard;
		

		
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				Square sq = new Square(i, j, this);
				getBoardArray()[i][j] = sq;
			}
		}
		
		if (mainBoard) {
			ChessGame.setMainBoard(this);
			boardPanel = new BoardPanel();
			boardPanel.setLocation(6, 6);
			for (int i = 1; i <= 8; i++) {
				for (int j = 1; j<= 8; j++) {
					getBoardArray()[i][j].setPanel(boardPanel.getPanelArray()[i][j]);
				}
			}
		
			for (int i = 1; i<=8; i++) {
				new Pawn(i, 2, true, this);
				new Pawn(i, 7, false, this);
			}
			
			new Rook(1, 1, true, this);
			new Rook(8, 1, true, this);
			new Rook(1, 8, false, this);
			new Rook(8, 8, false, this);
			
			
			new Queen(4, 8, false, this);
			new Queen(4, 1, true, this);
			
			new Knight(2, 1, true, this);
			new Knight(7, 1, true, this);
			new Knight(2, 8, false, this);
			new Knight(7, 8, false, this);
			
			new Bishop(3, 1, true, this);
			new Bishop(6, 1, true, this);
			new Bishop(3, 8, false, this);
			new Bishop(6, 8, false, this);
			
			
			whiteKing = new King(5, 8, false, this);
			blackKing = new King(5, 1, true, this);
			
		}
	}
	
	public void duplicate(Board board) {
		for (int i = 1; i<=8; i++) {
			for (int j = 1; j<=8; j++) {
				getBoardArray()[i][j].setPiece(board.getBoardArray()[i][j].getPiece());
			}
		}
	}
	
	public  ChessPiece getPieceAt(int x, int y) {
		if (getBoardArray()[x][y].hasPiece())
			return getBoardArray()[x][y].getPiece();
		else
			return null;
	}

	public  Square[][] getBoardArray() {
		return boardArray;
	}
	
	public  boolean getTurn() {
		return currentMove;
	}
	
	public void setTurn(boolean b) {
		currentMove = b;
		ChessGameWindow.nextMove();
		
		checkEndGame();	
	}
	
	public void checkEndGame() {
		if (whiteKing.getCheck()) {
			
			if (whiteKing.getCheckMate()) {
				JOptionPane.showMessageDialog(null, "White is in CheckMate. Black wins!");
				ChessGameWindow.append("##");
				ChessGame.endGame();
			} else {
				JOptionPane.showMessageDialog(null, "White is now in Check!");
				ChessGameWindow.append("#");
			}
		} if (blackKing.getCheck()) {
			if (blackKing.getCheckMate()) {
				JOptionPane.showMessageDialog(null, "Black is now in CheckMate. White wins!");
				ChessGame.endGame();
			} else 
				JOptionPane.showMessageDialog(null, "Black is now in Check!");
		}
	}
	
	public King getWKing() {
		return whiteKing;
	}
	
	public King getBKing() {
		return blackKing;
	}
	
	public boolean getCheck(boolean b) {
		if (b) 
			return check_b;
		else
			return check_w;
	}
	
	public void updatePanel() {
		if (mainBoard)
			BoardPanel.updateBoard();
	}
	
	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public boolean isMainBoard() {
		return mainBoard;
	}
	
	public void print() {
		System.out.println("Printing Board:");
		for (int i = 1; i<=8; i++) {
			String line = "";
			for (int j = 1; j<=8; j++) {
				char type = getBoardArray()[i][j].getPiece().getType();
				if (getBoardArray()[i][j].getPiece().getColor())
					type = Character.toUpperCase(type);
				if (getBoardArray()[i][j].getPiece() instanceof NullPiece)
					type = '.';
				line = line + type;
			}
			System.out.println(i + " " + line);
		}
	}
}