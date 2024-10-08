package ru.novik.tggptbot.executors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.novik.tggptbot.CompletionService;
import ru.novik.tggptbot.TgGptBot;
import ru.novik.tggptbot.properties.BotProperty;

@Component
@Slf4j
public class ReplyExecutor implements CommandExecutor {

    private final CompletionService completionService;
    private final BotProperty botProperty;
    private TgGptBot tgGptBot;
    private static final int MAX_MESSAGE_LENGTH = 4096;

    public ReplyExecutor(CompletionService completionService, BotProperty botProperty) {
        this.completionService = completionService;
        this.botProperty = botProperty;
    }

    public CommandExecutor setTgGptBot(TgGptBot tgGptBot) {
        this.tgGptBot = tgGptBot;
        return this;
    }


    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Message messageToDelete = sendMessage(chatId, botProperty.getMessagePleaseWait());
        String call = completionService.call(chatId, message.getText());
        deleteMessage(chatId, messageToDelete.getMessageId());
        if (call.length() > MAX_MESSAGE_LENGTH) {
            int start = 0;
            int end = MAX_MESSAGE_LENGTH;
            while (start < call.length()) {
                sendMessage(chatId, call.substring(start, end));
                start = end;
                end = Math.min(start + MAX_MESSAGE_LENGTH, call.length());
            }
        } else {
            sendMessage(chatId, call);
        }

    }

    private void deleteMessage(Long chatId, Integer messageId) {
        try {
            DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), messageId);
            tgGptBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Error deleting message", e);
        }
    }

    private Message sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            return tgGptBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
            throw new RuntimeException("Error sending message", e);
        }
    }
}
