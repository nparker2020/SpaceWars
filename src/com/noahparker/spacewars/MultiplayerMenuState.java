package com.noahparker.spacewars;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.noahparker.multiplayer.MultiplayerClient;
import com.noahparker.multiplayer.MultiplayerServer;

public class MultiplayerMenuState extends BasicGameState {
	MultiplayerServer server;
	MultiplayerClient client;
	TrueTypeFont font;
	Menu options;
	Menu ServerLobby;
	Button lobbylabel;
	Button current;
	TextField CLIENT_IP;
	TextField CLIENT_PORT;
	TextField SERVER_PORT;
	boolean join = false;
	boolean host = false;
	StateBasedGame game;
	Rectangle iprect;
	Rectangle portrect;
	Rectangle serverportrect;
	Input input;
	
	//Local player object needs their controller to be set!
	
	public MultiplayerMenuState() {
		options = new Menu();
		options.addButton(100, 100, 200, 100, "Host", 0);
		options.addButton(100, 250, 200, 100, "Join:", 1);	
		options.addButton(500, 250, 100, 100, "Join Server", 2);
		options.addButton(500, 100, 100, 100, "Launch Server", 3);
		ServerLobby = new Menu();
		ServerLobby.addButton(100,100,200,100, "SERVER_LOBBY:", 0);
		//lobbylabel = new Button(100,200,200,100, "SERVER_LOBBY:", 0);
		ServerLobby.addButton(100,300,200,100, "Player 1:", 1);
		ServerLobby.addButton(100,500,200,100, "Player 2:", 2);
		ServerLobby.addButton(100,700,200,100, "Player 3:", 3);
		ServerLobby.addButton(100,900,200,100, "Player 4:", 4);
		ServerLobby.addButton(300,900,200,100, "Start Game!", 5);
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		font = new TrueTypeFont(new Font("Arial", Font.BOLD, 24), false);
		CLIENT_IP = new TextField(gc, font, 100, 360, 500, 30);
		CLIENT_IP.setBorderColor(Color.white);
		CLIENT_IP.setBackgroundColor(Color.white);
		CLIENT_IP.setTextColor(Color.black);
		iprect = new Rectangle(CLIENT_IP.getX(), CLIENT_IP.getY(), CLIENT_IP.getWidth(), CLIENT_IP.getHeight());
		CLIENT_PORT = new TextField(gc, font, 610, 360, 200, 30);
		CLIENT_PORT.setBorderColor(Color.white);
		CLIENT_PORT.setBackgroundColor(Color.white);
		CLIENT_PORT.setTextColor(Color.black);
		SERVER_PORT = new TextField(gc, font, 100, 200, 500, 30);
		SERVER_PORT.setBorderColor(Color.white);
		SERVER_PORT.setBackgroundColor(Color.white);
		SERVER_PORT.setTextColor(Color.black);
		portrect = new Rectangle(CLIENT_PORT.getX(), CLIENT_PORT.getY(), CLIENT_PORT.getWidth(), CLIENT_PORT.getHeight());
		serverportrect = new Rectangle(SERVER_PORT.getX(), SERVER_PORT.getY(), SERVER_PORT.getWidth(), SERVER_PORT.getHeight());
		this.game = game;
		this.input = gc.getInput();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		
		
		
		
		//g.setColor(Color.white);
		if(!join && !host) {
			for(int i = 0; i<options.getNumberButtons(); i++){
				font.drawString(options.getButton(i).getShape().getCenterX()-font.getWidth(options.getButton(i).getText())/2, options.getButton(i).getShape().getCenterY(), options.getButton(i).getText());
			}
			
			CLIENT_IP.render(gc, g);
			CLIENT_PORT.render(gc, g);
			SERVER_PORT.render(gc, g);
		}
		
		if(join) {
			//Get player/server lobby data from server!
			//font.drawString(ServerLobby.getButton(0).getShape().getCenterX(), ServerLobby.getButton(0).getShape().getCenterY(), ServerLobby.getButton(0).getText());
			
			for(int i = 0; i<ServerLobby.getNumberButtons(); i++){
				font.drawString(ServerLobby.getButton(i).getShape().getCenterX()-font.getWidth(ServerLobby.getButton(i).getText())/2, ServerLobby.getButton(i).getShape().getCenterY(), ServerLobby.getButton(i).getText());
			}
			
			g.drawString("Contents of player:"+client.getPlayersJoined()[0]+", "+client.getPlayersJoined()[1]+", "+client.getPlayersJoined()[2]+", "+client.getPlayersJoined()[3], 400, 400);
			
			for(int i = 0; i<client.getPlayersJoined().length; i++) {
				if(i>0 && client.getPlayersJoined()[i-1]!=-1) {
					font.drawString(ServerLobby.getButton(i).getShape().getCenterX()+200, ServerLobby.getButton(i).getShape().getCenterY(), "Joined!");
					//this called -1? -fixed I think 
				}
				
				/*if(client.getPlayersJoined()[i]!=-1) {
					//font.drawString(ServerLobby.getButton(client.getPlayersJoined()[i]).getShape().getCenterX()+200, ServerLobby.getButton(client.getPlayersJoined()[i]).getShape().getCenterY(), "Joined!");
				}else{
					//System.out.println("Contents of player:"+client.getPlayersJoined()[0]+", "+client.getPlayersJoined()[1]+", "+client.getPlayersJoined()[2]+", "+client.getPlayersJoined()[3]);
				}*/
			}
		}
		
		if(host && client!=null) {
			//font.drawString(lobbylabel.getShape().getX(), lobbylabel.getShape().getY(), lobbylabel.getText());
			//System.out.println(lobbylabel.getShape().getY());
			g.drawString("Contents of player:"+client.getPlayersJoined()[0]+", "+client.getPlayersJoined()[1]+", "+client.getPlayersJoined()[2]+", "+client.getPlayersJoined()[3], 400, 400);

			
			for(int i = 0; i<ServerLobby.getNumberButtons(); i++){
				font.drawString(ServerLobby.getButton(i).getShape().getCenterX()-font.getWidth(ServerLobby.getButton(i).getText())/2, ServerLobby.getButton(i).getShape().getCenterY(), ServerLobby.getButton(i).getText());
				if(i>0 && server.getPlayer(i-1)!=-1) {
					//g.fill(ServerLobby.getButton(i).getShape());
					//System.out.println("Joined drawn");
					g.setColor(Color.white);
					
					font.drawString(ServerLobby.getButton(i).getShape().getCenterX()+200, ServerLobby.getButton(i).getShape().getCenterY(), "Joined!");
					//System.out.println(ServerLobby.getButton(i).getText()+", X: "+ServerLobby.getButton(i).getShape().getCenterX()+", Y: "+ServerLobby.getButton(i).getShape().getCenterY());
				}
			}
			
		}
			
		if(current!=null) {
			g.draw(current.getShape());
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
		//System.out.println("MOUSE X"+x+", MOUSE Y: "+y);
	}
	
	public void mousePressed(int button, int x, int y) {
		//System.out.println("Mouse clicked!");
		//x = input.getMouseX();
		
		if(portrect.contains(x,y)) {
			CLIENT_PORT.setFocus(true);
		}else{
			CLIENT_PORT.setFocus(false);
		}
		
		if(iprect.contains(x,y)) {
			CLIENT_IP.setFocus(true);
		}else{
			CLIENT_IP.setFocus(false);
			//System.out.println("Click not in rect, X:"+x+", Y:"+y);
		}
		
		if(serverportrect.contains(x,y)) {
			SERVER_PORT.setFocus(true);
		}else{
			SERVER_PORT.setFocus(false);
		}
		
		if(options.getButton(2).getShape().contains(x,y)) {
			String ip = CLIENT_IP.getText();
			String port = CLIENT_PORT.getText();
			System.out.println("Join Server clicked!");
			if(isValidIP(ip, port)) {
				//client = new MultiplayerClient((MultiplayerGameState) game.getState(Util.MULTIPLAYER_GAME_STATE), ip, Integer.parseInt(port));
				((MultiplayerGameState) game.getState(Util.MULTIPLAYER_GAME_STATE)).setupConnection(ip, Integer.parseInt(port));
				client = ((MultiplayerGameState) game.getState(Util.MULTIPLAYER_GAME_STATE)).getClient();
				//instead of this call setupConnection();
				System.out.println("Client set.");
				join = true;
			}else{
				System.out.println("Not a valid ip!");
			}
		}
		if(options.getButton(3).getShape().contains(x,y)) {
			//launch server
			
			String port = SERVER_PORT.getText();
			if(isValidPort(port)) {
				server = new MultiplayerServer(Integer.parseInt(port));
				//client = new MultiplayerClient((MultiplayerGameState) game.getState(Util.MULTIPLAYER_GAME_STATE), "localhost", Integer.parseInt(port));
				((MultiplayerGameState) game.getState(Util.MULTIPLAYER_GAME_STATE)).setupConnection("localhost", Integer.parseInt(port));
				client = ((MultiplayerGameState) game.getState(Util.MULTIPLAYER_GAME_STATE)).getClient();
				host = true;
			}else{
				System.out.println("Not a valid Port!");
			}
			
		}
		if(ServerLobby.getButton(5).getShape().contains(x,y) && host==true) {
			//start game!
			//send a start game packet?
			server.sendGameStartPacket();
		}
		
	}
	
	public void keyPressed(int key, char c) {
		//go through all possible controls, set current
			/*if(current==null) {
				current = options.getButton(0);
				return;
			}
		
			//check KEY_RIGHT to set focus for CLIENT_PORT?
			
			switch(key) {
			case Input.KEY_DOWN:
				current = options.moveDown();
				//check if at bottom, then set focus to CLIENT_IP
				break;
			case Input.KEY_UP:
				current = options.moveUp();
				break;
			case Input.KEY_ENTER:
				//selectButton(current);
				//System.out.println("Enter pressed.");
				/*if(current.ID== 0) { //Host
					//setup server
					host = true;
				}else{ //Join
					//draw textbox
					join = true;
					CLIENT_IP.setFocus(true);
					//game.getState(Util.MULTCLIENT_IPLAYER_GAME_STATE).setupConnection(host, port);
				}
				if(isValidCLIENT_IP(CLIENT_IP.getText())) {
					CLIENT_PORT.setFocus(true);
				}
				break;
			}*/
		if(key==Input.KEY_ESCAPE) {
			game.enterState(Util.PLAY_MENU_STATE);
		}
	}

	public boolean isValidIP(String ip, String port) {
		String delim = "[.]";
		String[] parsed = ip.split(delim);
		
		if(ip.toLowerCase().equals("localhost") && isValidPort(port)) {
			return true;
		}
		
		if(parsed.length!=4 && ip.toLowerCase()!="localhost") {
			return false;
		}
		if(port.length()==0) {
			return false;
		}
		try { 
	        Integer.parseInt(port); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
		return true;
	}
	
	boolean isValidPort(String port) {
		try { 
	        Integer.parseInt(port); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
		return true;
	}
	
	@Override
	public int getID() {
		return Util.MULTI_PLAY_MENU_STATE;
	}

}
