package com.alex.message.main.port;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ClientHandler implements Runnable {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private InetSocketAddress sa;
	private String hostName;
	private InetAddress hostAddress;
	private String hostIp;
	private List<Socket> mLinkedSocketList = null;
	private Map<Socket, PrintWriter> mSocketOutputMap = null;

	public ClientHandler(Socket socket, List<Socket> linkedSocketList,
			Map<Socket, PrintWriter> socketOutputMap) {
		this.socket = Objects.requireNonNull(socket);
		System.out.println("server: accepted socket = " + socket);
		sa = (InetSocketAddress) socket.getRemoteSocketAddress();
		hostName = sa.getHostName();
		hostAddress = sa.getAddress();
		hostIp = hostAddress.getHostAddress();
		mLinkedSocketList = linkedSocketList;
		mSocketOutputMap = socketOutputMap;
	}

	@Override
	public void run() {
		try{ 
			// 减少代码量的花招……
			System.out.println("server: client get in! device: hostIp = "
					+ hostIp + ", hostName = " + hostName
					+ ", linked clients numbers = " + mLinkedSocketList.size());
			OutputStream osBuffer = socket.getOutputStream();
			System.out.println("server: socket buffer output stream = "
					+ osBuffer);
			// 包装套接字的输入流以读取客户端发送的文本行。
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(),
					StandardCharsets.UTF_8));
			// 包装套接字的输出流以向客户端发送转换结果。
			out = new PrintWriter(new OutputStreamWriter(osBuffer,
					StandardCharsets.UTF_8), true);
			System.out.println("server: in = " + in + "; out = " + out);
			mSocketOutputMap.put(socket, out);
			String line = null;
			// int j = 0;
			// for (int i = 0; i < mLinkedSocketList.size(); i++) {
			// System.out.println("server: send to others, id = " + i);
			// try {
			// Socket socket = mLinkedSocketList.get(i);
			// System.out.println("server: socket = " + socket);
			// OutputStream os = socket.getOutputStream();
			// System.out.println("server: socket output stream = " + os);
			// PrintWriter o = new PrintWriter(new OutputStreamWriter(os));
			// o.println("server: client get in! device: hostIp = "
			// + hostIp + ", hostName = " + hostName
			// + ", linked clients numbers = "
			// + mLinkedSocketList.size() + "times = " + j);
			// System.out.println("server: send to others, id = " + i
			// + ", finished! ");
			// j++;
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			for (int i = 0; i < mLinkedSocketList.size(); i++) {
				System.out.println("server: send to others, id = " + i);
				Socket socket = mLinkedSocketList.get(i);
				PrintWriter o = mSocketOutputMap.get(socket);
				o.println("server: client get in! device: hostIp = "
						+ hostIp + ", hostName = " + hostName
						+ ", linked clients numbers = "
						+ mLinkedSocketList.size());
				System.out.println("server: send to others, id = " + i
						+ ", finished! ");
			}
			while ((line = in.readLine()) != null) {
				System.out.println("server: line = " + line);
				if (line.equals("bye")) {
					break;
				}
				if (line.equals("|")) {
				} else {
					out.println(line.toUpperCase(Locale.ENGLISH));
				}
			}
			in.close();
			out.close();
			socket.close();
			mSocketOutputMap.remove(socket);
			mLinkedSocketList.remove(socket);
			System.out.println("server: client leave! device: hostIp = "
					+ hostIp + ", hostName = " + hostName
					+ ", linked clients numbers = " + mLinkedSocketList.size());
			for (int i = 0; i < mLinkedSocketList.size(); i++) {
				System.out.println("server: send to others, id = " + i);
				Socket socket = mLinkedSocketList.get(i);
				PrintWriter o = mSocketOutputMap.get(socket);
				o.println("server: client get leave! device: hostIp = "
						+ hostIp + ", hostName = " + hostName
						+ ", linked clients numbers = "
						+ mLinkedSocketList.size());
				System.out.println("server: send to others, id = " + i
						+ ", finished! ");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
