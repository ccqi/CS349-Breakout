package breakout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Block extends BreakoutObj{
	public Block(Point location, Point size, Color color) {
		super(location, size, color);
	}
	
	@Override
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		super.draw(g2, scaledLocation, scaledSize);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.BLACK);
		g2.drawRect(scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y);
	}
}
