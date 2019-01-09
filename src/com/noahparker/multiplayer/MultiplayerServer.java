package com.noahparker.multiplayer;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MultiplayerServer {
	NioSocketAcceptor acceptor;
	MultiplayerServerHandler handler;
	ServerUpdateThread t;
	private int port;
	int updates[] = {0,0,0,0};
	//float players[][] = new float[4][6]; //playerID x PlayerPacketData
	//Every ~30 ms, update players[][] and send player data to all clients (except their own data)
	
	public MultiplayerServer(int port) {
		this.port = port;
		acceptor = new NioSocketAcceptor();
		handler = new MultiplayerServerHandler();
		//acceptor.getFilterChain().addFirst("codec", new ProtocolCodecFilter(new PlayerDataCodecFactory(false)));
		//acceptor.getFilterChain().addFirst("codec2", new ProtocolCodecFilter(new PlayerJoinCodecFactory(false))); //"protocol"?
		acceptor.getFilterChain().addFirst("codec", new ProtocolCodecFilter(new PlayerDataCodecFactory(false)));
		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
		acceptor.setHandler(handler);
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.setReuseAddress(true);
		try {
			acceptor.bind(new InetSocketAddress(this.port));
		} catch (IOException e) {
			System.out.println("Oh no! There was an error binding to the port! Is it in use?");
			e.printStackTrace();
		}
		t = new ServerUpdateThread(this);
		//t.start();
	}
	
	public int getPlayer(int ID) {
		return handler.getPlayer(ID);
	}
	
	public void updatePlayers() {
		handler.updateClientsPlayerData(); //game data
	}
	
	public void sendGameStartPacket() {
		handler.sendGameStartPacket();
		t.start();
	}
	
}
