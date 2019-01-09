package com.noahparker.multiplayer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class PlayerJoinDataEncoder implements ProtocolEncoder {
	
	
	@Override
	public void dispose(IoSession session) throws Exception {
		
	}

	//send packet with playerID, if [] = -1, not joined
	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer = IoBuffer.allocate(20, false);
		if(obj instanceof int[]) {
			buffer.putInt(MultiplayerUtils.PACKET_PLAYER_JOIN);
			int[] data = (int[]) obj;
			buffer.putInt(data[0]);
			buffer.putInt(data[1]);
			buffer.putInt(data[2]);
			buffer.putInt(data[3]);
			buffer.flip();
			out.write(buffer);
			System.out.println("Player Join Data Encoded");
		}
	}
}
