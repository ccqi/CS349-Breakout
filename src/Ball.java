import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Ball extends BreakoutObj{
	private int radius;
	public Ball(Point location, Point size, int radius, Color color) {
		super(location, size, color);
		this.radius = radius;
	}
	@Override
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		g2.setColor(this.color);
		g2.fillOval(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
	}
}
