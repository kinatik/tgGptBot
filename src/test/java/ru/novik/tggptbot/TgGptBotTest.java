package ru.novik.tggptbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.novik.tggptbot.executors.CommandExecutorFactory;
import ru.novik.tggptbot.properties.BotProperty;
import ru.novik.tggptbot.validation.MessageValidator;

import java.lang.reflect.Method;
import java.util.Set;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TgGptBotTest {

    private TgGptBot tgGptBot;
    private BotProperty botProperty;
    private CompletionService completionService;
    private MessageValidator messageValidator;
    private CommandExecutorFactory commandExecutorFactory;

    @BeforeEach
    public void setUp() {
        botProperty = mock(BotProperty.class);
        completionService = mock(CompletionService.class);
        messageValidator = mock(MessageValidator.class);
        commandExecutorFactory = mock(CommandExecutorFactory.class);
        tgGptBot = spy(new TgGptBot(botProperty, messageValidator, commandExecutorFactory));
    }

    @Test
    public void testStartCommand() throws NoSuchMethodException, TelegramApiException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(123L);
        when(botProperty.getAllowedChatIds()).thenReturn(Set.of(123L));
        when(botProperty.getMessageStartCommandReply()).thenReturn("Welcome!");

        tgGptBot.onUpdateReceived(update);

        verify(completionService).clearChatHistory(123L);
        verify(tgGptBot).execute(any(SendMessage.class));
    }

    @Test
    public void testRegularMessage() throws NoSuchMethodException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Hello");
        when(message.getChatId()).thenReturn(123L);
        when(botProperty.getAllowedChatIds()).thenReturn(Set.of(123L));
        when(completionService.call(123L, "Hello")).thenReturn("Response");

        tgGptBot.onUpdateReceived(update);

        verify(completionService).call(123L, "Hello");
        Method sendMessageMethod = TgGptBot.class.getDeclaredMethod("sendMessage", Long.class, String.class);
        sendMessageMethod.setAccessible(true);
//        verify(tgGptBot).sendMessage(123L, "Response");
    }

    @Test
    public void testNotAllowedChatId() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Hello");
        when(message.getChatId()).thenReturn(123L);
        when(botProperty.getAllowedChatIds()).thenReturn(Set.of(456L));
        when(botProperty.getMessageNotAllowedChatIdReply()).thenReturn("Not allowed");

        tgGptBot.onUpdateReceived(update);

//        verify(tgGptBot).sendMessage(123L, "Not allowed");
    }
}