package org.jweb.core.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketUtil {
	private static Socket getSocket(String ip, int port, String localIp,
			int localPort) throws IOException {
		Socket socket = null;
		if (localIp != null && localPort > 0) {
			if ("".equals(localIp))
				localIp = "127.0.0.1";
			socket = new Socket();
			socket.bind(new InetSocketAddress(localIp, localPort));
			socket.connect(new InetSocketAddress(ip, port));
		} else {
			socket = new Socket(ip, port);
		}
		return socket;
	}

	/**
	 * 此方法使用println发�?字符串，因此会在�?��多加�?��\n\r
	 * 
	 * @param ip
	 * @param port
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String sendStr(String ip, int port, String str)
			throws Exception {
		return sendStr(ip, port, str, null, 0);
	}

	/**
	 * 此方法使用println发�?字符串，因此会在�?��多加�?��\n\r
	 * 
	 * @param ip
	 * @param port
	 * @param str
	 * @param localIp
	 * @param localPort
	 * @return
	 * @throws Exception
	 */
	public static String sendStr(String ip, int port, String str,
			String localIp, int localPort) throws Exception {
		Socket socket = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		try {
			socket = getSocket(ip, port, localIp, localPort);
			dis = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(str);
			dos.flush();
			return dis.readUTF();
		} finally {
			StreamUtil.close(dis);
			StreamUtil.close(dos);
			if (socket != null)
				socket.close();
		}
	}

	public static Object sendObject(String ip, int port, Object m)
			throws Exception {
		return sendObject(ip, port, m, null, 0);
	}

	public static Object sendObject(String ip, int port, Object m,
			String localIp, int localPort) throws Exception {
		Socket socket = getSocket(ip, port, localIp, localPort);
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(m);
			ois = new ObjectInputStream(socket.getInputStream());
			while (true) {
				Object o = ois.readObject();
				if (o != null) {
					return o;
				}
				Thread.sleep(500);
			}
		} finally {
			StreamUtil.close(ois);
			StreamUtil.close(oos);
			if (socket != null)
				socket.close();
		}
	}
}