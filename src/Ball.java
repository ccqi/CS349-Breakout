import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;

public class Ball extends BreakoutObj{
	private int radius;
	private boolean launched;
	private int vX;
	private int vY;
	
	public Ball(Point location, Point size, int radius, Color color) {
		super(location, size, color);
		this.radius = radius;
		this.launched = false;
		this.vX = 1;
		this.vY = -1;
	}
	
	@Override
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		g2.setColor(this.color);
		g2.fillOval(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
	}
	
	@Override
	public Collision update(Point location) {
		if (!launched) {
			this.location.x = location.x - this.size.x/2;
		}
		return Collision.NONE;
	}
	
	public void set (Point location, Point size, boolean launched) {
		super.set(location, size);
		this.launched = launched;
	}
	
	public Collision update(int increment) {
		if (launched) {
			Point newPos = new Point(this.location);
			newPos.x += increment * vX;
			newPos.y += increment * vY;
			Collision collision = checkBounds(newPos);
			this.changeDirection(collision);
			if (collision == Collision.NONE) {
				this.location = newPos;
			}
			return collision;
		}
		return Collision.NONE;
	}
	
	public void changeDirection(Collision collision) {
		switch (collision) {
		case UP:
		case DOWN:
			vY = -vY;
			break;
		case LEFT:
		case RIGHT:
			vX = -vX;
			break;
		}
	}
	
	public boolean launched() {
		return this.launched;
	}
	
	public void launch() {
		if (!launched) {
			launched = true;
		}
	}
	
}
