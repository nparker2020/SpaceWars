package com.noahparker.spacewars;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Transform;

public class Projectile {
	private float x; 
	private float y;
	private float velx;
	private float vely;
	private float travelled;
	private int ID; //Player ID who fired it!
	private int index;
	private int damage;
	boolean draw = false;
	Line line;
	
	public Projectile(float x, float y, float velx, float vely, int ID, int damage, boolean draw) {
		this.x = x;
		this.y = y;
		this.velx = velx;
		this.vely = vely;
		this.ID = ID;
		travelled = 0;
		this.draw = draw;
		this.damage = damage;
		line = new Line(x, y, x+3*velx, y-3*vely);
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public Line getLine() {
		return line;
	}
	
	public void setLine(float x, float y, float velx, float vely) {
		line = null;
		line = new Line(x, y, x+3*velx, y-3*vely);
		//System.out.println("velx: "+velx+", vely: "+vely);
	}
	
	boolean getDraw() {
		return draw;
	}
	
	void setDraw(boolean draw) {
		this.draw = draw;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	void setIndex(int i) {
		index = i;
	}
	
	int getIndex() {
		return index;
	}
	
	int getID() {
		return ID;
	}
	
	float getX() {
		return x;
	}
	
	float getY() {
		return y;
	}
	
	void setX(float x) {
		this.x = x;
	}
	
	void setY(float y) {
		this.y = y;
	}
	
	void setVector(float x, float y, float velx, float vely) {
		this.x = x;
		this.y = y;
		this.velx = velx;
		this.vely = vely;
		travelled = 0;
	}
	
	float getVelX() {
		return velx;
	}
	
	float getVelY() {
		return vely;
	}
	
	float getTravelled() {
		return travelled;
	}
	
	void addTraveled(int add) {
		travelled += add;
	}
	
	void moveProjectile() {
		x += velx;
		y -= vely;
		line = (Line) line.transform(Transform.createTranslateTransform(x-line.getPoint(0)[0], y-line.getPoint(0)[1]));
	}
	
	void addX(float add) {
		x += add;
		line = (Line) line.transform(Transform.createTranslateTransform(x-line.getPoint(0)[0], 0));
	}
	
	void addY(float add) {
		y += add;
	}
}
