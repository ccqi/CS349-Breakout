package breakout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
enum Collision {
	UP, DOWN, LEFT, RIGHT, NONE
}

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
	
	public void set(Point location, Point size) {
		this.location = location;
		this.size = size;
	}
	
	public Collision update(Point location) {
		Collision collision = checkBounds(location);
		if (collision == Collision.NONE) {
			this.location = location;
		}
		return collision;
	}
	
	public Collision checkBounds(Point p) {
		if (p.x < 0)
			return Collision.RIGHT;
		else if (p.x + this.size.x >= Breakout.MODEL_SIZE.x)
			return Collision.LEFT;
		else if (p.y < 0)
			return Collision.DOWN;
		else if (p.y + this.size.y >= Breakout.MODEL_SIZE.y)
			return Collision.UP;
		else
			return Collision.NONE;
	}
	
	public Collision checkBounds(int i) {
		if (i < 0) {
			return Collision.RIGHT;
		}
		else if (i+this.size.x >=Breakout.MODEL_SIZE.x){
			return Collision.LEFT;
		}
		else {
			return Collision.NONE;
		}
	}
	
	public Collision checkCollision(BreakoutObj b) {
		double ax = 0.5 * (this.size.x + b.getSize().x);
		double ay = 0.5 * (this.size.y + b.getSize().y);
		double dx = (this.location.x + this.size.x/2) - (b.getPos().x + b.getSize().x/2);
		double dy = (this.location.y + this.size.y/2) - (b.getPos().y + b.getSize().y/2);
		if (Math.abs(dx) <= ax && Math.abs(dy) <=ay) {
			double wy = ax * dy;
			double hx = ay * dx;
			if (wy > hx)
		        if (wy > -hx)
		            return Collision.DOWN;
		        else
		            return Collision.LEFT;
		    else {
		        if (wy > -hx) 
		            return Collision.RIGHT;
		        else
		            return Collision.UP;
		    }
		} else {
			return Collision.NONE;
		}
	}
	
	public double getDistance(BreakoutObj b) {
		double dx = (this.location.x + this.size.x/2) - (b.getPos().x + b.getSize().x/2);
		double dy = (this.location.y + this.size.y/2) - (b.getPos().y + b.getSize().y/2);
		return dx * dx + dy * dy;
	}
	
	protected Point getCollisionPoint() {
		return this.location;
	}
}
