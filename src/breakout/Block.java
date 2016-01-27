package breakout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Block extends BreakoutObj{
	
	private int health;
	private boolean breakable;
	
	public Block(Point location, Point size, Color color, int health, boolean breakable) {
		super(location, size, color);
		
		this.breakable = breakable;
		this.health = this.breakable? health: 1000;
		
	}
	
	public void hit() {
		if (this.breakable) {
			health--;
		}
	}
	
	public int getHealth() {
		return this.health;
	}
	
	@Override
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		super.draw(g2, scaledLocation, scaledSize);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.BLACK);
		
		if (this.health > 1) {
			g2.drawString(Integer.toString(this.health), scaledLocation.x + 10, scaledLocation.y + 10);
		}
		if (!this.breakable) {
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.GRAY);
			g2.fillRect(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
		}
		g2.drawRect(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
		
	}
}
