package com.skillswap.config;

import com.skillswap.websocket.HttpHandshakeInterceptor; // Import the new interceptor
import com.skillswap.websocket.SignalingHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SignalingHandler(), "/signaling")
                // This line adds the interceptor to the configuration
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}
