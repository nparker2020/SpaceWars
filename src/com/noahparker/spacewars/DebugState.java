package com.noahparker.spacewars;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class DebugState extends BasicGameState {
	Missile m;
	float targetx = 200;
	float targety = 400;
	float normvx;
	float normvy;
	float normsimvx;
	float normsimvy;
	float theta;
	float lawangle;
	
	float tanvx;
	float tanvy;
	
	float diff;
	
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		m = new Missile(1500, 600, 0, 0, 40, 10, 0,  true);
		System.out.println("INIT THETA: "+m.getTheta());
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.setAntiAlias(true);
		// TODO Auto-generated method stub
		g.setColor(Color.white);
		g.drawString("Debug State", 100, 100);
		g.fill(m.getShape());
		g.drawRect(targetx, targety, 10, 10);
		
		g.setColor(Color.red);
		g.drawLine(m.getX(), m.getY(), m.getX()+normvx*1000, m.getY()+normvy*1000);
		
		g.setColor(Color.magenta);
		g.drawLine(m.getShape().getCenterX(), m.getShape().getCenterY(), m.getX()+normsimvx*1000, m.getY()-normsimvy*1000);
		
		g.setColor(Color.green);
		g.drawLine(m.getX(), m.getY(), m.getX()+tanvx*1000, m.getY()-tanvy*1000);
		
		
		g.setColor(Color.red);
		g.drawString("Target Angle: "+m.getTargetAngle(), m.getX(), m.getY());
		g.drawString("Theta*Pi "+m.getTheta()*Math.PI, 300, 200);
		g.drawString("Theta: "+theta, 300, 300);
		g.drawString("Target Angle*PI "+m.getTargetAngle()*Math.PI, 300, 220);
		g.drawString("Law of cosign Angle: "+lawangle*Math.PI, 300, 240);
		g.drawString("Difference: "+diff, 300, 260);
		
		//g.drawLine(m.getX()+normsimvx*1000, m.getY()-normsimvy*1000, (m.getX()+normsimvx*1000)-(normhx*1000), (m.getY()-normsimvy*1000)-(normhy*1000));
		
		//g.drawLine(m.getTargetX(), m.getTargetY(), m.getTargetX()+(normhx*1000.0f), m.getTargetY()-(normhy*1000.0f));
	
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		/*try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}*/
		
		m.setTargetX(targetx);
		m.setTargetY(targety);
		
		float vx = targetx-m.getX();
		float vy = targety-m.getY();

		
		float length = (float) Math.sqrt((vx*vx)+(vy*vy));
		
		normvx = vx/length;
		normvy = vy/length;
		
		float simvx = (float) Math.sin(m.getTheta());
		float simvy = (float) Math.cos(m.getTheta());
		
		float simlength = (float) Math.sqrt((simvx*simvx)+(simvy*simvy));
		
		normsimvx = simvx/simlength;
		normsimvy = simvy/simlength;
		
		float tantheta = (float) Math.atan2(vy, vx);
		
		tantheta += Math.PI/2.0f;
		
		if(tantheta > 2*Math.PI) {
			tantheta -= 2*Math.PI;
		}else if (tantheta<0) {
			tantheta += 2*Math.PI;
		}
		
		float difference = m.getTheta()-tantheta;
		
		if(difference>2*Math.PI) {
			difference -= 2*Math.PI;
		}else if(difference<0) {
			difference += 2*Math.PI;
		}
		
		float gain = 0.1f;
		
		if(difference>0 && difference < Math.PI) {
			m.setRotation(-gain * difference);
		}else{
			m.setRotation(gain * difference);
		}
		
		diff = difference;
		
		tanvx = (float) Math.sin(tantheta);
		tanvy = (float) Math.cos(tantheta);

		m.setVelX((float) (Math.sin(m.getTheta())*10));
		m.setVelY((float) (Math.cos(m.getTheta())*10));
		
		
		m.setTargetAngle(tantheta);
		
		theta = m.getTheta();
		
		m.updateLocation();
		m.rotateShape();
		//System.out.println("Theta: "+m.getTheta());
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return Util.DEBUG_STATE;
	}

}
