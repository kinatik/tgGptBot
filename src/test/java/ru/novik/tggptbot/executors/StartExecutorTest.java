package ru.novik.tggptbot.executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.novik.tggptbot.CompletionService;
import ru.novik.tggptbot.TgGptBot;
import ru.novik.tggptbot.properties.BotProperty;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StartExecutorTest {

    private CompletionService completionService;
    private BotProperty botProperty;
    private TgGptBot tgGptBot;
    private StartExecutor startExecutor;

    @BeforeEach
    void setUp() {
        completionService = mock(CompletionService.class);
        botProperty = mock(BotProperty.class);
        tgGptBot = mock(TgGptBot.class);
        startExecutor = new StartExecutor(completionService, botProperty);
        startExecutor.setTgGptBot(tgGptBot);
    }

    @Test
    void setTgGptBot() {
        assertEquals(startExecutor, startExecutor.setTgGptBot(null), "Should return this");
    }

    @Test
    void execute_whenUpdateHasMessage() throws TelegramApiException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(botProperty.getMessageStartCommandReply()).thenReturn("Welcome!");

        startExecutor.execute(update);

        verify(completionService).clearChatHistory(123L);
        verify(tgGptBot).execute(any(SendMessage.class));
    }

    @Test
    void execute_whenUpdateHasNoMessage() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> startExecutor.execute(update));
    }

    @Test
    void execute_whenSendMessageThrowsException() throws TelegramApiException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(botProperty.getMessageStartCommandReply()).thenReturn("Welcome!");
        doThrow(TelegramApiException.class).when(tgGptBot).execute(any(SendMessage.class));

        assertThrows(RuntimeException.class, () -> startExecutor.execute(update));
    }
}