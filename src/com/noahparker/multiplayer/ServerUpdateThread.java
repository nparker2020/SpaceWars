package com.noahparker.multiplayer;

public class ServerUpdateThread extends Thread {
	MultiplayerServer server;
	boolean running = true;
	
	public ServerUpdateThread(MultiplayerServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		while(running) {
			/*server.updatePlayers();
			System.out.println("Before sleep. "+System.currentTimeMillis());
			try {
				ServerUpdateThread.sleep(30);
			} catch (InterruptedException e) {
				System.out.println("Oh no! There was an error in the Server Thread!");
				e.printStackTrace();
			}
			System.out.println("After sleep. "+System.currentTimeMillis());*/
		}
	}
	
	public void stopServer() {
		running = false;
	}
}
