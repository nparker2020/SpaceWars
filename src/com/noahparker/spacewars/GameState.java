package com.noahparker.spacewars;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameState extends BasicGameState {
	int xres;
	int yres;
	int numplayers;
	int numprojectiles = 0;
	Player players[] = new Player[4];
	Projectile projectiles[] = new Projectile[20];
	int scores[] = new int[4];
	TrueTypeFont font;
	TrueTypeFont smallfont;
	CollisionListener collisions;
	Polygon lifeshape;
	boolean gameover = false;
	private int winner = -1;
	int stars[][] = new int [80][3];
	ItemPickup pickups[] = new ItemPickup[5];
	int numpickups = 0;
	Color blue = new Color(0, 191, 255);
	ParticleSystem system;
	Image image;
	File xml;
	int playersalive;
	ConfigurableEmitter explosionemitter;
	ConfigurableEmitter projectilemitter; //used for projectile collisions!
	int resetdelay = 0;
	boolean tickdelay = false;
	
	//WHEN ITEMS ARE ADDED TO THE GAME JUST CHECK IF ITEMPICKUP.DRAW IS FALSE THEN OVERWRITE!!!!!!!!!!**************
	//Add Delay before ship is respawned, so that particle effect works and so that it is more graceful
	
	public void setPlayers(Player players[], int numplayers) {
		this.numplayers = numplayers;
		this.players = players;
		collisions = new CollisionListener(this, players, scores, xres, yres, numplayers);
		System.out.println("Collision Listener Created: Players Set.");
		for(int i = 0; i<numplayers; i++) {
			this.players[i].setEmitter(xml, image);
			this.players[i].getEmitter().setEnabled(false);
			system.addEmitter(players[i].getEmitter());
		}
	}
	
	public void setResolution(int x, int y) {
		xres = x;
		yres = y;
		collisions = new CollisionListener(this, players, scores, xres, yres, numplayers);
		System.out.println("Collision Listener Created: Resolution Set");
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		font = new TrueTypeFont(new Font("Arial", Font.BOLD, 72), false);
		smallfont = new TrueTypeFont(new Font("Arial", Font.BOLD, 32), false);
		
		for(int i = 0; i<projectiles.length; i++) {
			 projectiles[i] = new Projectile(0, 0, 0, 0, 0, 0, false);
		}
		
		 for(int a = 0; a<numplayers; a++) {
			 scores[a] = 0;
		 }
		 
		 //image = new Image(this.getClass().getResourceAsStream("/com/noahparker/resources/particle_line.png"), "particle_line.png", false);
		 
		 image = new Image(this.getClass().getResourceAsStream("/com/noahparker/resources/particle_circle.png"), "particle_circle.png", false);
		 
		 system = new ParticleSystem(image, 4000);
		 system.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
		 system.setRemoveCompletedEmitters(false);
		 
		 
		 URL url = this.getClass().getResource("/com/noahparker/resources/test_emitter.xml");
		 URL fire = this.getClass().getResource("/com/noahparker/resources/flame.xml");
		 URL explosion = this.getClass().getResource("/com/noahparker/resources/explosion.xml");
		 
		 File firexml = null;
		 File explosionxml = null;
		 try {
			 xml = new File(url.toURI());
			 firexml = new File(fire.toURI());
			 explosionxml = new File(explosion.toURI());
			 projectilemitter = ParticleIO.loadEmitter(firexml);
			 explosionemitter = ParticleIO.loadEmitter(explosionxml);
		 } catch (URISyntaxException | IOException e) {
			System.out.println("Failed to load xml file for particle effects! Oh no!");
			e.printStackTrace();
		 }
		 
		 system.addEmitter(explosionemitter);
		 explosionemitter.setEnabled(false);
		 explosionemitter.setPosition(xres*2, yres*2, false);
		 system.addEmitter(projectilemitter);
		 
		 
		 lifeshape = new Polygon();
		
			lifeshape.addPoint(0, 0-25);
			lifeshape.addPoint(0+15, 0+15);
			lifeshape.addPoint(0, 0+15);
			lifeshape.addPoint(0-15, 0+15);
		
		Random r = new Random();
		for(int b = 0; b<80;b++) {
			stars[b][0] = r.nextInt(xres);
			stars[b][1] = r.nextInt(yres);
			stars[b][2] = r.nextInt(5);
		}
		
		System.out.println("***************** PICKUPS CREATED *************************");
		for(int c = 0; c<pickups.length;c++) {
			int x = r.nextInt((xres-200))+200;
			int y = r.nextInt(yres-200)+200;
			int item = r.nextInt(4);
			switch(item) {
			case 0:
				pickups[c] = new ItemHeatMissile(x,y,20,20);
				break;
			case 1:
				pickups[c] = new ItemLaserGun(x,y,20,20);
				break;
			case 2:
				pickups[c] = new ItemSurroundShield(x,y,20,20);
				break;
			case 3:
				pickups[c] = new ItemHeatMissile(x,y,20,20);
				break;
			default:
				pickups[c] = new ItemSurroundShield(x,y,20,20);
				break;
			}
			//pickups[c] = new ItemLaserGun(x, y, 20, 20);
			//pickups[c] = new ItemSurroundShield(x, y, 20, 20);
			
			System.out.println(pickups[c].getShape().toString());
		}
		System.out.println("************************************************************");
		//collisions.updatePickups(pickups);
		playersalive = numplayers;
		System.out.println("INIT RAN.");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		//g.setLineWidth(1);
		g.setAntiAlias(true);
		g.drawString("FPS: "+gc.getFPS(), 20, 30);
		//g.drawImage(image, 200, 200);
		/*
		 * Stars + Items
		 */
		for(int a = 0; a<80; a++) {
			g.setColor(Color.white);
			g.fillOval(stars[a][0], stars[a][1], stars[a][2], stars[a][2]);
		}
		for(int b = 0; b<pickups.length; b++) {
			if(pickups[b].getDraw())
			g.draw(pickups[b].getShape());
			g.drawString(pickups[b].getName(), pickups[b].getX(), pickups[b].getY());
		}
		
		/*
		 * Draws player's shape and UI -this should be moved internally to each player
		 * 	-or each player should be given unique UI coords, and draw all the same
		 */
		for(int i = 0; i<numplayers; i++) {
			g.setLineWidth(1);
			Player player = players[i];
			
			/*Player UI/Info*/
			player.drawUIBlock(g, lifeshape, font, smallfont);
			
			/*Player shape*/
			if(!player.isDestroyed()) {
				
				g.setLineWidth(5);
				g.setColor(player.getColor());
				g.draw(player.getShape());
				
				g.setLineWidth(1);
				g.setColor(Color.white);
				g.draw(player.getShape());
				
				//player.drawItem(g); *****************************************
				if(player.hasItem()) {
					player.getItem().render(g, player);
				}
				g.setColor(Color.white);
				g.draw(player.getFlame());
				
				if(player.isThrusting()) { //activate/deactivate particle emitter
					g.setLineWidth(5);
					g.setColor(Color.cyan);
					g.drawLine(player.getFlame().getPoint(4)[0], player.getFlame().getPoint(4)[1], player.getFlame().getPoint(2)[0], player.getFlame().getPoint(2)[1]);
				}
					
			}
				g.setColor(Color.white);
		}
		
		//Particle effects system
		system.render();
		
		//render score/whatever at player.getDisplayX(), player.getDisplayY();
	
		/*
		 * Drawing Lasers
		 */
		for(int a = 0; a<numprojectiles; a++) {
			//draw projectiles if draw==true
			if(projectiles[a].draw) {
				g.setLineWidth(5);
				g.setColor(players[projectiles[a].getID()].getColor());
			//	g.drawLine(projectiles[a].getX(), projectiles[a].getY(), projectiles[a].getX()+3*projectiles[a].getVelX(), projectiles[a].getY()-3*projectiles[a].getVelY());
				g.draw(projectiles[a].getLine());
				g.setLineWidth(1);
				g.setColor(Color.white);
				//g.drawLine(projectiles[a].getX(), projectiles[a].getY(), projectiles[a].getX()+3*projectiles[a].getVelX(), projectiles[a].getY()-3*projectiles[a].getVelY());
				g.draw(projectiles[a].getLine());
				//g.fillOval(projectiles[a].getX(), projectiles[a].getY(), 5, 5);
				
			}
		} 
		
		
		//font.drawString((xres/2)-font.getWidth("Player 0 won!"), yres/2, "Player "+(winner+1)+" Won!");
		if(gameover) {
			g.setColor(Color.white);
			smallfont.drawString((xres/2)-(smallfont.getWidth("Player 0 won!")/2), (yres/2)-(smallfont.getHeight("Player 0 Won!")/2), "Player "+(winner+1)+" Wins!");
		}
	
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		for(int i = 0; i<numplayers; i++) { //somehow write in reset() delay? check playersalive every tick?
			players[i].updateLocation();
			players[i].checkInput();
			if(players[i].hasItem()) {
				players[i].getItem().update(players, projectiles);
			}
		}
		for(int a = 0; a<numprojectiles; a++) {
			projectiles[a].addTraveled(1);
			projectiles[a].moveProjectile(); //translation of line shape!
			if(projectiles[a].getTravelled()>=50) { //player.getRange()?
				projectiles[a].setDraw(false);
			}
		}
		system.update(delta);
		//System.out.println("UPDATE RAN");
		collisions.check(projectiles, numprojectiles, pickups);
		
	}

	public void addProjectile(float x, float y, float velx, float vely, int damage, int ID) {
		//assign values to an array!
		for(int i = 0; i<numprojectiles;i++) { //overwrites projectile that has expired or is off screen
			if(!projectiles[i].draw || isOffScreen(projectiles[i].getX(), projectiles[i].getY())) {
				projectiles[i].setVector(x, y, velx, vely); //re-write into one function?
				projectiles[i].setLine(x, y, velx, vely);
				projectiles[i].setDamage(damage);
				projectiles[i].setID(ID);
				projectiles[i].setDraw(true);
				return;
			}
		}
			projectiles[numprojectiles].setVector(x, y, velx, vely); //if no overwrite found
			projectiles[numprojectiles].setLine(x, y, velx, vely);
			projectiles[numprojectiles].setDamage(damage);
			projectiles[numprojectiles].setID(ID);
			projectiles[numprojectiles].setDraw(true);
			numprojectiles += 1;
	}
	
	public void setProjectileCollisionEmitter(float x, float y) {
		projectilemitter.setPosition(projectilemitter.getX(), y, false);
		projectilemitter.setPosition(x, projectilemitter.getY(), false);
		projectilemitter.replay();
	}
	
	public void registerKill(Player dead, Player killer) {
		registerDestroy(dead);
		
		killer.addScore(1);
	}
	
	public void registerDestroy(Player destroyed) {
		destroyed.destroy();
		
		playersalive -= 1;
		
		if(playersalive<=1) { //somehow register a delay
			reset();
			//tickdelay = true;
		}
		
		
		explosionemitter.setEnabled(false); //explosion appearing in wrong place for some reason before moved to player...
		
		explosionemitter.setPosition(destroyed.getX(), explosionemitter.getY(), false);
		explosionemitter.setPosition(explosionemitter.getX(), destroyed.getY(), false);
		
		explosionemitter.setEnabled(true);
		explosionemitter.replay();
		
	}
	
	public float[] getSpawnPoints(int ID, int numplayers) {
		float array[] = new float[2];
		if(numplayers==2) {
			switch(ID) {
			case 0:
				array[0] = (xres/4);
				break;
			case 1:
				array[0] = (xres/4) * (3);
				break;
			}
			//array[0] = xres/4 * (ID+1);
			array[1] = (yres/2);
			//System.out.println("2 Player Game Started, y:"+(yres/2));
			return array;
		}else{
			if(ID<=1) {
				array[1] = yres/4; //top corners
			}else{
				array[1] = (yres/4)*3; //bottom corners
			}
		}
		switch(ID) {
		case 0:
			array[0] = xres/4;
		break;
		case 1:
			array[0] = (xres/4)*3;
			break;
		case 2:
			array[0] = xres/4;
			break;
		case 3:
			array[0] = (xres/4)*3;
			break;
		}
		
		return array;
	}
	
	public float[] getInfoDisplayPoints(int playerid, float spawnx) {
		float array[] = new float[2];
		
		switch(playerid) {
		case 0:
			array[1] = yres/32;
			array[0] = xres/36;
			/*array[1] = 40;
			array[0] = 20;*/
			break;
		case 1:
			/*array[1] = yres/10;
			array[0] = (xres/10)*9;
			*/
			array[1] = yres/32;
			array[0] = (xres/36)*35;
			break;
		case 2:
			array[1] = (yres/4)*3;
			break;
		case 3:
			array[1] = (yres/4)*3;
			break;
		}
		return array;
	}
	
	public void reset() { //reset game
		collisions.setPlayersAlive(numplayers);
		int playersalive = numplayers;
		int winner = -1;
		for(int i = 0; i<numplayers; i++) {
			if(players[i].getLives()==0) {
				playersalive-=1;
			}else{
				winner = players[i].getID();
			}
			players[i].reset();
		}
		if(playersalive==1) {
			System.out.println("Player "+winner+" Won!");
			gameover = true;
			this.winner = winner;
		}else{
			System.out.println("PlayersAlive: "+playersalive);
		}
	}
	
	public void keyPressed(int key, char c) {
		if(key==Input.KEY_ESCAPE) {
			restart();
		}
	}
	
	public void controllerButtonPressed(int controller, int button) {
		if(button==Util.BACK && controller == players[0].getID()) {
			restart();
		}
	}
	
	public void restart() {
		gameover = false;
		winner = -1;
		collisions.setPlayersAlive(numplayers);
		for(int i = 0; i<numplayers; i++) {
			players[i].restart();
		}
	}
	
	public boolean isOffScreen(float x, float y) {
		if(x>xres || x<0 || y<0 || y>yres) {
			return true;
		}
		return false;
	}
	
	public void tickReset() { //call from update?
		resetdelay++;
		if(resetdelay>=1000) {
			reset();
			resetdelay = 0;
		}
	}
	
	@Override
	public int getID() {
		return Util.SINGLEPLAYER_GAME_STATE;
	}

}
