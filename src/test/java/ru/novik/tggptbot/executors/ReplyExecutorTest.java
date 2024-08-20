package ru.novik.tggptbot.executors;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.novik.tggptbot.CompletionService;
import ru.novik.tggptbot.TgGptBot;
import ru.novik.tggptbot.properties.BotProperty;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReplyExecutorTest {

    @Mock
    private CompletionService completionService;
    @Mock
    private BotProperty botProperty;
    @Mock
    private TgGptBot tgGptBot;
    @Mock
    private Update update;

    @Test
    void setTgGptBot() {
        ReplyExecutor replyExecutor = new ReplyExecutor(null, null);
        assertEquals(replyExecutor, replyExecutor.setTgGptBot(null), "Should return this");
    }

    @Test
    void execute() throws TelegramApiException {
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Some text");
        when(message.getChatId()).thenReturn(1L);
        when(botProperty.getMessageStartCommandReply()).thenReturn("Some text");
        Message message1 = mock(Message.class);
        when(tgGptBot.execute(any(SendMessage.class))).thenReturn(message1);
        when(completionService.call(1L, "Some text")).thenReturn("Some other text");
        when(message1.getMessageId()).thenReturn(1);
        when(botProperty.getMessagePleaseWait()).thenReturn("Please wait");

        ReplyExecutor replyExecutor = new ReplyExecutor(completionService, botProperty);
        replyExecutor.setTgGptBot(tgGptBot);
        replyExecutor.execute(update);

        verify(tgGptBot, times(1)).execute(any(DeleteMessage.class));
        verify(tgGptBot, times(2)).execute(any(SendMessage.class));

    }
}