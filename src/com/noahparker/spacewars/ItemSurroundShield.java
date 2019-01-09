package com.noahparker.spacewars;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class ItemSurroundShield extends ItemDefense {
	Circle shield;
	Shape[] translate;
	Shape test;
	boolean draw = false;
	
	public ItemSurroundShield(float x, float y, int width, int height) {
		super(x, y, width, height);
		translate = new Shape[1];
	}

	@Override
	public int getDuration() {
		return 0;
	}

	@Override
	public void onPickup(float x, float y, float theta) {
		shield = new Circle(x, y, 100); //change radius?
	}

	@Override
	public void intersectPlayer(Player player, Player source, Shape intersected) {
		//player.destroy();
		//source.addScore(1);
		((GameState) player.getController().getGame()).registerKill(player, source);
	}

	@Override
	public Shape[] getActiveShapes() {
		return translate;
	}

	@Override
	public void render(Graphics g, Player player) { //render UI here or in super class?
		translate[0] = (Shape) shield;
		translate[0] = shield.transform(Transform.createTranslateTransform(player.getShape().getCenterX()-shield.getCenterX(), player.getShape().getCenterY()-shield.getCenterY()));
		//shield = (Circle) translate;		
		//laser.transform(Transform.createTranslateTransform(x-laser.getPoint(1)[0], y-laser.getPoint(1)[1]));
		if(this.getActive()) {
			g.setColor(player.getColor());
			g.setLineWidth(6);
			g.draw(translate[0]);
			g.setLineWidth(1);
			g.setColor(Color.white);
			g.draw(translate[0]);
		}
	}

	@Override
	public void useItem(boolean pressed) {
		this.setActive(pressed);
	}

	@Override
	public void intersectProjectile(Projectile p) { //change leading point of laser to x,y? that should solve clipping/incorrect reflection issues
		//p.draw = false;
		
		float x1 = translate[0].getCenterX(); //location of shield's shape
		float y1 = translate[0].getCenterY();
		
		float x2 = p.getX(); //location of projectile
		float y2 = p.getY();
		
		float vx = x1-x2; //vector from shape to projectile's intersection point
		float vy = y1-y2; //(this is the normal of the tangent line)
		
		float length = (float) Math.sqrt((vx*vx)+(vy*vy));
		
		float normvx = vx/length; //normalized vector of normal of tangent! 
		float normvy = vy/length;
		
		float plength = (float) Math.sqrt((p.getVelX()*p.getVelX())+(p.getVelY()*p.getVelY()));
		
		float normpx = p.getVelX()/plength; //normalized vector of projectile!
		float normpy = p.getVelY()/plength;
		
		float dot = (normvx * normpx) + (normvy * normpy); //dot product of two vectors
				
		float newpx = normpx - 2*(normvx)*(dot); //equation for vector reflection
		float newpy = normpy - 2*(normvy)*(dot); //v1 - 2*(v2*dot)
		
		newpx*=plength; //set magnitude of reflection to what it was before reflection
		newpy*=plength; 
		
		p.setVector(p.getLine().getPoint(1)[0], p.getLine().getPoint(1)[1], newpx, newpy); //reset line and vector of projectile
		p.setLine(p.getLine().getPoint(1)[0], p.getLine().getPoint(1)[1], newpx, newpy);
		
		}

	public void update(Player[] players, Projectile[] projectiles) {
		
	}

	@Override
	public String getName() {
		return "Shield";
	}

}
