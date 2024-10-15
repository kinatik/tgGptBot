package ru.novik.tggptbot.executors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.novik.tggptbot.properties.BotProperty;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommandExecutorFactoryTest {

    @Test
    void getCommandExecutor() {
        ReplyExecutor replyExecutor = mock(ReplyExecutor.class);
        ImageExecutor imageExecutor = mock(ImageExecutor.class);
        StartExecutor startExecutor = mock(StartExecutor.class);
        BotProperty botProperty = mock(BotProperty.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(replyExecutor.setTgGptBot(any())).thenReturn(replyExecutor);
        when(imageExecutor.setTgGptBot(any())).thenReturn(imageExecutor);
        when(startExecutor.setTgGptBot(any())).thenReturn(startExecutor);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Some text");
        CommandExecutorFactory commandExecutorFactory = new CommandExecutorFactory(replyExecutor, imageExecutor, startExecutor, botProperty);
        assertEquals(replyExecutor, commandExecutorFactory.getCommandExecutor(update, null), "Should return replyExecutor by default");
        verify(replyExecutor, times(1)).setTgGptBot(null);


        when(message.getText()).thenReturn("/start");
        assertEquals(startExecutor, commandExecutorFactory.getCommandExecutor(update, null), "Should return replyExecutor by default");
        verify(startExecutor, times(1)).setTgGptBot(null);
    }
}