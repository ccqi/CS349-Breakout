import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class BreakoutObj {
	Point location;
	Point size;
	Color color;
	
	public BreakoutObj(Point location, Point size, Color color) {
		this.location = location;
		this.size = size;
		this.color = color;
	}
	public Point getPos() {
		return this.location;
	}
	public Point getSize() {
		return this.size;
	}
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		g2.setColor(this.color);
		g2.fillRect(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
	}
}
