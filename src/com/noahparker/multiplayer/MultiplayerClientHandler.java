package com.noahparker.multiplayer;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.noahparker.spacewars.MultiplayerGameState;
import com.noahparker.spacewars.SpaceWars;

public class MultiplayerClientHandler implements IoHandler {
	MultiplayerGameState state;
	//SpaceWars main;
	float data[][] = new float[4][6]; //4 players, 6 values each
	int players[] = {-1, -1, -1, -1};
	
	public MultiplayerClientHandler(MultiplayerGameState state) {
		this.state = state;
	}
	
	public int[] getPlayersJoined() {
		return players;
	}
	
	public void setPlayers(int p1, int p2, int p3, int p4) {
		players[0] = p1;
		players[1] = p2;
		players[2] = p3;
		players[3] = p4;
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable t) throws Exception {
		System.out.println("Oh no! There was an error in the client handler!");
		t.printStackTrace();
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
			
	}

	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		if(obj instanceof float[]) {
			float d[] = (float[]) obj; //for now, always going to be player data - later there will be multiple packet types. head packets with IDs? or sort by size.
			switch((int) d[0]) {
			case MultiplayerUtils.PACKET_PLAYER_DATA:
				int packetid = (int) d[0];
				float data[] = new float[6];
				data[0] = d[1];
				data[1] = d[2];
				data[2] = d[3];
				data[3] = d[4];
				data[4] = d[5];
				data[5] = d[6];
				state.updatePlayerdata(data);
				System.out.println("Player Data Recieved, length: "+d.length);
				break;
			}
			/*if(d.length==6) {
				state.updatePlayerdata(d);
				/*for(int i = 0; i<4; i++) {
					data[i][0] = d[0]; //player ID
					data[i][1] = d[1];
					data[i][2] = d[2];
					data[i][3] = d[3];
					data[i][4] = d[4];
					data[i][5] = d[5];
				}
				//System.out.println("Client: Data Recieved.");
			}else{
				System.out.println("Client: data recieved not length 6, length was "+data.length);
			}*/

		}else if(obj instanceof int[]) {
			System.out.println("Int recieved, length==");
			int d[] = (int[]) obj;
			System.out.println("Int recieved, length=="+d.length);
			switch(d[0]) {
			case MultiplayerUtils.PACKET_PLAYER_JOIN:
				//players = d;
				setPlayers(d[1], d[2], d[3], d[4]);
				break;
			case MultiplayerUtils.PACKET_GAME_START:
				int x = d[1];
				int y = d[2];
				int id = d[3];
				//client still in MultiplayerMenuState, need to switch to MultiplayerGameState
				
				state.EnterMultiPlayerGame(getNumberPlayers(), x, y, id);
				break;
			}
			
		}else{
			System.out.println("obj was not float[] or int[]?");
		}
		/*int data[] = (int[]) obj;
		if(data.length==4) {
			System.out.println("Player Join Data Processed.");
		}*/
		
		System.out.println("Client: Message Recieved."+obj.toString());
	}
	
	public int getNumberPlayers() {
		int numplayers = 0;
		for(int i = 0; i<players.length;i++) {
			if(players[i]!=-1) {
				numplayers++;
			}
		}
		return numplayers;
	}

	@Override
	public void messageSent(IoSession session, Object obj) throws Exception {
		
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		
		
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("Client: Connected to server!");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		
	}

}
