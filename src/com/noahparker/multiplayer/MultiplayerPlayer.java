package com.noahparker.multiplayer;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class MultiplayerPlayer {
	private float x;
	private float y;
	private float xvel;
	private float yvel;
	private float rotation;
	private float theta;
	private int ID;
	boolean destroyed = false;
	private Polygon shape;
	private Polygon flame;
	
	public MultiplayerPlayer(float x, float y, int ID) {
		this.x = x;
		this.y = y;
		this.ID = ID;
		shape = new Polygon();
		shape.addPoint(this.x, this.y-45);
		shape.addPoint(this.x+35, this.y+35);
		shape.addPoint(this.x, this.y+35); //center point for flame
		shape.addPoint(this.x-35, this.y+35);
		flame = new Polygon();
		flame.addPoint(this.x-18, this.y+40);
		flame.addPoint(this.x, this.y+40);
		flame.addPoint(this.x+18, this.y+40);
		flame.addPoint(this.x, this.y+70);
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public Shape getFlame() {
		return flame;
	}
	
	void update(float[] data) {
		this.x = data[1];
		this.y = data[2];
		this.xvel = data[3];
	
		this.yvel = data[4];
		this.theta = data[5]; //maybe add packet space for rotation for nice transition
	}
	
	int getID() {
		return ID;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	float getXVelocity() {
		return xvel;
	}
	
	public void setXVelocity(float xvel) {
		this.xvel = xvel;
	}
	
	public float getYVelocity() {
		return yvel;
	}
	
	public void setYVelocity(float yvel) {
		this.yvel = yvel;
	}
	
	float getRotation() {
		return rotation;
	}
	
	void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	float getTheta() {
		return theta;
	}
	
	public void setTheta(float theta) {
		this.theta = theta;
	}
	
	public void updateLocation() {
		x += xvel;
		y += yvel;
		shape = (Polygon) shape.transform(Transform.createTranslateTransform(x-shape.getCenterX(), y-shape.getCenterY()));
		flame = (Polygon) flame.transform(Transform.createTranslateTransform((shape.getPoint(2)[0])-flame.getPoint(1)[0], (shape.getPoint(2)[1])-flame.getPoint(1)[1]));
	}
	
	public void updateData(float[] data) { //incoming packet
		if(ID == data[5]) {
			setX(data[0]);
			setY(data[1]);
			setXVelocity(data[2]);
			setYVelocity(data[3]);
			setTheta(data[5]);
		}else{
			System.out.println("Data was misplaced! Player ID: "+ID+", Data ID: "+data[5]);
		}
	}
	
}
