package com.noahparker.spacewars;

public class Menu {
	private int numbuttons = 0;
	private Button buttons[] = new Button[6];
	private int current = 0;
	Button activator;
	
	
	public void addButton(float x, float y, int width, int height, String text, int ID) {
		buttons[numbuttons] = new Button(x, y, width, height, text, ID);
		numbuttons += 1;
		//System.out.println("Buttons Added");
	}
	
	public void addButton(Button b) {
		buttons[numbuttons] = b;
		numbuttons += 1;
	}
	
	public void setActivator(Button b) {
		activator = b;
	}
	
	public Button getButton(int index) {
		//System.out.println("index: "+index);
		//System.out.println(buttons[index]);
		return buttons[index];
	}
	
	public int getNumberButtons() {
		return numbuttons;
	}
	
	public Button moveDown() {
		if(current == numbuttons-1) {
			current = 0;
		}else{
			current += 1;
		}
		return buttons[current];
	}
	
	public Button moveUp() {
		if(current == 0) {
			current = numbuttons - 1;
		}else{
			current -= 1;
		}
		return buttons[current];
	}
}
