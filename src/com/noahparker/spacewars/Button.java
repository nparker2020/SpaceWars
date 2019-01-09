package com.noahparker.spacewars;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

public class Button {
	Polygon button;
	String text;
	String secondary = "";
	int ID;
	Menu activate;
	
	public Button(float x, float y, int width, int height, String text, int ID) {
		button = new Polygon();
		button.addPoint(x, y);
		button.addPoint(x+width, y);
		button.addPoint(x+width, y+height);
		button.addPoint(x, y+height);
		this.text = text;
		this.ID = ID;
	}
	
	void setActivate(Menu m) {
		activate = m;
	}
	
	public void setShape(Shape shape) {
		this.button = (Polygon) shape;
	}
	
	Shape getShape() {
		return button;
	}
	
	String getText() {
		return text;
	}
	
	int getID() {
		return ID;
	}
	
}
