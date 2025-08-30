package com.skillswap.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SignalingHandler extends TextWebSocketHandler {

    // A thread-safe map to store sessions. Key: sessionId, Value: another map of userId -> WebSocketSession
    private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Extract sessionId and userId from the handshake URI
        // e.g., /signaling?sessionId=123&userId=456
        String sessionId = getSessionId(session);
        String userId = getUserId(session);

        // Create the room if it doesn't exist
        rooms.putIfAbsent(sessionId, new ConcurrentHashMap<>());
        Map<String, WebSocketSession> room = rooms.get(sessionId);
        
        // Add the user to the room
        room.put(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = getSessionId(session);
        String userId = getUserId(session);
        Map<String, WebSocketSession> room = rooms.get(sessionId);

        // Relay the message to the *other* user in the room
        if (room != null) {
            for (WebSocketSession peerSession : room.values()) {
                if (!peerSession.getId().equals(session.getId())) {
                    try {
                        peerSession.sendMessage(message);
                    } catch (IOException e) {
                        System.err.println("Error sending message to peer: " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = getSessionId(session);
        String userId = getUserId(session);
        Map<String, WebSocketSession> room = rooms.get(sessionId);

        if (room != null) {
            room.remove(userId);
            // If the room is empty, remove it
            if (room.isEmpty()) {
                rooms.remove(sessionId);
            }
        }
    }

    private String getSessionId(WebSocketSession session) {
        return (String) session.getAttributes().get("sessionId");
    }

    private String getUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }
}
