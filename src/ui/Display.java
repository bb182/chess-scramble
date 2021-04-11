package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Display extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private int[] board = new int[64];
	private BufferedImage boardImg = null;
	private BufferedImage highlightImg = null;
	private Map<Integer, BufferedImage> sprites = new HashMap<>();
	
	private Set<Integer> markedSquare = new HashSet<>();
	
	public Display() {
		loadImgs();
		
		JFrame frame = buildFrame();

	    frame.add(this);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(boardImg, 0, 0, null);
        
        for(int square : markedSquare) {
        	g.drawImage(highlightImg, square%8*64, square/8*64, null);
        }
        
        for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				int piece = board[x + y*8];
				if(piece > 0) {
					g.drawImage(sprites.get(piece), x*64, y*64, null);
				}
			}
		}
    }
	
	public void updateBoard(int[] ChessBoard) {
		board = ChessBoard;
	}
	
	private void loadImgs() {
		try {
		    boardImg = ImageIO.read(new File("res/sprites/Board.png"));
		    BufferedImage pieces = ImageIO.read(new File("res/sprites/Pieces.png"));
		    highlightImg = pieces.getSubimage(192, 0, 64, 64);
		    sprites.put(1, pieces.getSubimage(0, 0, 64, 64));
		    sprites.put(2, pieces.getSubimage(64, 0, 64, 64));
		    sprites.put(3, pieces.getSubimage(128, 0, 64, 64));
		    sprites.put(4, pieces.getSubimage(0, 64, 64, 64));
		    sprites.put(5, pieces.getSubimage(64, 64, 64, 64));
		    sprites.put(6, pieces.getSubimage(128, 64, 64, 64));
		    sprites.put(9, pieces.getSubimage(0, 128, 64, 64));
		    sprites.put(10, pieces.getSubimage(64, 128, 64, 64));
		    sprites.put(11, pieces.getSubimage(128, 128, 64, 64));
		    sprites.put(12, pieces.getSubimage(0, 192, 64, 64));
		    sprites.put(13, pieces.getSubimage(64, 192, 64, 64));
		    sprites.put(14, pieces.getSubimage(128, 192, 64, 64));
		} catch (IOException e) {
			System.err.println("Faild to read PNG!");
		}
	}


	private static JFrame buildFrame() {
	    JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.setSize(64*8, 64*8);
	    frame.setVisible(true);
	    return frame;
	}

	public void mark(int x) {
		markedSquare.add(x);
		this.updateUI();
	}

	public void clearMarked() {
		markedSquare.clear();
		this.updateUI();
	}

}
