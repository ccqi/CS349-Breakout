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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
public class Breakout extends JPanel {
	int fps;
	int speed;
	int lives;
	int score;
	int scoreIncrement;
	int increment;
	
	Ball ball;
	Paddle paddle;
	ArrayList<Block> blocks;
	ArrayList<PowerUp> powerUps;
	
	
	Timer timer;
	
	public final static Point MODEL_SIZE = new Point(1280, 720);
	public final static Point PADDLE_SIZE = new Point(150,15);
	public final static Point BALL_SIZE = new Point(20,20);
	public final static Point BLOCK_SIZE = new Point(65, 20);
	public final static Point POWERUP_SIZE = new Point(30, 30);
	public static ArrayList<BufferedImage> blockImages;
	public static BufferedImage powerUpImage;
	
	
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
			ball.launch();
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
		this.speed = 150 + speed * 50;
		this.lives = 3;
		this.score = 0;
		this.scoreIncrement = 100;
		this.increment = 1;
		if (this.speed > this.fps)
			this.increment = new Double(this.speed/this.fps).intValue();
		this.setFocusable(true);
		this.blockImages = new ArrayList<BufferedImage>();
		try {
			this.powerUpImage = ImageIO.read(new File("assets/powerup.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < 4; i++) {
			try {
				this.blockImages.add(ImageIO.read(new File("assets/block_" + (i+1) + ".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
					int fps = 60;
					int speed = 3;
					try {
						fps = Integer.parseInt(args[0]);
						speed = Integer.parseInt(args[1]);
						if (fps < 25 || fps > 60 || speed < 1 || speed > 5 ) {
							throw new Exception("Parameters out of range");
						}
					}
					catch (Exception e) {
						System.err.println("Invalid input parameters! Using defaults");
						fps = 60;
						speed = 3;
					}
					String[] choices = {"Play Game"};
					String info = "Charles Qi\n205077209\nBreakout\n"+
					"Conquer your inner demons by defeating CS349 in breakout!\n"+
					"Press space/enter/left mouse to launch your ball from the paddle to destroy all the blocks!\n"+
					"Use your mouse to control your paddle and prevent your ball from reaching the ground\n"+
					"Powerups may drop from randomly blocks, some may aid you, but others are placed by TAs/Profs "+
					"to prevent you from reaching your goal\n";
					int result = JOptionPane.showOptionDialog(
					                               null                    
					                             , info    
					                             , "Breakout"            
					                             , JOptionPane.YES_NO_OPTION  
					                             , JOptionPane.PLAIN_MESSAGE 
					                             , null                    
					                             , choices
					                             , null
					                           );
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
		this.scoreIncrement = 100;
		
		if (pb == Collision.UP){
			double dist = ball.getPos().x + ball.getSize().x/2 - (paddle.getPos().x + paddle.getSize().x/2);
			double speed = ball.vX * ball.vX + ball.vY * ball.vY;
			double xVel = dist * 2.5 / paddle.getSize().x;
			double yVel = -Math.sqrt(speed - xVel * xVel); 
			ball.setVel(xVel, yVel);
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
			
			if (!ball.isAcidic()) {
				ball.changeDirection(nearestCollision);
			}

			score+=scoreIncrement;
			scoreIncrement += 100;
			if (removeBlock.getHealth() < 2) {
				if (removeBlock.hasPowerup()) {
					Random rand = new Random();
					powerUps.add(new PowerUp(removeBlock.getPos(), this.POWERUP_SIZE, Color.BLACK, rand.nextInt(3)));
				}
				blocks.remove(removeBlock);
			} else {
				removeBlock.hit();
			}
		}
		
		if (blocks.size() == 0) {
			timer.stop();
			this.showScore(true);
		}
	}
	
	public void update() {
		if (ball.launched()) {
			Collision collision = ball.update(this.increment);
			Iterator<PowerUp> iter = powerUps.iterator();
			while (iter.hasNext()) {
				PowerUp powerUp = iter.next();
				Collision c = powerUp.update(this.increment);
				if (c == Collision.UP) {
					iter.remove();
					this.scoreIncrement = 100;
				} else {
					Collision pp = powerUp.checkCollision(paddle);
					if (pp != Collision.NONE) {
						this.score+=500;
						paddle.addPowerUp(powerUp.getType());
						ball.addPowerUp(powerUp.getType());
						iter.remove();
					}
				}
			}
			
			
			if (collision == Collision.UP) {
				this.reset();
			}
			else if (collision == Collision.NONE) {
				this.checkCollision();
			}
		}
		if (ball.isAcidic()) {
			ball.checkPowerupDuration();
		}
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		for (PowerUp p: this.powerUps) {
			this.drawBreakoutObj(g2, p);
		}
		
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
	
	private void showScore(boolean win) {
		if (win) {
			this.score += 50000 + lives * 10000;
		}
		String msg = win ? "You win!": "You lost!";
		JFrame scoreFrame = new JFrame(msg);
		scoreFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scoreFrame.setSize(320, 300);
		
		JPanel panel = new JPanel();
		JButton no = new JButton("No");
		no.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				System.exit(1);
			}
		});
		JButton yes = new JButton("Yes");
		yes.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				scoreFrame.setVisible(false);
				initModel();
				lives = 3;
				score = 0;
				scoreIncrement = 100;
				timer.start();
			}
		});
		JLabel msgLabel = new JLabel(msg);
		msgLabel.setFont(new Font("IMPACT", Font.BOLD, 48));
		if (win) {
			msgLabel.setForeground(Color.GREEN);
		} else {
			msgLabel.setForeground(Color.RED);
		}
		JLabel scoreLabel = new JLabel("Your final score is: ");
		scoreLabel.setFont(new Font("IMPACT", Font.ITALIC, 28));
		scoreLabel.setForeground(Color.BLACK);
		JLabel scoreDisplay = new JLabel(Integer.toString(this.score));
		scoreDisplay.setFont(new Font("IMPACT", Font.BOLD, 48));
		scoreDisplay.setForeground(Color.BLACK);
		JLabel playAgain = new JLabel("Would you like to play again?");
		playAgain.setFont(new Font("IMPACT", Font.PLAIN, 18));
		playAgain.setForeground(Color.BLACK);
		panel.add(msgLabel);
		panel.add(scoreLabel);
		panel.add(scoreDisplay);
		panel.add(playAgain);
		panel.add(yes);
		panel.add(no);
		scoreFrame.add(panel);
		scoreFrame.getContentPane().setBackground(Color.BLACK);
		scoreFrame.setVisible(true);
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
		ball = new Ball(getInitBallPos(), BALL_SIZE, Color.RED);
		blocks = new ArrayList<Block>();
		powerUps = new ArrayList<PowerUp>();
		ArrayList<Point> points = readFile();
		for (Point point: points) {
			Random rand = new Random();
			blocks.add(new Block(point, BLOCK_SIZE, this.getColor(rand.nextInt(5)), rand.nextInt(3)+1));
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
			this.showScore(false);
		} else {
			this.lives--;
		}
	}
	
	private ArrayList<Point> readFile() {
		ArrayList<Point> blockLocation = new ArrayList<Point>();
		try {
            BufferedReader in = new BufferedReader(new FileReader("blocks.txt"));
            String str;
            HashMap<Point, Boolean> exist = new HashMap<Point, Boolean>();
            while ((str = in.readLine()) != null) {
                try {
                	String[] ar=str.split(",");
                	Point p = new Point(Integer.parseInt(ar[0].trim()), Integer.parseInt(ar[1].trim()));
                	if (exist.containsKey(p)) {
                		System.out.println("Duplicate" + p);
                	} else {
                		exist.put(p, true);
                		blockLocation.add(p);
                	}
                	
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
