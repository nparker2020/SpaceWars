package com.noahparker.spacewars;


public class Util {
	//get controllerID and dispatch to individual PlayerControllerController!
	public static final int A_BUTTON = 1;
	public static final int B_BUTTON = 2;
	public static final int X_BUTTON = 3;
	public static final int Y_BUTTON = 4;
	public static final int R_SHOULDER = 6;
	public static final int L_SHOULDER = 5;
	public static final int START = 8;
	public static final int BACK = 7;
	
	public static final int RIGHT_TRIGGER_AXIS = 4;
	public static final int LEFT_STICK_HORIZONTAL = 1; //ranges from -1.0 (full left) to 1.0 (full right)
	public static final int LEFT_STICK_VERTICAL = 0;	//same
	
	public static final int MENU_STATE = 0;
	public static final int PLAY_MENU_STATE = 1;
	public static final int SINGLE_PLAY_MENU_STATE = 2;
	public static final int MULTI_PLAY_MENU_STATE = 3;
	public static final int SINGLEPLAYER_GAME_STATE = 4; //single player
	public static final int MULTIPLAYER_GAME_STATE = 5;
	public static final int DEBUG_STATE = 6;
	//Record all initial values of controllers and then compare on events?

	
	//spawn points?
}
