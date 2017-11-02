package com.mo9.risk.util;

import javax.websocket.Session;

import java.io.IOException;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 功能说明：用来存储业务定义的sessionId和连接的对应关系
 * 
 * 利用业务逻辑中组装的sessionId获取有效连接后进行后续操作
 * 
 * 
 * 
 */
public class WebSocketSessionUtil {

	public static final Map<String, Session> clients = new ConcurrentHashMap<String, Session>();

	public static Session put(String userId,  Session session) {
		return clients.put(userId, session);
	}

	public static Session get(String userId) {
		return clients.get(userId);

	}

	public static Session remove(String userId) {
		return	clients.remove(userId);
	}
	
	/**
	 * 判断是否有连接
	 * @param userId
	 * @return
	 */

	public static boolean hasConnection(String userId) {
		Session session = clients.get(userId);
		return session != null && session.isOpen();
	}

	public static void sendMessage(String message, String userId) throws IOException {
		Session session = clients.get(userId);
		if (session == null || !session.isOpen()) {
			return;
		}
		session.getBasicRemote().sendText(message);
	}
}
