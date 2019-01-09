package com.noahparker.multiplayer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class PlayerJoinDataDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception {
		System.out.println("Player Join Data Decoder Ran, buffer=="+buffer.toString());
		if(buffer.remaining()>=20) {
			int data[] = new int[4];
			int id = buffer.getInt();
			data[0] = buffer.getInt(); 
			data[1] = buffer.getInt(); 
			data[2] = buffer.getInt(); 
			data[3] = buffer.getInt(); 
			out.write(data);
			System.out.println("Player Join Data Decoded. contents: "+data[0]+", "+data[1]+", "+data[2]+", "+data[3]);
			return true;
		}
		return false;
	}

}
