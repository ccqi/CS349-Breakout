package breakout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Block extends BreakoutObj{
	
	private int health;
	private boolean hasPowerUp;
	private BufferedImage image;
	
	public Block(Point location, Point size, Color color, int health) {
		super(location, size, color);
		Random rand = new Random();
		this.health = health;
		this.image = Breakout.blockImages.get(this.health -1);
		this.hasPowerUp = rand.nextInt(5) == 0;	
	}
	
	public void hit() {
		this.health--;
		this.image = Breakout.blockImages.get(this.health -1);
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public boolean hasPowerup() {
		return this.hasPowerUp;
	}
	
	@Override
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		g2.setColor(this.color);
		g2.drawImage(this.image, scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y, null);
	}
}
