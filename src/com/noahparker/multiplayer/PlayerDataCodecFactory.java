package com.noahparker.multiplayer;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class PlayerDataCodecFactory implements ProtocolCodecFactory {
	ProtocolEncoder encoder;
	ProtocolDecoder decoder;
	
	public PlayerDataCodecFactory(boolean client) {
		if(client) {
			encoder = new PlayerDataEncoder();
			decoder = new PlayerDataDecoder();
		}else {
			encoder = new PlayerDataEncoder();
			decoder = new PlayerDataDecoder();
		}
	}
	
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		System.out.println("PLAYER DATA DECODER REQUESTED.");
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

}
