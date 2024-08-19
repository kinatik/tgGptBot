package ru.novik.tggptbot.executors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.novik.tggptbot.CompletionService;
import ru.novik.tggptbot.TgGptBot;
import ru.novik.tggptbot.properties.BotProperty;

@Component
@Slf4j
public class StartExecutor implements CommandExecutor {

    private final CompletionService completionService;
    private final BotProperty botProperty;
    private TgGptBot tgGptBot;

    public StartExecutor(CompletionService completionService, BotProperty botProperty) {
        this.completionService = completionService;
        this.botProperty = botProperty;
    }

    public CommandExecutor setTgGptBot(TgGptBot tgGptBot) {
        this.tgGptBot = tgGptBot;
        return this;
    }

    @Override
    public void execute(Update update) {
        log.info("Received /start command");
        Long chatId = update.getMessage().getChatId();
        completionService.clearChatHistory(chatId);
        sendMessage(chatId, botProperty.getMessageStartCommandReply());
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
