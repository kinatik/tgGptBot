package ru.novik.tggptbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.novik.tggptbot.executors.CommandExecutor;
import ru.novik.tggptbot.executors.CommandExecutorFactory;
import ru.novik.tggptbot.properties.BotProperty;
import ru.novik.tggptbot.validation.CheckResult;
import ru.novik.tggptbot.validation.MessageValidator;

import java.util.List;

@Slf4j
@Component
public class TgGptBot extends TelegramLongPollingBot {

    private final BotProperty botProperty;
    private final MessageValidator messageValidator;
    private final CommandExecutorFactory commandExecutorFactory;

    @Autowired
    public TgGptBot(BotProperty botProperty, MessageValidator messageValidator, CommandExecutorFactory commandExecutorFactory) {
        super(botProperty.getToken());
        this.botProperty = botProperty;
        this.messageValidator = messageValidator;
        this.commandExecutorFactory = commandExecutorFactory;
    }

    @Override
    public void onUpdateReceived(Update update) {

        CheckResult checkResult = messageValidator.check(update);
        if (!checkResult.isValid()) {
            if (checkResult.getMessage() != null) {
                log.warn("Unauthorized access attempt with chatId: {} and message: {}", update.getMessage().getChatId(), update.getMessage().getText());
                sendMessage(checkResult.getMessage());
            }
            return;
        }

        CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(update, this);
        if (commandExecutor != null) {
            commandExecutor.execute(update);
            return;
        }
    }

    public Message sendMessage(SendMessage message) {
        try {
            return execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
            throw new RuntimeException("Error sending message", e);
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return botProperty.getName();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

}
