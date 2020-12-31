package com.example.demo

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList


@Component
class SocketHandler : TextWebSocketHandler() {
    var sessions: MutableList<WebSocketSession> = CopyOnWriteArrayList()
    @Throws(InterruptedException::class, IOException::class)
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		session.sendMessage(TextMessage("Hello " + message + " !"))
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
    }
}


@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(SocketHandler(), "/name").setAllowedOrigins("*")
    }
}
