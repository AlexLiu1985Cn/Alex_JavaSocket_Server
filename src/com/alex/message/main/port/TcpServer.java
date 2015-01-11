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
		// �����󶨵�ĳ���˿ڵ� TCP �����������׽��֡�
		serverSocket = new ServerSocket(port);
		linkedSocketList = new ArrayList<Socket>();
		socketOutputMap = new HashMap<Socket, PrintWriter>();
		System.out.println("serverSocket:" + serverSocket);
	}

	@Override
	public void run() {
		while (true) {
			try {
				// �������ķ�ʽ����һ���ͻ������ӣ����ش�������ӵ������׽��֡�
				Socket socket = serverSocket.accept();
				linkedSocketList.add(socket);
				System.out.println("server: serversocket accept, socket = " + socket);
				// �����߳��д���ͻ������ӡ�
				new Thread(new ClientHandler(socket, linkedSocketList, socketOutputMap)).start();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}