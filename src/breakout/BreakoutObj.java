package breakout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import breakout.Ball;
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
			return Collision.LEFT;
		else if (p.x + this.size.x >= Breakout.MODEL_SIZE.x)
			return Collision.RIGHT;
		else if (p.y < 0)
			return Collision.UP;
		else if (p.y + this.size.y >= Breakout.MODEL_SIZE.y)
			return Collision.DOWN;
		else
			return Collision.NONE;
	}
	
	public Collision checkBounds(int i) {
		if (i < 0) {
			return Collision.LEFT;
		}
		else if (i+this.size.x >=Breakout.MODEL_SIZE.x){
			return Collision.RIGHT;
		}
		else {
			return Collision.NONE;
		}
	}
	
	public Collision checkCollision(BreakoutObj b) {
		
//		Point cp = this.getCollisionPoint();
//		if (cp.x + this.size.x > b.getPos().x && 
//				cp.x <= b.getPos().x &&
//				cp.y >= b.getPos().y && 
//				cp.y < b.getPos().y + b.getSize().y)
//			return Collision.LEFT;
//		else if (cp.x < b.getPos().x + b.getSize().x && 
//				cp.x + this.size.x >= b.getPos().x + b.getSize().x &&
//				cp.y >= b.getPos().y && 
//				cp.y < b.getPos().y + b.getSize().y)
//			return Collision.RIGHT;
//		else if (cp.y + this.size.y > b.getPos().y &&
//				cp.y <= b.getPos().y &&
//				cp.x >= b.getPos().x && 
//				cp.x < b.getPos().x + b.getSize().x)
//			return Collision.UP;
//		else if (cp.y  < b.getPos().y + b.getSize().y &&
//				cp.y + this.size.y >= b.getPos().y + b.getSize().y &&
//				cp.x >= b.getPos().x && 
//				cp.x < b.getPos().x + b.getSize().x)
//			return Collision.DOWN;
//		
//		else
//			return Collision.NONE;
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
		            return Collision.RIGHT;
		    else
		        if (wy > -hx) 
		            return Collision.LEFT;
		        else
		            return Collision.UP;
			
		} else {
			return Collision.NONE;
		}
	}
	
	protected Point getCollisionPoint() {
		return this.location;
	}
}
