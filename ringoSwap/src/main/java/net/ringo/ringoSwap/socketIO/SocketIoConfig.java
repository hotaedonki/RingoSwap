package net.ringo.ringoSwap.socketIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.context.annotation.Configuration
public class SocketIoConfig 
{
    @Value("${socketio.server.port}")
    private int socketIoPort;

    @Bean
    public SocketIOServer socketIOServer() 
    {
        Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(socketIoPort);
        log.debug("SocketIOServer is Running! [ Port num : {} ]", socketIoPort);
        
        return new SocketIOServer(config);
    }
}