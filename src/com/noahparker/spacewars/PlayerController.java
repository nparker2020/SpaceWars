package com.noahparker.spacewars;

import org.newdawn.slick.state.BasicGameState;

public abstract class PlayerController {
	
	public abstract void checkInput();
	
	public abstract String getStartButton();

	public abstract BasicGameState getGame();
}
