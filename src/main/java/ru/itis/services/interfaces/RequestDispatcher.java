package ru.itis.services.interfaces;

import ru.itis.ConsumerCallback;
import ru.itis.MessageDto;

public interface RequestDispatcher {
    MessageDto dispatch(ConsumerCallback<MessageDto> callback, MessageDto message);
}
