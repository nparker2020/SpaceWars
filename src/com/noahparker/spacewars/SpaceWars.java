package com.noahparker.spacewars;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class SpaceWars extends StateBasedGame {
	int players;
	MenuState menu;
	PlayMenuState playmenu;
	SingleplayerMenuState singleplaymenu;
	MultiplayerMenuState multiplaymenu;
	GameState game;
	DebugState debug;
	MultiplayerGameState multigame;
	static int resx = 1920;
	static int resy = 1080;
	
	public SpaceWars(String name) {
		super(name);
		menu = new MenuState();
		playmenu = new PlayMenuState();
		singleplaymenu = new SingleplayerMenuState();
		multiplaymenu = new MultiplayerMenuState();
		game = new GameState();
		multigame = new MultiplayerGameState();
		setResolution(resx, resy);
		debug = new DebugState();
		//playmenu.setGameState(game);
	}

	void setResolution(int x, int y) {
		game.setResolution(x, y);
	}
	 
	public void setPlayers(int players) {
		this.players = players;
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		menu.init(gc, this);
		//addState(debug);
		addState(menu);
		addState(playmenu);
		addState(singleplaymenu);
		addState(multiplaymenu);
		addState(game);
		multigame.init(this.getContainer(), this);
		addState(multigame);
	}

	public static void main(String args[]) {
		try {
	         AppGameContainer container = new AppGameContainer(new SpaceWars("Super Awesome Space Battle"));
	         container.setDisplayMode(resx, resy, false);
	         container.setVSync(true);
	         container.setTargetFrameRate(60);
	         container.setShowFPS(false);
	         container.start();
	      } catch (SlickException e) {
	         e.printStackTrace();
	      }
	}
	
}
