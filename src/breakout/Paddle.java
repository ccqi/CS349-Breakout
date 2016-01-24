package breakout;
import java.awt.Color;
import java.awt.Point;

public class Paddle extends BreakoutObj{
	public Paddle(Point location, Point size, Color color) {
		super(location, size, color);
	}
	
	@Override
	public Collision update(Point location) {
		int newX = location.x - this.size.x/2;
		Collision collision = checkBounds(newX);
		if (collision == Collision.NONE) {
			this.location.x = newX;
		}
		return collision;
	}
}
