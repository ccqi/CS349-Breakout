package breakout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.*;
public class Breakout extends JPanel {
	int fps;
	int speed;
	int lives;
	int score;
	int increment;
	
	Ball ball;
	Paddle paddle;
	ArrayList<Block> blocks;
	
	Timer timer;
	
	public final static Point MODEL_SIZE = new Point(1280, 720);
	public final static Point PADDLE_SIZE = new Point(150,15);
	public final static Point BALL_SIZE = new Point(20,20);
	public final static Point BLOCK_SIZE = new Point(65, 20);
	
	public JFrame window;
	
	public class MouseEvents extends MouseAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			Point newPos = getModelCoordinates(e.getPoint());
			if (paddle.update(newPos) == Collision.NONE && !ball.launched()) {
				ball.update(newPos);
			}
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			//ball.launch();
			if (timer.isRunning()){
				timer.stop();
			} else {
				timer.start();
			}
		}
	}
	
	public class KeyEvents extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch(keyCode) {
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_ENTER:
					ball.launch();
					
			}
		}
	}
	
	public Breakout(int fps, int speed) {
		this.fps = fps;
		this.speed = 200 + speed * 50;
		this.lives = 3;
		this.score = 0;
		this.increment = 1;
		if (this.speed > this.fps)
			this.increment = new Double(this.speed/this.fps).intValue();
		this.setFocusable(true);
		MouseAdapter mouseEvents = new MouseEvents();
		
		timer = new Timer(1000/this.fps, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		
		timer.start();
		KeyAdapter keyEvents = new KeyEvents();
		this.addMouseListener(mouseEvents);
		this.addMouseMotionListener(mouseEvents);
		this.addKeyListener(keyEvents);
		
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		this.setCursor(blankCursor);
		this.initModel();
	}
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int fps = 30;
					int speed = 2;
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
	
	public void checkCollision() {
		Collision pb = ball.checkCollision(paddle);
		while (ball.checkCollision(paddle) == Collision.UP) {
			ball.changeDirection(pb);
			ball.update(this.increment);
		}
		Iterator<Block> iter = blocks.iterator();
		double minDist = MODEL_SIZE.x * MODEL_SIZE.x + MODEL_SIZE.y + MODEL_SIZE.y ;
		Collision nearestCollision = Collision.NONE;
		Block removeBlock = null;
		while (iter.hasNext()) {
			Block block = iter.next();
			Collision bc = ball.checkCollision(block);
			double distance = ball.getDistance(block);
			if (bc != Collision.NONE && distance < minDist) {
				minDist = distance;
				nearestCollision = bc;
				removeBlock = block;
			}
			
			
		}
		if (removeBlock != null && nearestCollision != Collision.NONE) {
			//System.out.println(nearestCollision);
			ball.changeDirection(nearestCollision);
			//timer.stop();
			score+=15;
			if (removeBlock.getHealth() < 2){
				blocks.remove(removeBlock);
			} else {
				removeBlock.hit();
			}
			//timer.stop();
		}
		
		if (blocks.size() == 0) {
			timer.stop();
		}
	}
	
	public void update() {
		if (ball.launched()) {
			Collision collision = ball.update(this.increment);
			if (collision == Collision.DOWN) {
				this.reset();
			}
			if (collision == Collision.NONE) {
				this.checkCollision();
			}
		}
		 this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		for (Block b: this.blocks) {
			this.drawBreakoutObj(g2, b);
		}
		this.drawBreakoutObj(g2, this.paddle);
		this.drawBreakoutObj(g2, this.ball);
		this.drawString(g2, "FPS: " + this.fps, Color.YELLOW, new Point(30,40), "IMPACT", Font.BOLD, 18);
		this.drawString(g2, "Score: " + this.score, Color.WHITE, new Point(30,70),"IMPACT", Font.BOLD, 18);
		this.drawString(g2, "Lives: " + this.lives, Color.WHITE, new Point(30,100),"IMPACT", Font.BOLD, 18);
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void drawBreakoutObj(Graphics2D g2, BreakoutObj b) {
		Point scaledLocation = getScaledCoordinates(b.getPos());
		Point scaledSize = getScaledCoordinates(b.getSize());
		b.draw(g2, scaledLocation, scaledSize);
	}
	
	private void drawString(Graphics2D g2, String s, Color color, Point location, String type, int style, int size) {
		Double scaledFactor = new Double((this.getWidth() * this.getHeight()) /
				(MODEL_SIZE.x * MODEL_SIZE.y));
		int scaledSize = new Double(size * scaledFactor).intValue();
		scaledSize = (scaledSize < 15) ? 15: scaledSize;
		System.out.println(scaledSize);
		Point scaledLocation = getScaledCoordinates(location);
		g2.setColor(color);
		g2.setFont(new Font(type, style, scaledSize));
		g2.drawString(s, scaledLocation.x, scaledLocation.y);
	}
	
	private Point getScaledCoordinates(Point p) {
		Double scaledX = p.getX() * this.getWidth() / MODEL_SIZE.x;
		Double scaledY = p.getY() * this.getHeight() / MODEL_SIZE.y;
		return new Point(scaledX.intValue(), scaledY.intValue());
	}
	
	private Point getModelCoordinates(Point p) {
		Double scaledX = p.getX() * MODEL_SIZE.x / this.getWidth();
		Double scaledY = p.getY() * MODEL_SIZE.y / this.getHeight();
		return new Point(scaledX.intValue(), scaledY.intValue());
	}
	
	private void initModel() {
		paddle = new Paddle(getInitPaddlePos(), PADDLE_SIZE, Color.GRAY);
		ball = new Ball(getInitBallPos(), BALL_SIZE, 1, Color.RED);
		blocks = new ArrayList<Block>();
		Point blocksStartingPos = getBlocksStaringPos();
		Point blockSize = getBlockSize();
		ArrayList<Point> points = readFile();
		for (Point point: points) {
			Random rand = new Random();
			blocks.add(new Block(point, BLOCK_SIZE, this.getColor(rand.nextInt(5)), rand.nextInt(4)+1, true));
		}
	}
	
	private void reset() {
		paddle.set(getInitPaddlePos(), PADDLE_SIZE);
		ball.set(getInitBallPos(), BALL_SIZE, false);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this.lives == 0 ){
			timer.stop();
		} else {
			this.lives--;
		}
	}
	
	private ArrayList<Point> readFile() {
		ArrayList<Point> blockLocation = new ArrayList<Point>();
		try {
            BufferedReader in = new BufferedReader(new FileReader("src/blocks.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
                try {
                	String[] ar=str.split(",");
                	Point p = new Point(Integer.parseInt(ar[0].trim()), Integer.parseInt(ar[1].trim()));
                	blockLocation.add(p);
                }
                catch (Exception e) {
                	 
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return blockLocation;
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
