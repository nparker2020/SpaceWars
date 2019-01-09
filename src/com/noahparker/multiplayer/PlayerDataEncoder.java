package com.noahparker.multiplayer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class PlayerDataEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession session) throws Exception {
				
	}

	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer;
		if(obj instanceof float[]) {
			System.out.println("Encoding Float Array Object!");
			float data[] = (float[]) obj;
			//buffer = IoBuffer.allocate(24, false); //should totally work I swear.
			int packetid = (int) data[0];
			//buffer.putInt(MultiplayerUtils.PACKET_PLAYER_DATA);
			switch(packetid) {
			case MultiplayerUtils.PACKET_PLAYER_DATA:
				
				buffer = IoBuffer.allocate(28, false);
				System.out.println("D length: "+data.length);
				buffer.putInt((int) data[0]);
				buffer.putFloat(data[1]); //x
				buffer.putFloat(data[2]); //y
				buffer.putFloat(data[3]); //xvel
				buffer.putFloat(data[4]); //yvel
				buffer.putFloat(data[5]); //rotation
				buffer.putFloat(data[6]); //player ID
				buffer.flip(); //make buffer readable
				out.write(buffer); //send
				System.out.println("Player Data for player "+(data[6]+1)+" Encoded. Contents: ");
				for(int i = 0; i<data.length;i++) {
					System.out.println(buffer.getFloat(i));
				}
				break;
				default:
					System.out.println("Packet Header of Float Array was :"+data[0]);
					break;
			}
			
			
		}else if(obj instanceof int[]) {
			//buffer = IoBuffer.allocate(20, false);
			int[] data = (int[]) obj;
			switch(data[0]) {
			case MultiplayerUtils.PACKET_PLAYER_JOIN:
				buffer = IoBuffer.allocate(20, false);
				int packetid = data[0];
				buffer.putInt(packetid);
				buffer.putInt(data[1]);
				buffer.putInt(data[2]);
				buffer.putInt(data[3]);
				buffer.putInt(data[4]);
				buffer.flip();
				out.write(buffer);
				System.out.println("Player Join Data Encoded from PlayerData Encoder!");
				break;
			/*case MultiplayerUtils.PACKET_CLIENT_PLAYER_DATA:
				buffer = IoBuffer.allocate(16, false);
				buffer.putInt(data[0]);
				buffer.putInt(data[1]);
				buffer.putInt(data[2]);
				buffer.putInt(data[3]);
				buffer.flip();
				out.write(buffer);
				System.out.println("Client Player Data Encoded!");
				break;*/
			case MultiplayerUtils.PACKET_GAME_START:
				buffer = IoBuffer.allocate(16, false);
				int id = data[0];
				buffer.putInt(id);
				buffer.putInt(data[1]);
				buffer.putInt(data[2]);
				buffer.putInt(data[3]);
				buffer.flip();
				out.write(buffer);
				System.out.println("Game Start Data Encoded from PlayerData Encoder!");
				break;
			case MultiplayerUtils.PACKET_GAME_END:
				
				break;
			}
			
		}
		/*if(obj instanceof float[]) { //re-wrote to encode for any size float packet (more portable)
			//buffer = IoBuffer.allocate(24, false);
			System.out.println("Type was F!");
			float data[] = (float[]) obj;
			buffer = IoBuffer.allocate(24, false); //should totally work I swear.
			buffer.putFloat(data[0]); //x
			buffer.putFloat(data[1]); //y
			buffer.putFloat(data[2]); //xvel
			buffer.putFloat(data[3]); //yvel
			buffer.putFloat(data[4]); //rotation
			buffer.putFloat(data[5]); //player ID
			
			for(int i = 0; i<data.length; i++) {
				buffer.putFloat(data[i]);
			}
			buffer.flip();
			out.write(data);
			System.out.println("Player Data for player"+(data[5]+1)+" Encoded.");
		}else if(obj instanceof int[]) { //re-wrote to encode any size int array, more portable.
			
			int[] data = (int[]) obj;
			buffer = IoBuffer.allocate(4*data.length, false);
			buffer.putInt(data[0]);
			buffer.putInt(data[1]);
			buffer.putInt(data[2]);
			buffer.putInt(data[3]);
			
			for(int i = 0; i<data.length; i++) {
				buffer.putInt(data[i]);
			}
			
			buffer.flip();
			out.write(buffer);
			System.out.println("Player Join Data Encoded");
		}*/
		
	}

}
