package com.noahparker.spacewars;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.state.BasicGameState;

public class CollisionListener {
	BasicGameState state;
	private Player players[] = new Player[4];
	private float resx;
	private float resy;
	private float numplayers;
	private int playersalive;
	
	public CollisionListener(BasicGameState state, Player players[], int scores[], float resx, float resy, float numplayers) {
		this.state = state;
		this.players = players;
		this.resx = resx;
		this.resy = resy;
		this.numplayers = numplayers;
		playersalive = (int) numplayers;
	}
	
	public void setPlayersAlive(int pa) {
		playersalive = pa;
	}
	
	public void check(Projectile projectiles[], int numprojectiles, ItemPickup[] pickups) {
		
		for(int i = 0; i<numplayers; i++) {
			Player player = players[i];
				if(player.isDestroyed()) {
					continue;
				}
				checkBounds(player);
				for(int a = 0; a<numplayers; a++) {
					Player p = players[a];
					if(p.isDestroyed()) {
						continue;
					}
					if(player!=p) {
						if(player.getShape().intersects(p.getShape())) {
							/*player.destroy();
							p.destroy();
							playersalive -= 2;
							if(playersalive<=1) {
								((GameState) state).reset();
							}*/
							((GameState) state).registerDestroy(p);
							((GameState) state).registerDestroy(player);
						}
						if(player.hasItem() && player.getItem().getActive()) { //if a player has an item, and if it is active
							//iterate through item.getActiveshapes()
							for(Shape s : player.getItem().getActiveShapes()) {
								if(s==null) {
									continue;
								}
								if(p.getShape().intersects(s)) { //when item is being used
									player.getItem().intersectPlayer(p, player, s);
								}
							}
							/*if(p.getShape().intersects(player.getItem().getActiveShape())) {
								player.getItem().intersectPlayer(p, player); //right? -needs to be tested. -WORKS!
								//not always going destroy/kill... so how to adjust score?
							}*/
						}
					}
			}
				
			for(ItemPickup p: pickups) {
				if(p.getDraw() && !player.hasItem() && player.getShape().intersects(p.getShape())) {
					p.setDraw(false);
					p.onItemPickup(player);
					player.addItem(p);
				}
			}
		}
		for(int a = 0; a<numprojectiles; a++) {
			//iterate through projectiles and check ships - May be slow and will need to check distance before calling intersects()
			if(!projectiles[a].draw) {
				continue;
			}
 			for(int b = 0; b<numplayers; b++) {
				
 				if(players[b].isDestroyed()) {
 					continue;
 				}
 				
 				if(players[b].getShape().intersects(projectiles[a].getLine())) {
					projectiles[a].draw = false; //re-write to be in one function within gamestate (setProjectileEmitter() probable)
					players[b].changeHealth(-projectiles[a].getDamage());
					((GameState) state).setProjectileCollisionEmitter(projectiles[a].getLine().getPoint(1)[0], projectiles[a].getLine().getPoint(1)[1]);
					
					if(players[b].getHealth()<=0) {
						/*players[b].destroy();
						players[projectiles[a].getID()].addScore(1);
						playersalive -= 1;
						if(playersalive==1) {
							((GameState) state).reset();
						}*/
						((GameState) state).registerKill(players[b], players[projectiles[a].getID()]);
					}
				}
				if(players[b].hasItem() && players[b].getItem().getActive() /*&& players[b].getItem().getActiveShape().intersects(projectiles[a].getLine())*/) {
					for(Shape s : players[b].getItem().getActiveShapes()) {
						if(s==null)
							continue;
						if(s.intersects(projectiles[a].getLine())) {
							players[b].getItem().intersectProjectile(projectiles[a]);
						}
					}
				}
			}
		}
	}
	
	public void checkBounds(Player player) {
		if(player.getShape().getCenterX()<0) {
			player.updateShape(player.getShape().transform(Transform.createTranslateTransform(resx, 0)));
			player.setX(resx);
		}
		if(player.getShape().getCenterY()<0) {
			player.updateShape(player.getShape().transform(Transform.createTranslateTransform(0, resy)));
			player.setY(resy);
		}
		if(player.getShape().getCenterX()>resx) {
			player.updateShape(player.getShape().transform(Transform.createTranslateTransform(-resx, 0)));
			player.setX(0);
			System.out.println("resX set.");
		}
		if(player.getShape().getCenterY()>resy) {
			player.updateShape(player.getShape().transform(Transform.createTranslateTransform(0, 0)));
			player.setY(0);
		}
	}
	
}
