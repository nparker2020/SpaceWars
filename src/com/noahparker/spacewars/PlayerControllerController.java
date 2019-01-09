package com.noahparker.spacewars;

import org.newdawn.slick.Input;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.state.BasicGameState;


public class PlayerControllerController extends PlayerController {
	Player player;
	Input input;
	InputProvider provider;
	int thruster;
	int fire;
	int controller;
	GameState game;
	boolean fired = false;
	//Input input;
	
	//PlayerController for handing Xbox controller - Needs work but works.
	public PlayerControllerController(GameState game, Input input, Player player, int controller) {
		this.player = player;
		//this.thruster = thruster;
		//this.fire = fire; //Button IDs, allows for customization
		this.controller = controller;
		this.input = input;
		this.game = game;
		//provider = new InputProvider(input);
		//provider.addListener(this);
		//input.addListener((InputListener) this);
		System.out.println("PlayerControllerController Created!");
		
		//this.input = input;
	} //Get controller ID, use that to get individual PlayerController!
	
	public int getID() {
		return controller;
	}
	
	public String getStartButton() {
		return "start";
	}
	
	@Override
	public void checkInput() {
		//use input.getAxisValue for triggers! noice!
		//maybe check right/left?
		/*for(int i = 0; i<input.getAxisCount(0);i++) {
			System.out.println("Axis "+i+" Equals: "+input.getAxisValue(0, i));
		}*/
		
		if(input.isButton1Pressed(controller)) {
			//System.out.println("A pressed."); //Fired boolean prevents "rapid  fire" 1 tap = 1 shot
			if(!fired) {
				float velx = player.getXVelocity();
				float vely = -player.getYVelocity();
				velx += Math.sin(player.getTheta())*10;
				vely += Math.cos(player.getTheta())*10;
				game.addProjectile(player.getTipPoint()[0], player.getTipPoint()[1], (float) velx, (float) vely, player.getDamage(), player.getID());
				//System.out.println("Fired.");
			}
			fired = true;
			
		}else{
			fired = false;
		}
		
		
		
		if(input.isControllerLeft(controller)) {
			player.setRotation(-0.08f);
			//game.one.theta -= 0.1f;
			player.setTheta(player.getTheta() - 0.08f);
			if(player.getTheta()<0.0f) {
				//player.setTheta((float) (player.getTheta() + 2*Math.PI));
				player.addTheta((float) (2*Math.PI));
				//System.out.println("2 PI added");
			}
			player.updateShape();
		}else{
			player.setRotation(0);
		}
		
		if(input.isControllerRight(controller)) {
			player.setRotation(0.08f);
			player.setTheta(player.getTheta() + 0.08f);
			if(player.getTheta()>2*Math.PI) { 
				//player.setTheta((float) (player.getTheta() - 2*Math.PI));
				player.addTheta((float) (-2*Math.PI));
			}
			player.updateShape();
		}else{
			player.setRotation(0);
		}
		
		if(input.getAxisValue(controller, Util.RIGHT_TRIGGER_AXIS)<-0.1) { //Trigger
			//thrusting
			if(input.getAxisValue(controller, Util.RIGHT_TRIGGER_AXIS)==-1.0) { //initial value is always 1.0
				return;
			}
			player.setThrusting(true);
			float vely = (float) Math.cos(player.getTheta())*(0.1f);
			float velx = (float) Math.sin(player.getTheta())*(0.1f);
			//System.out.println("velX:"+velx+", velY: "+vely);
			//game.one.velocity_x += velx;
			player.addXVelocity(velx);
			//game.one._y -= vely;
			player.addYVelocity(-vely);
			//System.out.println(input.getAxisValue(controller, 4));
		}else{
			player.setThrusting(false);
		}
		//check isControlPressed?
		
	}

	@Override
	public BasicGameState getGame() {
		return game;
	}
	
}
