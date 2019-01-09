package com.noahparker.multiplayer;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MultiplayerServerHandler implements IoHandler {
	HashMap<IoSession, Integer> sessions;
	float PlayerData[][] = new float[4][7]; //1st is MultiplayerUtils.PACKET_PLAYER_DATA;
	int players[] = {MultiplayerUtils.PACKET_PLAYER_JOIN, -1,-1,-1,-1};
	int numplayers = 0; 
	int spawnpoints[][] = new int[4][4];
	
	
	public MultiplayerServerHandler() {
		sessions = new HashMap<IoSession, Integer>();
		for(int i = 0; i<PlayerData.length; i++) {
			PlayerData[i][0] = MultiplayerUtils.PACKET_PLAYER_DATA;
			spawnpoints[i][0] = MultiplayerUtils.PACKET_GAME_START;
			if(i==0) {
				spawnpoints[i][1] = 1920/4;
				spawnpoints[i][2] = 1080/4;
			}else{
				spawnpoints[i][1] = (1920/4)*2;
				spawnpoints[i][2] = (1080/4)*2;
			}
			//spawnpoints[i][3] = PLAYERID
		}
	}
	
	public int getPlayer(int ID) {
		if(ID<0 || ID>=4) {
			return -1; 
		}
		
		for(IoSession session: sessions.keySet()) {
			if(sessions.get(session)==ID) {
				return ID;
			}
		}
		return -1;
	}
	
	public void updatePlayersJoined() {
		for(IoSession session: sessions.keySet()) {
			session.write(players);
		}
		System.out.println("Players Joined Data Pushed to all clients, contents of players: "+players[0]+", "+players[1]+", "+players[2]+", "+players[3]);
	}
	
	public void updateClientPlayerData(int playerID) { //for single player, not used
		for(IoSession session: sessions.keySet()) {
			if(sessions.get(session) == playerID) {
				//session.write(arg0)
				for(int i = 0; i<PlayerData.length; i++) {
					if(i+1!=playerID) {
						session.write(PlayerData[i]);
						System.out.println("Data for Player "+i+" sent to Player "+playerID);
					}
				}
			}
		}
	}
	
	public void updateClientsPlayerData() {
		for(IoSession session: sessions.keySet()) {
			for(int i = 0; i<numplayers/*PlayerData.length*/; i++) {
				if(i+1!=sessions.get(session)) {
					session.write(PlayerData[i]);
					System.out.println("Data for Player "+i+" sent to Player "+sessions.get(session));
				}
			}
		}
		System.out.println("CLIENTS UPDATED!");
	}
	
	public void sendGameStartPacket() {
		for(IoSession session: sessions.keySet()) {
			int playerid = sessions.get(session);
			//session.write(spawnpoints[playerid]);
			spawnpoints[playerid][3] = playerid;
			session.write(new int[] {MultiplayerUtils.PACKET_GAME_START, spawnpoints[playerid][1], spawnpoints[playerid][2], spawnpoints[playerid][3]});
			System.out.println("PlayerID variable: "+playerid+" Game Start Packet Written to Encoder: X:"+spawnpoints[playerid][1]+" Y:"+spawnpoints[playerid][2]+"PLAYER ID: "+spawnpoints[playerid][3]);
		}
		/*int numplayers = 0;
		for(int i = 1; i<players.length; i++) {
			if(players[i]!=-1) {
				numplayers += 1;
			}
		}*/
		
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable t) throws Exception {
		System.out.println("Oh no! There was a problem with the multiplayer server");
		//System.out.println("Must be the goblins again... they always chew on the wires.");
		t.printStackTrace();
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		
	}

	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		 //get primary packet recieved, add to sessions with playerID
		if(obj instanceof float[]) {
			float[] data = (float[]) obj;
			switch((int) data[0]) {
			case MultiplayerUtils.PACKET_PLAYER_DATA:
				int playerid = (int) data[5];
				float d[] = new float[6];
				
				PlayerData[(int) data[5]][0] = data[1];
				PlayerData[(int) data[5]][1] = data[2];
				PlayerData[(int) data[5]][2] = data[3];
				PlayerData[(int) data[5]][3] = data[4];
				PlayerData[(int) data[5]][4] = data[5];
				PlayerData[(int) data[5]][5] = data[6];
				
				/*for(int i = 0; i<6; i++) {
					PlayerData[playerid][i] = data[i];
				}*/
				
				System.out.println("Server: PlayerData Recieved");
				break;
			}
			
		}else if(obj instanceof int[]) {
			//int data[] = (int[]) obj;
			//for(int i = 0; i<players.length; i++) {
				/*if(players[i]!=data[i]) {
					players[i] = data[i]; //update joined value with playerID
				}*/
			//}
		}
		
	}

	@Override
	public void messageSent(IoSession session, Object obj) throws Exception {
		 
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("SERVER: Session Closed!");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("SERVER: A Client Connected!");
		PlayerData[numplayers][6] = numplayers; //saves ID
		sessions.put(session, numplayers);
		players[numplayers+1] = numplayers;
		updatePlayersJoined();
		//assignClientPlayerData(session, numplayers, 0, 0);
		numplayers++;
		//should send Player ID to client?
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		 
		
	}

}
