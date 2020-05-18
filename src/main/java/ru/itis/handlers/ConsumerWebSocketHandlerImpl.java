package ru.itis.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itis.ConsumerCallback;
import ru.itis.ConsumerWebSocketHandler;
import ru.itis.MessageDto;
import ru.itis.services.interfaces.RequestDispatcher;

@Data
@Component
public class ConsumerWebSocketHandlerImpl extends TextWebSocketHandler implements ConsumerWebSocketHandler<MessageDto> {
    private ConsumerCallback<MessageDto> callback;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RequestDispatcher requestDispatcher;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        MessageDto message = objectMapper.readValue(payload, MessageDto.class);
        MessageDto mesToSend = requestDispatcher.dispatch(callback, message);
        if (mesToSend != null){
            String string = objectMapper.writeValueAsString(mesToSend);
            session.sendMessage(new TextMessage(string));
        }
    }
}