package com.alex.message.main;

import java.io.IOException;

import com.alex.message.main.port.TcpServer;

public class Main {

	public static void main(String[] agrs){
		System.out.println("helloworld");
		TcpServer tcpServre = null;
		try {
			tcpServre = new TcpServer(5600);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tcpServre.run();
	}
}
