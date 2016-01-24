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
		if (this.location.y + this.size.y > b.getPos().y &&
				this.location.y <= b.getPos().y &&
				this.location.x >= b.getPos().x && 
				this.location.x < b.getPos().x + b.getSize().x)
			return Collision.UP;
		else if (this.location.y  < b.getPos().y + b.getSize().y &&
				this.location.y + this.size.y >= b.getPos().y + b.getSize().y &&
				this.location.x >= b.getPos().x && 
				this.location.x < b.getPos().x + b.getSize().x)
			return Collision.DOWN;
		else if (this.location.x + this.size.x > b.getPos().x && 
				this.location.x <= b.getPos().x &&
				this.location.y >= b.getPos().y && 
				this.location.y < b.getPos().y + b.getSize().y)
			return Collision.LEFT;
		else if (this.location.x < b.getPos().x + b.getSize().x && 
				this.location.x + this.size.x >= b.getPos().x + b.getSize().x &&
				this.location.y >= b.getPos().y && 
				this.location.y < b.getPos().y + b.getSize().y)
			return Collision.RIGHT;
		else
			return Collision.NONE;
	}
}
