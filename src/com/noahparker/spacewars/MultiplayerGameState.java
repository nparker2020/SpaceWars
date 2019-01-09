package com.noahparker.spacewars;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.noahparker.multiplayer.MultiplayerClient;
import com.noahparker.multiplayer.MultiplayerPlayer;
import com.noahparker.multiplayer.MultiplayerUtils;

public class MultiplayerGameState extends BasicGameState {
	MultiplayerClient client;
	MultiplayerPlayer players[] = new MultiplayerPlayer[4];
	Player player;
	float data[][] = new float[4][6];//holds packet data
	boolean moved[] = {false, false, false, false};
	int numplayers;
	int packetupdate = 0;
	GameContainer gc;
	StateBasedGame game;
	//Keep one normal Player object (for host), send playerdata every ~30 game loops
	
	/*public MultiplayerGameState(MultiplayerClient client, Player player, MultiplayerPlayer players[], int numplayers) {
		this.client = client;
		this.player = player; //one local player (host)
		this.players = players; //players[0] is always the host!
		this.numplayers = numplayers;
		//NUMPLAYERS = PLAYERS NOT INCLUDING HOST (MAX 3)
	}*/
	
	/*public void setupConnection(String host, int port, Player player, MultiplayerPlayer players[], int numplayers) {
		client = new MultiplayerClient(this, host, port);
		
		this.player = player;
		this.players = players;
		this.numplayers = numplayers;
	}*/
	public void setupConnection(String host, int port) { //is this useful? NEED FUNCTION THAT PROVIDES PLAYER DATA TO CREATE LOCAL OBJECT 
		client = new MultiplayerClient(this, host, port);
		
		/*this.player = player;
		this.players = players;
		this.numplayers = numplayers;*/
	}
	
	//this is called from client!
	public void updatePlayerdata(float[] d) { //6 floats
		int player = (int) d[5];
		data[player] = d; //updates player values in data[][]
		moved[player] = true;
		players[player].setX(d[0]);
		players[player].setY(d[1]);
		players[player].setXVelocity(d[2]);
		players[player].setYVelocity(d[3]);
		players[player].setTheta(d[4]);
		
	}
	
	public void EnterMultiPlayerGame(int numplayers, float x, float y, int ID) {
		player = new Player(x, y, ID, Color.red);
		player.setController(new PlayerKeyController(this, gc, player, Input.KEY_D, Input.KEY_A, Input.KEY_LSHIFT, Input.KEY_X, Input.KEY_C));
		//player.setX(x);
		//player.setY(y);
		this.numplayers = numplayers;
		for(int i = 0; i<this.numplayers;i++) {
			players[i] = new MultiplayerPlayer(-50+(-20*i), -50+(-20*i), i); //will this overwrite/corrupt id?
		}
		
		game.enterState(Util.MULTIPLAYER_GAME_STATE);
	}
	
	public MultiplayerClient getClient() {
		return client;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException {
		this.gc = gc;
		this.game = state;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.setLineWidth(5);
		if(!player.isDestroyed()) {
			g.draw(player.getShape());
		}
		for(int i = 0; i<numplayers;i++) {
			g.draw(players[i].getShape());
			//System.out.println("Player ID: "+i+", X: "+players[i].getX()+", Y: "+players[i].getY());
			g.drawString("Player "+numplayers+" X:"+players[i].getX(), 50, 100+(10*i));
			g.drawString("Player "+numplayers+" Y:"+players[i].getY(), 50, 100+(30*i));
		}
		
	}

	public void addProjectile(float x, float y, float velx, float vely, int damage, int id) {
		//finish this code!
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		player.updateLocation();
		player.checkInput();
		for(int i = 0; i<numplayers; i++) {
			/*if(moved[i]) {
				//update player variables
			}*/
			players[i].updateLocation();
		}
		
		/*packetupdate += 1;
		if(packetupdate == 200) { //sends player data to server - should also assign aquired values?
			packetupdate = 0;
			client.sendPacket(new float[]{MultiplayerUtils.PACKET_PLAYER_DATA, player.getX(), player.getY(), player.getXVelocity(), player.getYVelocity(), player.getTheta(), player.getID()});
			for(int a = 0; a<numplayers;a++) { //updating local values with values recieved from server
				players[a].updateData(data[a]);
			}
		}*/
	}
	
	public void keyPressed(int key, char c) {
		System.out.println("Key "+key+" pressed!");
		if(key==Input.KEY_P) {
			client.sendPacket(new float[]{MultiplayerUtils.PACKET_PLAYER_DATA, player.getX(), player.getY(), player.getXVelocity(), player.getYVelocity(), player.getTheta(), player.getID()});
		}
	}
	
	@Override
	public int getID() {
		return Util.MULTIPLAYER_GAME_STATE;
	}

}
