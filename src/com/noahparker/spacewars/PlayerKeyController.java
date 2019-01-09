package com.noahparker.spacewars;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayerKeyController extends PlayerController implements InputProviderListener {
	Input input;
	InputProvider provider;
	private Command MOVE_RIGHT = new BasicCommand("MOVE_RIGHT");
	private Command MOVE_LEFT = new BasicCommand("MOVE_LEFT");
	private Command THRUSTER = new BasicCommand("THRUSTER");
	//private Command JUMP = new BasicCommand("JUMP");
	private Command FIRE = new BasicCommand("FIRE");
	private Command USEITEM = new BasicCommand("USEITEM");
	//SpaceWarsGame game;
	Player player;
	BasicGameState game;
	StateBasedGame g;
	int right;
	int left;
	int thruster;
	int fire;
	int useitem;
	
	//PlayerController for keyboard use. - Should be cleaned up/rewritten but works
	public PlayerKeyController(BasicGameState g, GameContainer gc, Player player, int right, int left, int thruster, int fire, int useitem) {
		//this.game = g;
		input = gc.getInput();
		this.right = right;
		this.left = left;
		this.thruster = thruster;
		this.fire = fire;
		this.useitem = useitem;
		this.player = player;
		
		if(g instanceof GameState) {
			this.game = (GameState) g;
		}else{
			this.game = (MultiplayerGameState) g;
		}
		
		provider = new InputProvider(input);
		
		//provider.bindCommand(new KeyControl(Input.KEY_SPACE), JUMP);
		provider.bindCommand(new KeyControl(thruster), THRUSTER);
		provider.bindCommand(new KeyControl(fire), FIRE);
		provider.bindCommand(new KeyControl(useitem), USEITEM);
		provider.addListener(this);
	}
	
	
	
	public PlayerKeyController(StateBasedGame game, GameContainer gc, Player player, int right, int left, int thruster, int fire) {
		input = gc.getInput();
		this.right = right;
		this.left = right;
		this.thruster = thruster;
		this.fire = fire;
		this.player = player;
		this.g = game;
	}
	
	public boolean hasKey(int key) {
		if(key==right || key==left || key==thruster || key==fire || key==useitem) {
			return true;
		}
		return false;
	}
	
	public String getStartButton() {
		return "Enter";
	}
	
	@Override
	public void controlPressed(Command cmd) {
		
		if(cmd.equals(FIRE)) {
			//Projectile p = new Projectile(player.getX(), player.getY(), (float) Math.sin(player.getTheta()), (float) Math.cos(player.getTheta()), player.getID());
			
			/*float velx = player.getXVelocity();
			float vely = -player.getYVelocity();
			*/
			float velx = (float) (Math.sin(player.getTheta())*10);
			float vely = (float) (Math.cos(player.getTheta())*10);
			
			
			velx += Math.sin(player.getTheta())*10;
			vely += Math.cos(player.getTheta())*10;
			
			if(game instanceof GameState) {
				((GameState) game).addProjectile(player.getTipPoint()[0], player.getTipPoint()[1], (float) velx, (float) vely, player.getDamage(), player.getID());
			}else{
				((MultiplayerGameState) game).addProjectile(player.getTipPoint()[0], player.getTipPoint()[1], (float) velx, (float) vely, player.getDamage(), player.getID());
			}
			
			//System.out.println("Fired.");
			
			//game.addProjectile(player.getX(), player.getY(), (float) Math.sin(player.getTheta())*6, (float) Math.cos(player.getTheta()*6));
			/*
			 * game.getNewProjectile(player.getX(), player.getY();
			 */
		}
		if(cmd.equals(USEITEM)) {
			//use item! (true); - for initiating
			if(player.hasItem()) {
				player.getItem().useItem(true);
			}
		}
	}

	public void checkInput() {
		
		if(input.isKeyDown(left)) {
			//game.one.rotation = -0.1f;
			player.setRotation(-0.08f);
			//game.one.theta -= 0.1f;
			player.setTheta(player.getTheta() - 0.08f);
			if(player.getTheta()<0.0f) {
				//player.setTheta((float) (player.getTheta() + 2*Math.PI));
				player.addTheta((float) (2*Math.PI));
				//System.out.println("2 PI added");
			}
			player.updateShape();
		}
		if(input.isKeyDown(right)) {
			//game.one.rotation = 0.1f;
			//game.one.theta += 0.1f;
			player.setRotation(0.08f);
			player.setTheta(player.getTheta() + 0.08f);
			if(player.getTheta()>2*Math.PI) { 
				//player.setTheta((float) (player.getTheta() - 2*Math.PI));
				player.addTheta((float) (-2*Math.PI));
			}
			player.updateShape();
		}
		if(input.isKeyDown(thruster)) {
			//game.one.acceleration_x += 0.01;
			player.setThrusting(true);
			float vely = (float) Math.cos(player.getTheta())*(0.1f);
			float velx = (float) Math.sin(player.getTheta())*(0.1f);
			//System.out.println("velX:"+velx+", velY: "+vely);
			//game.one.velocity_x += velx;
			player.addXVelocity(velx);
			//game.one._y -= vely;
			player.addYVelocity(-vely);
			//System.out.println("Theta: "+game.one.theta);	
		}
		
	}
	
	@Override
	public void controlReleased(Command cmd) {
		if(cmd.equals(MOVE_LEFT)) {
			//game.one.rotation = 0;
			player.setRotation(0);
		}
		if(cmd.equals(MOVE_RIGHT)) {
			//game.one.rotation = 0;
			player.setRotation(0);
		}
		if(cmd.equals(THRUSTER)) {
			//game.one.thrusting = false;
			player.setThrusting(false);
		}
		if(cmd.equals(USEITEM)) {
			//use item (false); - ending
			if(player.hasItem()) {
				player.getItem().useItem(false);
			}
			
		}
	}



	@Override
	public BasicGameState getGame() {
		return game;
	}

}
