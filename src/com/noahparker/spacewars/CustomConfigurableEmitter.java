package com.noahparker.spacewars;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ConfigurableEmitter;

public class CustomConfigurableEmitter extends ConfigurableEmitter {
	Image image;
	
	public CustomConfigurableEmitter(String name, Image image) {
		super(name);
		this.image = image.copy();
	}
	
	@Override
	public Image getImage() {
		return image;
	}
	
}
