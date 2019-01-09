package com.noahparker.spacewars;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SingleplayerMenuState extends BasicGameState {
	Menu play;
	TrueTypeFont font;
	int numplayers = 0;
	Player players[] = new Player[4];
	Button p[] = new Button[4];
	//GameState game;
	SpaceWars main;
	GameContainer gc;
	boolean allowstart = false;
	/*public void setGameState(GameState game) {
		this.game = game;
	}*/
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException { //UPDATE TO USE RESOLUTION SPECIFIC VALUES!
		play = new Menu();
		play.addButton(0, 0, 480, 1080, "Press any button to join!", 0);
		play.addButton(480, 0, 480, 1080, "Press any button to join!", 2);
		play.addButton(960, 0, 480, 1080, "Press any button to join!", 3);
		play.addButton(1440, 0, 480, 1080, "Press any button to join!", 4);
		font = new TrueTypeFont(new Font("Arial", Font.BOLD, 24), false);
		this.gc = gc;
		//this.game = (GameState) game.getState(Util.GAME_STATE);
		this.main = (SpaceWars) game;

		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		
		for(int i = 0; i<play.getNumberButtons(); i++) {
			g.draw(play.getButton(i).getShape());
			//DRAW TEXT!
			font.drawString(play.getButton(i).getShape().getCenterX()-150, play.getButton(i).getShape().getCenterY(), play.getButton(i).getText());
		}
		
		if(allowstart) {
			font.drawString(1845-(font.getWidth("Press start!")/2), 1000, "Press "+players[0].getController().getStartButton()+"!");
		}
		
		for(int a = 0; a<numplayers; a++) {
			g.setColor(players[a].getColor());
			g.fill(p[a].getShape());
			g.setColor(Color.black);
			font.drawString(p[a].getShape().getCenterX()-(font.getWidth(p[a].getText())/2), p[a].getShape().getCenterY(), p[a].getText(), Color.black);
			g.setColor(Color.white);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		for(int a = 0; a<numplayers; a++) {
			if(p[a].getShape().getCenterY()+(p[a].getShape().getHeight()/2)!=1080) {
				p[a].setShape(p[a].getShape().transform(Transform.createTranslateTransform(0, 40)));
				//System.out.println(p[a].getShape().getCenterY());
			}else{
				//System.out.println("False");
			}
		}
	}	
	
	public void controllerButtonPressed(int controller, int button) {
		if(numplayers==4) {
			return;
		}
		
		if(numplayers>=1) {
			allowstart = true;
		}
		
		System.out.println(button);
		if(players[0]!=null)
		if(controller==players[0].getID()) { //if controller is player 1 -this needs to be fixed (controller 0 not necessarily player 0
			if(button==Util.START && allowstart) {
				//start the game!
				//this doesn't work! -pretty sure it does, should be tested!
				
				for(int i = 0; i<numplayers; i++) { //update spawn points based on final player count
					players[i].setX(((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getSpawnPoints(players[i].getID(), numplayers)[0]);
					players[i].setY(((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getSpawnPoints(players[i].getID(), numplayers)[1]);
					players[i].setDisplayPoints(((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getInfoDisplayPoints(players[i].getID(), players[i].getSpawnX()));
					System.out.println("ID:"+players[i].getID());
					System.out.println("Y:"+players[i].getY());
				}
				System.out.println("Y:"+players[0].getY());
				((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).setPlayers(players, numplayers);
				//this.setGameState(game);
				main.enterState(Util.SINGLEPLAYER_GAME_STATE);
				System.out.println("Game Started!");
			}else{
				System.out.println("Button did not match");
			}
		}else{
			System.out.println("Controller did not match");
		}
		
		if(playerEntered(controller)) {
			return;
		}
		
		addPlayer(controller, true, 0, 0, 0, 0, 0);
		
		/*Player player = new Player(((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getSpawnPoints(controller)[0], ((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getSpawnPoints(controller)[1], numplayers);
		players[numplayers] = player;
		PlayerController playercontroller;
		switch(numplayers) {
		case 0:
			
				p[numplayers] = new Button(0, -1080, 480, 1080, "Player "+(numplayers+1)+" Joined!", 0);
				numplayers += 1;
				System.out.println("Player "+numplayers+" Created.");
				//PLAYER ONE, SET PLAYER SPAWN POINTS!
				
			//Player player = new Player(0, 0, 0);
			//create PlayerControllerController objects (probably should be under just PlayerController)
				//playercontroller = new PlayerControllerController(game, player, controller);
			//Add to GameState?
			break;
		case 1:
			
				p[numplayers] = new Button(480, -1080, 480, 1080, "Player "+(numplayers+1)+"Joined!", 1);
				numplayers += 1;
				
			break;
		case 2:
			
				p[numplayers] = new Button(960, -1080, 480, 1080, "Player "+(numplayers+1)+"Joined!", 2);
				numplayers += 1;
			
			
			break;
		case 3:
			
				p[numplayers] = new Button(1440, -1080, 480, 1080, "Player "+(numplayers+1)+"Joined!", 3);
				numplayers += 1;
			
			break;
		}
		playercontroller = new PlayerControllerController((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE), gc.getInput(), player, controller);
		player.setController(playercontroller);*/
	}
	
	public void keyPressed(int key, char c) {
		if(key==Input.KEY_ENTER && allowstart) {
			

			for(int i = 0; i<numplayers; i++) { //update spawn points based on final player count
				players[i].setSpawnX(((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getSpawnPoints(players[i].getID(), numplayers)[0]);
				players[i].setSpawnY(((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getSpawnPoints(players[i].getID(), numplayers)[1]);
				players[i].setDisplayPoints(((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getInfoDisplayPoints(players[i].getID(), players[i].getSpawnX()));
				System.out.println("ID:"+players[i].getID());
			}
			System.out.println("Y:"+players[0].getY());
			
			
			((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).setPlayers(players, numplayers);
			//this.setGameState(game);
			main.enterState(Util.SINGLEPLAYER_GAME_STATE);
			System.out.println("Game Started!");
		}
		
		if(numplayers==4) {
			return;
		}
		
		if(numplayers>=1) {
			allowstart = true;
		}
		
		
		if(key==Input.KEY_A || key==Input.KEY_S || key==Input.KEY_W || key==Input.KEY_D) {
			//first control group
			//p[numplayers] = new Button(0, -1080, 480, 1080, "Player "+(numplayers+1)+" Joined!", 0);
			if(playerKeyEntered(Input.KEY_A)) {
				return;
			}
			addPlayer(0, false, Input.KEY_D, Input.KEY_A, Input.KEY_LSHIFT, Input.KEY_X, Input.KEY_C);
		}else if(key==Input.KEY_J || key==Input.KEY_K || key==Input.KEY_L || key==Input.KEY_I) {
			if(playerKeyEntered(Input.KEY_J)) { //player already been registered
				return;
			}
			addPlayer(0, false, Input.KEY_L, Input.KEY_J, Input.KEY_B, Input.KEY_PERIOD, Input.KEY_SLASH);
		}
		
		if(key==Input.KEY_ESCAPE) {
			main.enterState(Util.PLAY_MENU_STATE);
		}
	}
	
	public void addPlayer(int controllerid, boolean controller, int right, int left, int thruster, int fire, int useitem) {
		Player player = new Player(((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getSpawnPoints(numplayers, numplayers)[0], ((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE)).getSpawnPoints(numplayers, numplayers)[1], numplayers, getColor(numplayers));
		players[numplayers] = player;
		PlayerController playercontroller;
		switch(numplayers) {
		case 0:
			
				p[numplayers] = new Button(0, -1080, 480, 1080, "Player "+(numplayers+1)+" Joined!", 0);
				numplayers += 1;
				System.out.println("Player "+numplayers+" Created.");
				//PLAYER ONE, SET PLAYER SPAWN POINTS!
				
			//Player player = new Player(0, 0, 0);
			//create PlayerControllerController objects (probably should be under just PlayerController)
				//playercontroller = new PlayerControllerController(game, player, controller);
			//Add to GameState?
			break;
		case 1:
			
				p[numplayers] = new Button(480, -1080, 480, 1080, "Player "+(numplayers+1)+" Joined!", 1);
				numplayers += 1;
				
			break;
		case 2:
			
				p[numplayers] = new Button(960, -1080, 480, 1080, "Player "+(numplayers+1)+" Joined!", 2);
				numplayers += 1;
			
			
			break;
		case 3:
			
				p[numplayers] = new Button(1440, -1080, 480, 1080, "Player "+(numplayers+1)+" Joined!", 3);
				numplayers += 1;
			
			break;
		}
		if(controller) {
			playercontroller = new PlayerControllerController((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE), gc.getInput(), player, controllerid);
		}else{ //keyboard
			playercontroller = new PlayerKeyController((GameState) main.getState(Util.SINGLEPLAYER_GAME_STATE), gc, player, right, left, thruster, fire, useitem); //get Key IDs!
		}
		player.setController(playercontroller);
	}
	
	boolean playerEntered(int ID) {
		for(int i = 0; i<numplayers; i++) {
			if(p[i].getID()==ID) {
				return true;
			}
		}
		return false;
	}
	
	Color getColor(int id) {
		switch(id) {
		case 0:
			return Color.blue;
		case 1:
			return Color.red;
		case 2:
			return Color.magenta;
		case 3:
			return Color.green;
		}
		return Color.black;
	}
	
	boolean playerKeyEntered(int key) {
		for(int i = 0; i<numplayers; i++) {
			if(((PlayerKeyController) players[i].getController()).hasKey(key)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return Util.SINGLE_PLAY_MENU_STATE;
	}

}
