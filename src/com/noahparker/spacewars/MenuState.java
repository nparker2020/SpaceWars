package com.noahparker.spacewars;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
//import org.newdawn.slick.particles.ConfigurableEmitter;
//import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import java.awt.Font;
import java.io.File;
//import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuState extends BasicGameState {
	Button current;
	TrueTypeFont font;
	static final int MAIN_MENU = 0;
	//static int MENU_STATE = MAIN_MENU;
	static final int OPTIONS_MENU = 1;
	//static final int PLAY_MENU = 2;
	static final int PLAY_BUTTON = 0;
	static final int OPTIONS_BUTTON = 1;
	static final int QUIT_BUTTON = 2;
	Menu main;
	int current_id = 0;
	StateBasedGame game;
	Input input;
	Player player;
	int nextmove = 0;
	Random random;
	CollisionListener listener;
	float targetrotation;
	int randx = 0;
	int randy = 0;
	int stars[][] = new int[80][3];
	ParticleSystem system;
	//Draw Menu, set up controllers, etc. use game.enterState(0,1,2,3,4, etc. -order of added to container?) function
	
	public MenuState() { //Sign in + Challenge Buttons
		main = new Menu();
		main.addButton(100, 100, 200, 100, "Play", 0);
		main.addButton(100, 250, 200, 100, "Options", 1);
		main.addButton(100, 400, 200, 100, "Quit", 2);
		current = main.getButton(0);
		random = new Random();
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		//current_menu = main;
		this.game = game;
		this.input = gc.getInput();
		//System.out.println(font);
		System.out.println("Font initializing....");
		Font f = new Font("Arial", Font.BOLD, 24);
		System.out.println("BASIC FONT TYPE: "+f);
		font = new TrueTypeFont(new Font("Arial", Font.BOLD, 24), false);
		System.out.println("TRUETYPE FONT: "+font);
		player = new Player((gc.getWidth()/4)*3, gc.getHeight()/2, 0, Color.white);
		
		Image image = new Image(this.getClass().getResourceAsStream("/com/noahparker/resources/particle_circle.png"), "particle", false);
		 
		system = new ParticleSystem(image, 2000);
		system.setBlendingMode(ParticleSystem.BLEND_COMBINE);
		File xml = null;
		 
		URL url = this.getClass().getResource("/com/noahparker/resources/test_emitter.xml");
		 try {
			 xml = new File(url.toURI());
		} catch (URISyntaxException e) {
			System.out.println("Failed to load xml file for particle effects! Oh no!");
			e.printStackTrace();
		}
		
		player.setEmitter(xml, image);
		system.addEmitter(player.getEmitter());
		system.setRemoveCompletedEmitters(false);
		
		listener = new CollisionListener(this, new Player[]{player}, new int[]{0,0,0,0}, gc.getWidth(), gc.getHeight(), 0);
	
		Random r = new Random();
		for(int b = 0; b<80;b++) {
			stars[b][0] = r.nextInt(1920);
			stars[b][1] = r.nextInt(1080);
			stars[b][2] = r.nextInt(5);
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		//g.drawString("Menu State", 100, 100);
		//System.out.println("Font: "+font);	
		
		for(int a = 0; a<80; a++) {
			g.setColor(Color.white);
			g.fillOval(stars[a][0], stars[a][1], stars[a][2], stars[a][2]);
		}
		
		g.setLineWidth(1);
			for(int i = 0; i<main.getNumberButtons(); i++){
				font.drawString((main.getButton(i).getShape().getCenterX()-font.getWidth(main.getButton(i).getText())/2), main.getButton(i).getShape().getCenterY(), main.getButton(i).getText());
				//g.drawString(main.getButton(i).getText(), main.getButton(i).getShape().getCenterX(), main.getButton(i).getShape().getCenterY());
			}
			if(current!=null) {
				g.draw(current.getShape());
			}
			g.setLineWidth(5);
			g.setColor(Color.blue);
			g.draw(player.getShape());
			g.setLineWidth(1);
			g.setColor(Color.white);
			g.draw(player.getShape());
			g.setLineWidth(5);
			
			g.setLineWidth(1);
			g.draw(player.getFlame());
			
			if(player.isThrusting()) {
				g.setLineWidth(5);
				g.setColor(Color.cyan);
				g.drawLine(player.getFlame().getPoint(4)[0], player.getFlame().getPoint(4)[1], player.getFlame().getPoint(2)[0], player.getFlame().getPoint(2)[1]);
			}
			//g.fillRect(randx, randy, 5, 5);
			//g.drawString(""+player.getTheta(), 400, 300);
			//g.drawString(""+targetrotation, 400,400);
			system.render();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		nextmove+=1;
		targetrotation = (float) Math.atan2((player.getY()-randy), (player.getX()-randx));
		
		if(targetrotation>0) {
			player.setRotation(-0.06f);
			//System.out.println("Rotation negative.");
		}else{
			player.setRotation(0.06f);
			//System.out.println("Rotation positive.");
		}
		
		targetrotation += Math.PI/2;
		targetrotation += Math.PI;
		
		targetrotation %= Math.PI*2;
		
		
		
		if(Math.abs(player.getTheta()-targetrotation)<.05) {
			player.setRotation(0.0f);
			player.setThrusting(true);
			//System.out.println("Rotation stopped.");
		}
		
		/*if(targetrotation<0) {
			if(player.getTheta()<=targetrotation) {
				player.setRotation(0.0f);
				//player.setThrusting(true);
			}
		}else{
			if(player.getTheta()>=targetrotation) {
				player.setRotation(0.0f);
				//player.setThrusting(true);
			}
		}*/
		if(nextmove==50) {
			player.setThrusting(false);
		}
		
		
		/*if(targetrotation<1 && targetrotation>-1) {
			player.setRotation(0.0f);
		}*/
		
		if(nextmove==100) {
			//player.setThrusting(false);
			/*int rand =  random.nextInt(2 - 0 + 1);
			switch(rand) {
			case 0:
				player.setRotation(0.06f);
				//player.setTheta(player.getTheta() + 0.06f);
				break;
			case 1:
				player.setRotation(-0.06f);
				//player.setTheta(player.getTheta() - 0.06f);
				break;
			case 2:
				player.setRotation(0.0f);
				break;
			}
			int r = random.nextInt(100);
			if(r<25) {
				player.setThrusting(true);
			}else{
				player.setThrusting(false);
			}*/
			randx = random.nextInt(gc.getWidth());
			randy = random.nextInt(gc.getHeight());
			//targetrotation = (float) Math.atan2((player.getY()-randy), (player.getX()-randx));
			
			/*if(targetrotation<0) {
				player.setRotation(-0.06f);
			}else{
				player.setRotation(0.06f);
			}*/
			
			nextmove=0;
		}
		player.setTheta(player.getTheta() + player.getRotation());
		//System.out.println(player.getTheta());
		if(player.isThrusting()) {
			player.addXVelocity((float) Math.sin(player.getTheta())*(0.1f));
			player.addYVelocity(-(float) Math.cos(player.getTheta())*(0.1f));
		}
		
		if(player.getTheta()>2*Math.PI) { 
			//player.setTheta((float) (player.getTheta() - 2*Math.PI));
			player.addTheta((float) (-2*Math.PI));
		}
		
		if(player.getTheta()<0.0f) {
			//player.setTheta((float) (player.getTheta() + 2*Math.PI));
			player.addTheta((float) (2*Math.PI));
			//System.out.println("2 PI added");
		}
		
		player.updateShape();
		player.updateLocation();
		listener.checkBounds(player);
		
		system.update(delta);
	}

	public void mousePressed(int button, int x, int y) {
		//System.out.println("clicked");
	
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
				selectButton(current);
				break;
			}
	}
	
	public void selectButton(Button b) {
		switch(b.getID()) {
		case PLAY_BUTTON:
			//MENU_STATE = PLAY_MENU;
			//switch to play menu	
			game.enterState(Util.PLAY_MENU_STATE);
			break;
		case OPTIONS_BUTTON:
			//MENU_STATE = OPTIONS_MENU;
			//switch to options menu
			break;
		case QUIT_BUTTON:
			System.exit(0);
			break;
		}
	}
	
	public void controllerDownPressed(int controller) {
		if(current==null) {
			current = main.getButton(0);
			return;
		}
		
		if(input.getAxisValue(controller, 0)==1.0) {
			return;
		}
		current = main.moveDown();
	}
	
	public void controllerButtonPressed(int controller, int button) {
		if(button==Util.START || button==Util.A_BUTTON) {
			selectButton(current);
		}
		System.out.println("Button Pressed");
	}

	public void controllerUpPressed(int controller) {
		if(current==null) {
			current = main.getButton(0);
			return;
		}
		
		if(input.getAxisValue(controller, 0)==-1.0) {
			return;
		}
		current = main.moveUp();
	}
	
	@Override
	public int getID()  {
		return Util.MENU_STATE;
	}

}
