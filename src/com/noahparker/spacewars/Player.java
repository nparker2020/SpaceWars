package com.noahparker.spacewars;

import java.io.File;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;

public class Player {
	private float x;
	private float y;
	private float velocity_x;
	private float velocity_y;
	private float spawnx;
	private float spawny;
	private float displayx;
	private float displayy;
	private float rotation; //angle
	private float theta;
	private Polygon shape;
	private Polygon flame;
	private boolean thrusting;
	private boolean destroyed = false;
	private PlayerController controller; //only used if the player is local!
	private int ID;
	private int score = 0;
	private int health;
	private int damage = 25;
	private int lives;
	private Color color;
	private boolean hasItem = false;
	ItemPickup item; 
	ConfigurableEmitter emitter;
	File xml;
	
	Player() {
		x = 0;
		y = 0;
		velocity_x = 0;
		velocity_y = 0;
		rotation = 0;
		theta = 0;
		shape = new Polygon();
		ID = -1;
		health = 100;
	}
	
	Player(float x, float y, int ID, Color color) {
		this.x = x;
		this.y = y;
		this.ID = ID;
		velocity_x = 0;
		velocity_y = 0;
		rotation = 0;
		theta = 0;
		this.color = color;
		shape = new Polygon();
		
		/*shape.addPoint(this.x, this.y-25);
		shape.addPoint(this.x+15, this.y+15);
		shape.addPoint(this.x, this.y+15); //center point for flame
		shape.addPoint(this.x-15, this.y+15);
		*/
		shape.addPoint(this.x, this.y-45);
		shape.addPoint(this.x+35, this.y+35);
		shape.addPoint(this.x, this.y+35);
		shape.addPoint(this.x-35, this.y+35);
		
		flame = new Polygon();
		/*flame.addPoint(this.x-8, this.y+20);
		flame.addPoint(this.x, this.y+20);
		flame.addPoint(this.x+8, this.y+20);
		flame.addPoint(this.x, this.y+40);*/
		
		flame.addPoint(this.x-18, this.y);
		flame.addPoint(this.x+18, this.y);
		flame.addPoint(this.x+18, this.y+20);
		flame.addPoint(this.x, this.y+20);
		flame.addPoint(this.x-18, this.y+20);
		
		/*flame.addPoint(this.x-18, this.y+40);
		flame.addPoint(this.x, this.y+40); //middle point
		flame.addPoint(this.x+18, this.y+40);
		flame.addPoint(this.x, this.y+70);
		*/
		
		
		//flame = (Polygon) flame.transform(Transform.createTranslateTransform(0, -300));
		//System.out.println(flame.getPoint(0)[0]+", "+flame.getPoint(0)[1]);
		health = 100;
		lives = 4;
	}
	
	void recreateShape() {
		shape = new Polygon();
		
		shape.addPoint(this.x, this.y-45);
		shape.addPoint(this.x+35, this.y+35);
		shape.addPoint(this.x, this.y+35);
		shape.addPoint(this.x-35, this.y+35);
		
		flame = new Polygon();
		/*flame.addPoint(this.x-8, this.y+20);
		flame.addPoint(this.x, this.y+20);
		flame.addPoint(this.x+8, this.y+20);
		flame.addPoint(this.x, this.y+40);*/
		
		/*flame.addPoint(this.x-18, this.y+40);
		flame.addPoint(this.x, this.y+40); //middle point
		flame.addPoint(this.x+18, this.y+40);
		flame.addPoint(this.x, this.y+70); //top
		*/
		flame.addPoint(this.x-18, this.y);
		flame.addPoint(this.x+18, this.y);
		flame.addPoint(this.x+18, this.y+20);
		flame.addPoint(this.x, this.y+20);
		flame.addPoint(this.x-18, this.y+20);
		
		
		health = 100;
	}
	
	public void drawUIBlock(Graphics g, Shape lifeshape, Font large, Font small) { //works but should fix depending on margin!
		g.setLineWidth(1);
		
		g.setColor(color);
		
		large.drawString(displayx, displayy, ""+score, color);
		small.drawString(displayx, displayy+large.getHeight(""+score), ""+health, color);	
		g.setLineWidth(4);
		
		lifeshape.setCenterY(displayy+(large.getHeight(""+score)+large.getHeight(""+health)));
		for(int i = 0; i<lives; i++) {
			if(ID == 0 || ID == 2) {
				lifeshape.setCenterX((displayx+15/*width of shape*/)+((i)*45));
			}else{
				lifeshape.setCenterX((displayx+15)-((i)*45));
			}
			
			g.setLineWidth(4);
			g.setColor(color);
			g.draw(lifeshape);
			g.setColor(Color.white);
			g.setLineWidth(1);
			g.draw(lifeshape);
		}
	}
	
	public void setEmitter(File xml, Image image) {
		try {
			this.emitter = ParticleIO.loadEmitter(xml);
		} catch (IOException e) {
			System.out.println("Player Particle Emitter failed to load!");
			e.printStackTrace();
		}
		emitter.setEnabled(false);
		this.xml = xml;
	}
	
	public ConfigurableEmitter getEmitter() {
		return emitter;
	}
	
	public boolean hasItem()  {
		return hasItem;
	}
	
	public void addItem(ItemPickup item) {
		hasItem = true;
		this.item = item;
	}
	
	public ItemPickup getItem() {
		return item;
	}
	
	public void activateItem() {
		
	}
	
	public void removeItem() {
		hasItem = false;
		this.item = null;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void changeHealth(int damage) {
		health += damage; //damage should be negative, health positive
	}
	
	public int getLives() {
		return lives;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public void addLives(int add) {
		lives += add;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public float[] getTipPoint() {
		return shape.getPoint(0);
	}
	
	public PlayerController getController() {
		return controller;
	}
	
	void addScore(int add) {
		score += add;
	}
	
	void removeScore(int remove) {
		score -= remove;
	}
	
	int getScore() {
		//System.out.println("Score: "+score);
		return score;
	}
	
	void updateShape() {
		shape = (Polygon) shape.transform(Transform.createRotateTransform(rotation, shape.getCenterX(), shape.getCenterY()));
		flame = (Polygon) flame.transform(Transform.createRotateTransform(rotation, shape.getCenterX(), shape.getCenterY()));
		//rotation = 0;
	}
	
	void updateLocation() {
		x += velocity_x;
		y += velocity_y;
		shape = (Polygon) shape.transform(Transform.createTranslateTransform(x-shape.getCenterX(), y-shape.getCenterY()));
		
		//flame = (Polygon) flame.transform(Transform.createTranslateTransform((shape.getPoint(2)[0])-flame.getPoint(1)[0], (shape.getPoint(2)[1])-flame.getPoint(1)[1]));
		
		flame = (Polygon) flame.transform(Transform.createTranslateTransform((shape.getPoint(2)[0])-flame.getCenterX(), (shape.getPoint(2)[1])-flame.getCenterY()));
		
		if(emitter==null) {
			return;
		}
		emitter.setPosition(flame.getPoint(3)[0], emitter.getY(), false); //set angular offset to theta in degrees
		emitter.setPosition(emitter.getX(), flame.getPoint(3)[1], false);
		emitter.angularOffset.setValue((float) ((theta+Math.PI)*(180/Math.PI)));;
	}
	
	/*void checkLocation() {
		if(shape.getCenterX()<0) {
			shape = (Polygon) shape.transform(Transform.createTranslateTransform(640, 0));
			x = 640;
		}
		if(shape.getCenterY()<0) {
			shape = (Polygon) shape.transform(Transform.createTranslateTransform(0, 480));
			y = 480;
		}
		if(shape.getCenterX()>640) {
			shape = (Polygon) shape.transform(Transform.createTranslateTransform(-640, 0));
			x = 0;
		}
		if(shape.getCenterY()>480) {
			shape = (Polygon) shape.transform(Transform.createTranslateTransform(0, 0));
			y = 0;
		}
	}*/
	
	void updateShape(Shape shape) {
		this.shape = (Polygon) shape;
	}
	
	void setDisplayPoints(float array[])  {
		displayx = array[0];
		displayy = array[1];
	}
	
	public float getDisplayX() {
		return displayx;
	}
	
	public float getDisplayY() {
		return displayy;
	}

	
	void setSpawnX(float x) {
		spawnx = x;
		this.x = x;
	}
	
	void setSpawnY(float y) {
		spawny = y;
		this.y = y;
	}
	
	float getSpawnX() {
		return spawnx;
	}
	
	float getSpawnY() {
		return spawny;
		
	}
	
	void setX(float x) {
		this.x = x;
	}
	
	void setY(float y) {
		this.y = y;
	}
	
	boolean isThrusting() {
		return thrusting;
	}
	
	void setThrusting(boolean b) {
		thrusting = b;
		
		if(emitter==null) {
			return;
		}
		if(thrusting) {
			//emitter.resetState();
			//emitter.reset();
			//emitter = duplicate;
			emitter.setEnabled(true);
			emitter.length.setEnabled(false);
			
		}else{
			emitter.length.setEnabled(true);
			//emitter.wrapUp();
			
			//emitter.wrapUp();
		}
	}
	
	float getXVelocity() {
		return velocity_x;
	}
	
	void setXVelocity(float vel) {
		velocity_x = vel;
	}
	
	void addXVelocity(float vel) {
		velocity_x += vel;
	}
	
	float getYVelocity() {
		return velocity_y;
	}
	
	void setYVelocity(float vel) {
		velocity_y = vel;
	}
	
	void addYVelocity(float vel) {
		velocity_y += vel;
	}
	
	float getX() {
		return x;
	}
	
	float getY() {
		return y;
	}
	
	float getTheta() {
		return theta;
	}
	
	void setTheta(float t) {
		theta = t;
	}
	
	void addTheta(float t) {
		theta += t;
	}
	
	float getRotation() {
		return rotation;
	}
	
	void setRotation(float num) {
		rotation = num;
	}
	
	Shape getShape() {
		return shape;
	}
	
	Shape getFlame() {
		return flame;
	}
	
	boolean isDestroyed() {
		return destroyed;
	}
	
	int getID() {
		return ID;
	}
	
	void kill(Player killer) {
		((GameState) controller.getGame()).registerKill(this, killer);
	}
	
	void destroy() {
		destroyed = true;
		lives -= 1;
	}
	
	void reset() {
		if(lives>0) {
			destroyed = false;
			velocity_x = 0;
			velocity_y = 0;
			rotation = 0;
			theta = 0;
			x = spawnx;
			y = spawny;
			health = 100;
			recreateShape();
			updateShape();
		}
	}
	
	void restart() {
		destroyed = false;
		velocity_x = 0;
		velocity_y = 0;
		rotation = 0;
		theta = 0;
		x = spawnx;
		y = spawny;
		health = 100;
		score = 0;
		lives = 4;
		System.out.println("SPAWNX: "+spawnx+", SPAWNY: "+spawny+", X: "+x+", Y: "+y);
		//checkLocation();
		//resetShape();
		recreateShape();
		updateShape();
	}
	
	void checkInput() {
		controller.checkInput();
	}
	
	void setController(PlayerController playercontroller) {
		this.controller = playercontroller;
	}
}
