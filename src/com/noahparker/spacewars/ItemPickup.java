package com.noahparker.spacewars;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

public abstract class ItemPickup {
	private float x;
	private float y;
	private int id; //id of player owner 
	Polygon shape;
	private boolean draw = true;
	private boolean consumed = false;
	private boolean active = false;
	
	public ItemPickup(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		shape = new Polygon();
		shape.addPoint(x, y);
		shape.addPoint(x+width, y);
		shape.addPoint(x+width, y+height);
		shape.addPoint(x, y+height);
	}
	
	public abstract int getDuration();
	
	public abstract void onPickup(float x, float y, float theta);
	
	public abstract String getName();
	//public abstract void render(float x, float y, float theta, int ID, Color color);
	
	public void onItemPickup(Player player) {
		this.id = player.getID();
		onPickup(player.getX(), player.getY(), player.getTheta());
	}
	
	public int getID() {
		return id;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setDraw(boolean draw) {
		this.draw = draw;
	}
	
	public boolean getDraw() {
		return draw;
	}
	
	public Polygon getShape() {
		return shape;
	}
	
	public String toString() {
		return "X: "+x+" Y: "+y+" SHAPE: "+shape.toString();
	}
	
	public boolean getConsumed() {
		return consumed;
	}
	
	public void setConsumed(boolean b) {
		this.consumed = b;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean b) {
		this.active = b;
	}
	
	public abstract void intersectPlayer(Player player, Player source, Shape intersected); //shape of projectile!
	
	public abstract void intersectProjectile(Projectile p);
	
	public abstract Shape[] getActiveShapes();
	
	public abstract void render(Graphics g, Player player);
	
	public abstract void update(Player[] players, Projectile[] projectiles);
	
	public abstract void useItem(boolean pressed); //pressed down or released
	
}
