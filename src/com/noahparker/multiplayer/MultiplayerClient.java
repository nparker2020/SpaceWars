package com.noahparker.multiplayer;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.noahparker.spacewars.MultiplayerGameState;
import com.noahparker.spacewars.SpaceWars;

public class MultiplayerClient {
	NioSocketConnector connector;
	MultiplayerClientHandler handler;
	IoSession session;
	private String HOSTNAME;
	private int PORT;
	float[] testpacket = {MultiplayerUtils.PACKET_PLAYER_DATA, 5f,4f,3f,2f,1f,0f};
	
	public MultiplayerClient(MultiplayerGameState state, String host, int port) {
		this.HOSTNAME = host;
		this.PORT = port;
		connector = new NioSocketConnector();
		handler = new MultiplayerClientHandler(state);
		connector.setConnectTimeoutMillis(10000);
		connector.getFilterChain().addFirst("codec", new ProtocolCodecFilter(new PlayerDataCodecFactory(true)));
		//connector.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(new PlayerJoinCodecFactory(true)));
		//connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PlayerJoinCodecFactory(true)));
		connector.setHandler(handler);
		
		try {
			ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, PORT));
			future.awaitUninterruptibly();
			session = future.getSession();
		} catch(RuntimeIoException e) {
			System.out.println("Oh no! client failed to connect!");
			e.printStackTrace();
		}
		//sendPacket(testpacket);
	}
	
	public void sendPacket(Object obj) {
		session.write(obj);
	}
	
	public int[] getPlayersJoined() {
		return handler.getPlayersJoined();
	}
	
	public void getData() {
		
	}
}
