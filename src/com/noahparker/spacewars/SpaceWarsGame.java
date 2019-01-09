package com.noahparker.spacewars;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class SpaceWarsGame extends BasicGame {

	static AppGameContainer agc;
	Player players[] = new Player[4];
	float numplayers;
	int numprojectiles = 0;
	CollisionListener collisions;
	float spawnlocs[][] = new float[4][4];
	Projectile projectiles[] = new Projectile[16];
	//ArrayList<Projectile> projectiles = new ArrayList<Projectile>(20);
	//implement some way to keep track of projectiles
	
	public SpaceWarsGame(String title, int numplayers) {
		super(title);
		this.numplayers = numplayers;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
	
		for(int i = 0; i<numplayers; i++) {
			Player player = players[i];
				if(!player.isDestroyed()) {
 					g.draw(player.getShape());
 					//g.drawString("X: "+player.getX()+", Y:"+player.getY()+", velX:"+player.getXVelocity()+", velY:"+player.getYVelocity(), 400, 400*player.getID());
 					if(player.isThrusting()) {
 						g.draw(player.getFlame());
 					}
				}
		}
		
		for(int a = 0; a<numprojectiles; a++) {
			Projectile p = projectiles[a];
			g.fillRect(p.getX(), p.getY(), 2, 2);
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		if(numplayers==2) { //use AGC to get resolution!
			float x1 = agc.getScreenWidth()/4;
			float x2 = (agc.getScreenWidth()/4)*3;
			float y1 = agc.getScreenHeight()/4;
			float y2 = (agc.getScreenHeight()/4)*2;
			spawnlocs[0][0] = x1;
			spawnlocs[0][1] = y1;
			spawnlocs[1][0] = x2;
			spawnlocs[1][1] = y2;
		}else{
			//generate spawn locations for 4 players instead of two.
		}
		
		for(int i = 0; i<numplayers; i++) {
			//Player player = new Player(spawnlocs[i][0], spawnlocs[1][1], i+1); //gets spawn location
			System.out.println("Player "+(i+1)+" Created.");
			//players[i] = player;
		}
		
		//collisions = new CollisionListener(players, agc.getScreenWidth(), agc.getScreenHeight(), numplayers);
		for(Player player : players) {
			if(player != null) {
				PlayerKeyController controller = null;
				switch(player.getID()) {
				case 1:
					//controller = new PlayerKeyController(this, gc, player, Input.KEY_D, Input.KEY_A, Input.KEY_LSHIFT, Input.KEY_X);
					break;
				case 2:
					//controller = new PlayerKeyController(this, gc, player, Input.KEY_L, Input.KEY_J, Input.KEY_B, Input.KEY_COMMA);
				break;
				case 3:
					//controller = new PlayerController(this, gc, player, Input.)
					break;
				}
				player.setController(controller);
			}
		}
	}

	@Override
	public void update(GameContainer gc, int arg) throws SlickException {
		
		for(int i = 0; i<numplayers; i++) {
				players[i].updateLocation();
				players[i].checkInput();
		}
		
		for(int a = 0; a<numprojectiles; a++) {
			projectiles[a].addX(projectiles[a].getVelX()*6);
			projectiles[a].addY(-projectiles[a].getVelY()*6);
			if((players[projectiles[a].getID()].getY()-projectiles[a].getY())/(players[projectiles[a].getID()].getX()-projectiles[a].getX())>100) {
				removeProjectile(projectiles[a]);
			}
		}
		
		//collisions.check();
	}
	
	public Player getPlayer(int i) {
		return players[i];
	}
	
	void addProjectile(Projectile p) {
		projectiles[numprojectiles] = p;
		p.setIndex(numprojectiles);
		numprojectiles += 1;
	}
	
	void removeProjectile(Projectile p) {
		projectiles[p.getIndex()] = null;
		numprojectiles -= 1;
	}
	
	int getNumProjectiles() {
		return numprojectiles;
	}

	public static void main(String args[]) {
		
		try {
			agc = new AppGameContainer(new SpaceWarsGame("Space Wars", 2));
			agc.setDisplayMode(1920, 1080, false);
			agc.setVSync(true);
			agc.setTargetFrameRate(60);
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
}
