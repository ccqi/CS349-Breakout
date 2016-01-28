package breakout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Block extends BreakoutObj{
	
	private int health;
	private boolean breakable;
	private boolean hasPowerUp;
	private BufferedImage image;
	
	public Block(Point location, Point size, Color color, int health, boolean breakable) {
		super(location, size, color);
		Random rand = new Random();
		this.breakable = breakable;
		this.health = this.breakable? health: 1000;
		this.image = Breakout.blockImages.get(this.health -1);
		this.hasPowerUp = rand.nextInt(5) == 0;	
	}
	
	public void hit() {
		if (this.breakable) {
			health--;
			this.image = Breakout.blockImages.get(health -1);
		}
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public boolean hasPowerup() {
		return this.hasPowerUp;
	}
	
	@Override
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		//super.draw(g2, scaledLocation, scaledSize);
		g2.setColor(this.color);
		//g2.fillRect(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
		g2.drawImage(this.image, scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y, null);
		//g2.setStroke(new BasicStroke(2));
		//g2.setColor(Color.BLACK);
		
		if (this.health > 1) {
			//g2.drawString(Integer.toString(this.health), scaledLocation.x + 10, scaledLocation.y + 10);
		}
		if (!this.breakable) {
			//g2.setStroke(new BasicStroke(5));
			//g2.setColor(Color.GRAY);
			//g2.fillRect(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
		}
		//g2.drawRect(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
		
	}
}
