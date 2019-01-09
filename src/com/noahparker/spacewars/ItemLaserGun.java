package com.noahparker.spacewars;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class ItemLaserGun extends ItemWeapon {
	Polygon[] laser = new Polygon[1];
	boolean draw;
	int duration = 0;
	float angle = 0;
	
	//Giant Laser Beam that sweet screen
	public ItemLaserGun(float x, float y, int width, int height) {
		super(x, y, width, height);
		laser[0] = new Polygon();
	}

	public int getDuration() {
		return 5; //5 seconds
	}

	@Override
	public void getProjectile(float x, float y, float theta, int ID) {
	
	}

	public Shape[] getActiveShapes() {
		return laser;
	}
	
	@Override
	public void render(Graphics g, Player player) { //owner of item
		//laser.transform(Transform.createRotateTransform(0.1f, x, y));
		float difference = player.getTheta() - angle;
		if(draw) {
			laser[0] = (Polygon) laser[0].transform(Transform.createTranslateTransform(player.getTipPoint()[0]-laser[0].getPoint(1)[0], player.getTipPoint()[1]-laser[0].getPoint(1)[1]));
			//laser = (Polygon) laser.transform(Transform.createTranslateTransform(x-lase, ))
			laser[0] = (Polygon) laser[0].transform(Transform.createRotateTransform(difference, player.getTipPoint()[0], player.getTipPoint()[1])); 
			angle+=difference;
			g.setColor(player.getColor());
			g.fill(laser[0]);
			g.setColor(Color.white);
			g.setLineWidth(20); //too small? maybe just draw a smaller poly?
			g.drawLine(laser[0].getPoint(1)[0], laser[0].getPoint(1)[1], laser[0].getPoint(4)[0], laser[0].getPoint(4)[1]);
			g.setLineWidth(1);
			g.draw(laser[0]);
		} 
		duration += 1;
		if(duration>=500) {
			draw = false;
			duration = 0;
			this.setActive(false);
			this.setConsumed(true);
		}
	}

	@Override
	public void onPickup(float x, float y, float theta) {
		laser[0].addPoint(x, y);
		laser[0].addPoint(x+25, y);
		laser[0].addPoint(x+50, y);
		laser[0].addPoint(x+50, y-2000);
		laser[0].addPoint(x+25, y-2000);
		laser[0].addPoint(x, y-2000);
		
		laser[0] = (Polygon) laser[0].transform(Transform.createRotateTransform(theta, x, y));
		laser[0] = (Polygon) laser[0].transform(Transform.createTranslateTransform(x-laser[0].getCenterX(), y-laser[0].getCenterY()));
		this.angle = theta;
	}
	
	public void useItem(boolean pressed) { //run when button in PlayerController is pressed
		/*if(pressed) {
			//draw = true; -tick down time!
			draw = true;
		}else{
			//draw = false; -stop ticking time!
			draw = true;
		}*/
		//OR... activate once and use all!
		if(pressed) {
			this.draw = true;
			this.setActive(true);
		}
		
	}

	@Override
	public void intersectPlayer(Player intersecting, Player source, Shape s) {
		intersecting.destroy();
		source.addScore(1);
	}

	@Override
	public void intersectProjectile(Projectile p) {
		// TODO Auto-generated method stub
		return;
	}

	public void update(Player[] players, Projectile[] projectiles) {
		
	}

	@Override
	public String getName() {
		return "Laser";
	}
	
}
