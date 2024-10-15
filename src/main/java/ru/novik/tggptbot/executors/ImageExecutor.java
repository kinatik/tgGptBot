package ru.novik.tggptbot.executors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.novik.tggptbot.CompletionService;
import ru.novik.tggptbot.ImageGenerationService;
import ru.novik.tggptbot.TgGptBot;
import ru.novik.tggptbot.properties.BotProperty;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Component
@Slf4j
public class ImageExecutor implements CommandExecutor {

    private final ImageGenerationService imageGenerationService;
    private final BotProperty botProperty;
    private TgGptBot tgGptBot;

    public ImageExecutor(ImageGenerationService imageGenerationService, BotProperty botProperty) {
        this.imageGenerationService = imageGenerationService;
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
        Message messageToDelete = sendMessage(chatId, botProperty.getMessagePleaseWaitImage());
        String base64 = imageGenerationService.call(chatId, message.getText());
        sendImage(chatId, base64);
        deleteMessage(chatId, messageToDelete.getMessageId());
    }

    private Message sendImage(Long chatId, String base64) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        InputFile inputFile = new InputFile(new ByteArrayInputStream(imageBytes), "image.png");
        sendPhotoRequest.setPhoto(inputFile);
        try {
            return tgGptBot.execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
            throw new RuntimeException("Error sending message", e);
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
