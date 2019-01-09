package com.noahparker.multiplayer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class PlayerDataDecoder extends CumulativeProtocolDecoder {

	//Have one decoder, ALWAYS PREFIX PACKET WITH ID INT, SET UP SWITCH STATEMENT IN DECODE!
	@Override
	public void dispose(IoSession arg0) throws Exception {
		
		
	}
	//float array {x, y, xvel, yvel, rotation, ID};

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception {
		
		switch(buffer.getInt(0)) {
		case MultiplayerUtils.PACKET_PLAYER_DATA:
			System.out.println("Prefix was PACKET_PLAYER_DATA, buffer size: "+buffer.remaining());
			if(buffer.remaining()>=28) { //6 4 byte floats + 4 byte identifier
				float data[] = new float[6]; //prefix ID in array? 
				int id = buffer.getInt(); //increments buffer to array
				data[0] = buffer.getFloat(); //x
				data[1] = buffer.getFloat(); //y
				data[2] = buffer.getFloat(); //xvel
				data[3] = buffer.getFloat(); //yvel
				data[4] = buffer.getFloat(); //rotation
				data[5] = buffer.getFloat(); //player ID
				out.write(data);
				System.out.println("Player data for player "+(data[5]+1)+" decoded. contents:");
				for(int i= 0; i<data.length; i++) {
					System.out.println(data[i]);
				}
				return true;
			}

			break;
		case MultiplayerUtils.PACKET_PLAYER_JOIN: //sent when player joins game
			if(buffer.remaining()>=20) { 
				//int id = buffer.getInt();
				int data[] = new int[5];
				data[0] = buffer.getInt(); //Packet ID 
				data[1] = buffer.getInt(); //Player one slot (filled or not)
				data[2] = buffer.getInt(); //Player two slot
				data[3] = buffer.getInt(); //Player three slot
				data[4] = buffer.getInt(); //Player four slot
				//data[5] = buffer.getInt();
				out.write(data);
				System.out.println("Player Data Packet decoded.");
				return true;
			}
			System.out.println("Player Data Packet recieved/indentified.");

			break;
			/*case MultiplayerUtils.PACKET_CLIENT_PLAYER_DATA:
			if(buffer.remaining()>=16) {
				int data[] = new int[4];
				data[0] = buffer.getInt(); //Packet ID
				data[1] = buffer.getInt(); //Player ID
				data[2] = buffer.getInt(); //Spawn X
				data[3] = buffer.getInt(); //Spawn Y
				out.write(data);
				System.out.println("Client Player Data Decoded!");
			}
			break;*/
		case MultiplayerUtils.PACKET_GAME_START:
			System.out.println("Packet Prefix was Game Start");
			//int id = buffer.getInt();
			int data[] = new int[4];
			int id = buffer.getInt();
			int x = buffer.getInt();
			int y = buffer.getInt();
			int playerid = buffer.getInt();
			data[0] = id; //packet ID
			data[1] = x;
			data[2] = y;
			data[3] = playerid;
			out.write(data);

			System.out.println("Game Start Packet Recieved and Decoded, x: "+x+", y: "+y+", ID: "+playerid);
			break;
		default:
			System.out.println("Packet Identifier not recognized... ID: "+buffer.getInt(0));
			break;
		}
		//if(buffer.get)

		return false;
	}
}
