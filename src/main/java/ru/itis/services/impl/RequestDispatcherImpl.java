package ru.itis.services.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.itis.ConsumerCallback;
import ru.itis.MessageDto;
import ru.itis.services.interfaces.RequestDispatcher;

import java.util.HashMap;
import java.util.Map;


@Component
public class RequestDispatcherImpl implements RequestDispatcher {

    @SneakyThrows
    @Override
    public MessageDto dispatch(ConsumerCallback<MessageDto> callback, MessageDto message) {
        Map<String, String> headers = message.getHeaders();
        String command = headers.get("command");
        System.out.println(message);
        MessageDto res = null;
        switch (command) {
            case "receive": {
                MessageDto messageDto = MessageDto
                        .builder()
                        .status(MessageDto.Status.ACCEPTED)
                        .messageId(headers.get("messageId"))
                        .body(message.getBody())
                        .build();

                callback.onReceive(messageDto);

                Map<String, String> resHeaders = new HashMap<>();
                resHeaders.put("command", "update");
                resHeaders.put("messageId", headers.get("messageId"));

                res = MessageDto
                        .builder()
                        .headers(resHeaders)
                        .body(messageDto.getStatus().getTitle())
                        .build();

                break;
            }
            case "error": {
                System.out.println(message);
                System.out.println(message.getBody());
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }
        return res;
    }
}
