package com.noahparker.spacewars;

public abstract class ItemWeapon extends ItemPickup {

	//location of item displayed on gamescreen, where players must pick it up
	public ItemWeapon(float x, float y, int width, int height) {  
		super(x, y, width, height);
	}

	public abstract void getProjectile(float x, float y, float theta, int ID);
	
	//return shape? run getProjectile(Graphics?) **** do this! 
	//public abstract void renderProjectile(Graphics g, float x, float y, float theta, int ID, Color color);
	
	//public abstract void onPickup(float x, float y, float theta);
	
	
	
}
