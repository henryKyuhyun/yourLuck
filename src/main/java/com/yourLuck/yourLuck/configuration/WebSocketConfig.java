package com.yourLuck.yourLuck.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket        // 웹소켓 버서 바용
@EnableWebSocketMessageBroker       // STOMP 사용
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 메시지 구독 주소의 prefix 지정
        registry.setApplicationDestinationPrefixes("/app"); // 메시지 발행 주소의 prefix 지정
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();
        // SockJS를 활용하여 WebSocket을 지원하지 않는 브라우저에 대한 호환성 제공
    }

}
