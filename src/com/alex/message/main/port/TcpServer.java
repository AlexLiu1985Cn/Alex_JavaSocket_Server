package com.alex.message.main.port;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TcpServer implements Runnable {

	private ServerSocket serverSocket = null;
	private List<Socket> linkedSocketList = null;
	private Map<Socket, PrintWriter> socketOutputMap = null;

	public TcpServer(int port) throws IOException {
		// 创建绑定到某个端口的 TCP 服务器被动套接字。
		serverSocket = new ServerSocket(port);
		linkedSocketList = new ArrayList<Socket>();
		socketOutputMap = new HashMap<Socket, PrintWriter>();
		System.out.println("serverSocket:" + serverSocket);
	}

	@Override
	public void run() {
		while (true) {
			try {
				// 以阻塞的方式接受一个客户端连接，返回代表该连接的主动套接字。
				Socket socket = serverSocket.accept();
				linkedSocketList.add(socket);
				System.out.println("server: serversocket accept, socket = " + socket);
				// 在新线程中处理客户端连接。
				new Thread(new ClientHandler(socket, linkedSocketList, socketOutputMap)).start();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}