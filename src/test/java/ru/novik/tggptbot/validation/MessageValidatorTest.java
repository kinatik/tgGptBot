package ru.novik.tggptbot.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.novik.tggptbot.properties.BotProperty;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageValidatorTest {

    @Mock
    BotProperty botProperty;

    @Mock
    Update update;

    MessageValidator messageValidator;

    @BeforeEach
    void setUp() {
        messageValidator = new MessageValidator(botProperty);
    }

    @Test
    void check_when_hasMessage_is_false() {
        when(update.hasMessage()).thenReturn(false);
        assertFalse(messageValidator.check(update).isValid());

        verify(update, times(0)).getMessage();
    }

    @Test
    void check_when_getMessage_is_null() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(null);

        assertFalse(messageValidator.check(update).isValid());
    }

    @Test
    void check_when_hasText_is_false() {
        Message message = mock(Message.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(false);

        assertFalse(messageValidator.check(update).isValid());
    }

    @Test
    void check_when_chatId_is_allowed() {
        Message message = mock(Message.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(botProperty.getAllowedChatIds()).thenReturn(Set.of(456L,123L,789L));
        when(message.hasText()).thenReturn(true);

        assertTrue(messageValidator.check(update).isValid());
    }

    @Test
    void check_when_chatId_is_not_allowed() {
        Message message = mock(Message.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(botProperty.getAllowedChatIds()).thenReturn(Set.of(321L));
        when(message.hasText()).thenReturn(true);

        assertFalse(messageValidator.check(update).isValid());
    }



}