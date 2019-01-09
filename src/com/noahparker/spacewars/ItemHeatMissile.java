package com.noahparker.spacewars;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

public class ItemHeatMissile extends ItemWeapon{
	Polygon[] missiles;
	Missile[] m = new Missile[4];
	int targetid = -1;
	/*float targetrotation = 0;
	float rotation;
	float theta = 0;
	float velx = 0;
	float vely = 0;*/
	int activemissiles = 0;
	float targetrotation;
	float angle;

	public ItemHeatMissile(float x, float y, int width, int height) {
		super(x, y, width, height);
		missiles = new Polygon[6];
		//m = new Missile[4];
		/*int count = 0;
		for(Missile m1 : m) {
			m1 = new Missile(0,0,0,0, 0, 0, -1, false);
			System.out.println(m[count]);
			count++;
		}*/
		for(int i = 0; i<m.length;i++) {
			m[i] = new Missile(0,0,0,0,0,0,-1,false);
			//System.out.println(m[i]);
		}
		//System.out.println(m[0]);
 	}

	
	public void getProjectile(float x, float y, float theta, int ID) {
			
	}

	
	public int getDuration() {
		return 5;
	}

	public void onPickup(float x, float y, float theta) {
		missiles[0] = new Polygon();
		missiles[0].addPoint(x, y);
		missiles[0].addPoint(x+5, y-5);
		missiles[0].addPoint(x+10, y);
		missiles[0].addPoint(x+10, y+20);
		missiles[0].addPoint(x, y+20);
	}

	
	public void intersectPlayer(Player player, Player source, Shape intersected) {
		//player.destroy();
		((GameState) player.getController().getGame()).registerKill(player, source);
		getMissile(intersected).setActive(false);
	}
	
	public Missile getMissile(Shape s) {
		for(int i = 0; i<activemissiles; i++) {
			if(m[i].getShape().equals(s)) {
				return m[i];
			}
		}
		return null;
	}

	public void intersectProjectile(Projectile p) {
		
	}

	public Shape[] getActiveShapes() {
		Shape[] shapes = new Shape[activemissiles];
		for(int i =0; i<activemissiles; i++) {
			shapes[i] = m[i].getShape();
		}
		return shapes;
	}

	public void render(Graphics g, Player player) {
		if(this.getActive()) {
			g.setColor(Color.white);
			for(int i = 0; i<activemissiles; i++) {
				g.setColor(player.getColor());
				g.fill(m[i].getShape());
				g.setColor(Color.white);
				g.draw(m[i].getShape());
			}
			
		}
	}

	public void useItem(boolean pressed) {	
		if(pressed) {
			//dish out missile!
			//addMissile();
			activemissiles += 1;
			
			this.setActive(true);
		}
	}
	
	public void addMissile(float x, float y, float velx, float vely, int id, boolean active) { //add new Missile object to array or set new values... etc.
		for(Missile m : m) {
			if(!m.getActive()) {
				m.setValues(x, y, velx, vely, 40, 10, id, 0, active);
				//add one to number of missiles
			}
		}
	}
	
	public void update(Player[] players, Projectile[] projectiles) {
		//adjust trajectories of active missiles
		float distance = 5000;
		
		if(this.getActive()) {
			 
				for(Player player : players) { //every loop finds closest target
					
					if(player==null || player.getID()==this.getID()) { //we don't want to target ourselves!
						continue;
					}
					float px = player.getX();
					float py = player.getX();
					
					float locx = players[this.getID()].getX();
					float locy = players[this.getID()].getY();
					
					float dist = (float) Math.sqrt((locx-px)*(locx-px)+(locy-py)*(locy-py));
					
					if(dist<distance) {
						distance = dist;
						targetid = player.getID();
					}
					//calculate distance from our player to others and find smallest one, sets targetid to their ID.
				}
				
				for(int i = 0; i<activemissiles; i++) {
					
					if(!m[i].getActive()) { //missile is queued but hasn't been set
						
						float velx = (float) (Math.sin(players[this.getID()].getTheta())*10);
						float vely = (float) (Math.cos(players[this.getID()].getTheta())*10);
						
						
						m[i].setValues(players[this.getID()].getTipPoint()[0], players[this.getID()].getTipPoint()[1], velx, vely, 40, 10, targetid, players[this.getID()].getTheta(), true);
					}else{
						
						m[i].setTargetX(players[m[i].getID()].getX());
						m[i].setTargetY(players[m[i].getID()].getY());
						
						float vx = players[m[i].getID()].getX()-m[i].getX(); //vector from target to missile
						float vy = players[m[i].getID()].getY()-m[i].getY(); 
						
						float tantheta = (float) Math.atan2(vy, vx); //theta of vector from target to missile 
						
						tantheta += Math.PI/2.0f;
						
						if(tantheta > 2*Math.PI) {
							tantheta -= 2*Math.PI;
						}else if (tantheta<0) {
							tantheta += 2*Math.PI;
						}
						
						/*
						 * difference of rotation between vector to target, and missile's vector.
						 * used to calculate how much missile needs to rotate
						 */
						float difference = m[i].getTheta()-tantheta; 
					
						if(difference>2*Math.PI) { //make sure difference is always between 0 and 2 Pi. 
							difference -= 2*Math.PI; //(this is how to missile and player's rotation are kept)
						}else if(difference<0) {
							difference += 2*Math.PI;
						}
						
						float gain = 0.06f; //how aggressive the missile's turning is
						
						//calculates which direction is more efficient to rotate
						if(difference>0 && difference < Math.PI) {
							m[i].setRotation(-gain * difference); //sets intensity of rotation based on how much rotation is needed
						}else{
							m[i].setRotation(gain * difference);
						}
						
						m[i].setTargetAngle(tantheta);				
						
						m[i].setVelX((float) (Math.sin(m[i].getTheta())*8)); //velocity set with new adjusted rotation
						m[i].setVelY((float) (Math.cos(m[i].getTheta())*8));
						
						m[i].updateLocation(); //update missile
						m[i].rotateShape();
					}
				}
		}
	}


	@Override
	public String getName() {
		return "Heat Seeking Missiles";
	}

}
