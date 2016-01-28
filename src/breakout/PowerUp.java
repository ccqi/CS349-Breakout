package breakout;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

enum PowerUpType {
	ACID, SHRINK_PADDLE, EXPAND_PADDLE, NONE
}

public class PowerUp extends BreakoutObj {
	
	BufferedImage image;
	private int type;
	public PowerUp(Point location, Point size, Color color, int type) {
		super(location, size, color);
		try {
			this.image = ImageIO.read(new File("src/assets/powerup.png"));
			this.type = type;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Collision update(int increment) {
		this.location.y += increment;
		Collision collision = checkBounds(this.location);
		return collision;
	}
	
	public void draw(Graphics2D g2, Point scaledLocation, Point scaledSize) {
		//g2.setColor(this.color);
		g2.drawImage(image, scaledLocation.x, scaledLocation.y, scaledSize.x, scaledSize.y, null);
	}
	
	public PowerUpType getType() {
		if (type >=0 && type < PowerUpType.values().length) {
			return PowerUpType.values()[type];
		}
		return PowerUpType.NONE;
	}
}
