import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

public class Breakout extends JPanel{
	int fps;
	int speed;
	int lives;
	int score;
	Ball ball;
	Paddle paddle;
	ArrayList<Block> blocks;
	
	final static Point MODEL_SIZE = new Point(1280, 720);
	final static Point PADDLE_SIZE = new Point(150,15);
	final static Point BALL_SIZE = new Point(25,25);
	
	public JFrame window;
	
	public Breakout(int fps, int speed) {
		this.fps = fps;
		this.speed = speed;
		this.lives = 3;
		this.score = 0;
		this.initModel();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int fps = 30;
					int speed = 5;
					try {
						fps = Integer.parseInt(args[0]);
						speed = Integer.parseInt(args[1]);
					}
					catch (Exception e) {
						System.err.println("Invalid input parameters! Using defaults");
					}
					JFrame window = new JFrame("Breakout");
					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					window.setSize(1280, 720);
					
					JPanel panel = new JPanel();
					JButton button = new JButton("OK");
					button.addMouseListener(new MouseAdapter(){
						public void mouseClicked(MouseEvent e) {
							System.exit(1);
						}
					});
					panel.add(button);
					window.add(panel);
					window.setContentPane(new Breakout(fps, speed));
					window.getContentPane().setBackground(Color.BLACK);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		this.drawBreakoutObj(g2, this.paddle);
		this.drawBreakoutObj(g2, this.ball);
		for (Block b: this.blocks) {
			this.drawBreakoutObj(g2, b);
		}
	}
	
	private void drawBreakoutObj(Graphics2D g2, BreakoutObj b) {
		Point scaledLocation = getScaledCoordinates(b.getPos());
		Point scaledSize = getScaledCoordinates(b.getSize());
		b.draw(g2, scaledLocation, scaledSize);
	}
	
	private Point getScaledCoordinates(Point p) {
		Double scaledX = p.getX() * this.getWidth() / MODEL_SIZE.x;
		Double scaledY = p.getY() * this.getHeight() / MODEL_SIZE.y;
		return new Point(scaledX.intValue(), scaledY.intValue());
	}
	
	private void initModel() {
		paddle = new Paddle(getInitPaddlePos(), PADDLE_SIZE, Color.GRAY);
		ball = new Ball(getInitBallPos(), BALL_SIZE, 1, Color.RED);
		blocks = new ArrayList<Block>();
		Point blocksStartingPos = getBlocksStaringPos();
		Point blockSize = getBlockSize();
		for (int i = 0; i < 90; i++) {
			int x = i % 15;
			int y = i / 15;
			Point startPos = new Point(blocksStartingPos.x + x *blockSize.x, blocksStartingPos.y + y *blockSize.y);
			blocks.add(new Block(startPos, blockSize, this.getColor(y)));
		}
	}
	
	private Point getInitPaddlePos() {
		int x = MODEL_SIZE.x/2 - PADDLE_SIZE.x/2;
		int y = MODEL_SIZE.y - 30 - PADDLE_SIZE.y;
		return new Point(x, y);
				
	}
	private Point getInitBallPos() {
		int x = MODEL_SIZE.x/2 - BALL_SIZE.x/2;
		int y = MODEL_SIZE.y - 30 - PADDLE_SIZE.y - BALL_SIZE.y;
		return new Point(x, y);
	}
	
	private Point getBlocksStaringPos() {
		int x = MODEL_SIZE.x / 10;
		int y = 3 * MODEL_SIZE.y / 20;
		return new Point(x, y);
				
	}
	
	private Point getBlockSize() {
		int x = 4 * MODEL_SIZE.x / 75;
		int y = 3 * MODEL_SIZE.y / 75;
		return new Point(x, y);
				
	}
	
	private Color getColor(int level) {
		switch(level) {
			case 0: 
				return Color.YELLOW;
			case 1:
				return Color.ORANGE;
			case 2:
				return Color.RED;
			case 3:
				return Color.CYAN;
			case 4:
				return Color.GREEN;
			default:
				return Color.WHITE;
		}
	}
	
}
