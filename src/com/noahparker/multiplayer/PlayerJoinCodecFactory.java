package com.noahparker.multiplayer;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class PlayerJoinCodecFactory implements ProtocolCodecFactory {
	PlayerJoinDataEncoder encoder;
	PlayerJoinDataDecoder decoder;
	
	public PlayerJoinCodecFactory(boolean client) {
		encoder = new PlayerJoinDataEncoder();
		decoder = new PlayerJoinDataDecoder();
	}
	
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		//System.out.println("GET JOIN DATA DECODER CALLED.");
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

}
