package com.noahparker.spacewars;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayMenuState extends BasicGameState {
	Menu main;
	Menu current_selected;
	Button current;
	TrueTypeFont font;
	StateBasedGame game;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		main = new Menu();
		main.addButton(100, 100, 200, 100, "Single-Player", 0);
		main.addButton(100, 250, 200, 100, "Multi-Player", 1);
		current_selected = main;
		font = new TrueTypeFont(new Font("Arial", Font.BOLD, 24), false);
		this.game = game;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
		for(int i = 0; i<main.getNumberButtons(); i++){
			font.drawString(main.getButton(i).getShape().getCenterX()-font.getWidth(main.getButton(i).getText())/2, main.getButton(i).getShape().getCenterY(), main.getButton(i).getText());
		}
		if(current!=null) {
			g.draw(current.getShape());
		}
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		
	}

	public void controllerButtonPressed(int controller, int button) {
		System.out.println(button);
	}
	
	public void keyPressed(int key, char c) {
		//go through all possible controls, set current
			if(current==null) {
				current = main.getButton(0);
				return;
			}

			switch(key) {
			case Input.KEY_DOWN:
				current = main.moveDown();
				break;
			case Input.KEY_UP:
				current = main.moveUp();
				break;
			case Input.KEY_ENTER:
				//selectButton(current);
				if(current.ID== 0) {
					//singleplayer
					game.enterState(Util.SINGLE_PLAY_MENU_STATE);
				}else{
					//multiplayer
					game.enterState(Util.MULTI_PLAY_MENU_STATE);
				}
				break;
			case Input.KEY_ESCAPE:
				game.enterState(Util.MENU_STATE);
				break;
			}
	}

	
	@Override
	public int getID() {
		return Util.PLAY_MENU_STATE;
	}

}
