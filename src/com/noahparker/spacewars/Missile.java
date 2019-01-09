package com.noahparker.spacewars;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class Missile {
	Polygon shape;
	float x;
	float y;
	float velx;
	float vely;
	float theta;
	float rotation;
	int id; //id of player (target player if used with ItemHeatMissile, not always though)
	boolean active;
	int lastrotation = 0; //loops since last rotation set!
	float targetx;
	float targety;
	float targetangle;
	
	public Missile(float x, float y, float velx, float vely, float height, float width, int id, boolean active) {
		this.x = x;
		this.y = y;
		this.velx = velx;
		this.vely = vely;
		this.id = id;
		this.theta = 0;
	
		shape = new Polygon();
		shape.addPoint(x, y);
		shape.addPoint(x+(width/2), y-(height/2));
		shape.addPoint(x+width, y);
		shape.addPoint(x+width, y+height);
		shape.addPoint(x, y+height);
		this.active = active;
	}
	
	public void setValues(float x, float y, float velx, float vely, float height, float width, int id, float theta, boolean active) {
		this.x = x;
		this.y = y;
		this.velx = velx;
		this.vely = vely;
		this.id = id;
		this.theta = theta;
		this.active = active;
		
		shape = null;
		
		shape = new Polygon();
		shape.addPoint(x, y);
		shape.addPoint(x+(width/2), y-(height/2));
		shape.addPoint(x+width, y);
		shape.addPoint(x+width, y+height);
		shape.addPoint(x, y+height);
		
		shape = (Polygon) shape.transform(Transform.createRotateTransform(theta, shape.getCenterX(), shape.getCenterY()));
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public void rotateShape(float rotation){
		shape = (Polygon) shape.transform(Transform.createRotateTransform(rotation));
		theta += rotation;
		lastrotation += 1;
	}
	
	public void rotateShape() {
		shape = (Polygon) shape.transform(Transform.createRotateTransform(rotation, shape.getCenterX(), shape.getCenterY()));
		theta += rotation;
		
		if(theta<0.0) {
			theta += 2*Math.PI;
			return;
		}
		if(theta>2*Math.PI) {
			theta -= 2*Math.PI;
		}
		lastrotation += 1;
	}
	
	public void updateLocation() {
		x+=velx;
		y-=vely;
		shape = (Polygon) shape.transform(Transform.createTranslateTransform(x-shape.getCenterX(), y-shape.getCenterY()));
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public int getID() {
		return id;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
		lastrotation = 0;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public int getLastRotation() {
		return lastrotation;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getTheta() {
		return theta;
	}
	
	public void setVelX(float velx) {
		this.velx = velx;
	}
	
	public float getVelX() {
		return velx;
	}
	
	public void setVelY(float vely) {
		this.vely = vely;
	}
	
	public float getVelY() {
		return vely;
	}
	
	public float getTargetX() {
		return targetx;
	}
	
	public float getTargetY() {
		return targety;
	}
	
	public void setTargetX(float x) {
		targetx = x;
	}
	
	public void setTargetY(float y) {
		targety = y;
	}
	
	public void setTargetAngle(float angle) {
		this.targetangle = angle;
	}
	
	public float getTargetAngle() {
		return targetangle;
	}
}
