package breakout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;

public class Ball extends BreakoutObj{
	private boolean launched;
	private boolean acid;
	private long powerupStart;
	public double vX;
	public double vY;
	
	public Ball(Point location, Point size, Color color) {
		super(location, size, color);
		this.launched = false;
		this.acid = false;
		this.vX = 1;
		this.vY = -1;
	}
	
	@Override
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		g2.setColor(this.color);
		if (this.acid) {
			g2.setColor(Color.GREEN);
		}
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
			this.location = newPos;
			return collision;
		}
		return Collision.NONE;
	}
	
	public void changeDirection(Collision collision) {
		switch (collision) {
		case UP: {
			if (vY < 0) {
				vX = -vX;
			} else {
				vY = -vY;
			}
			break;
		}
		case DOWN: {
			if (vY > 0) {
				vX = -vX;
			} else {
				vY = -vY;
			}
			break;
		}
		case LEFT: {
			if (vX < 0) {
				vY = -vY;
			} else {
				vX = -vX;
			}
			break;
		}
		case RIGHT:
			if (vX > 0) {
				vY = -vY;
			} else {
				vX = -vX;
			}
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
	
	public void addPowerUp(PowerUpType powerUp) {
		switch(powerUp) {
		case ACID: {
			this.acid = true;
			this.powerupStart = System.currentTimeMillis();
		}
		}
	}
	
	public boolean isAcidic() {
		return this.acid;
	}
	
	public void checkPowerupDuration() {
		if (this.acid && System.currentTimeMillis() - this.powerupStart > 3000) {
			this.acid = false;
			this.powerupStart = 0;
		}
	}
	
	public void setVel(double vx, double vy) {
		this.vX = vx;
		this.vY = vy;
	}
	
	
	
}
