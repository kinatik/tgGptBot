package ru.novik.tggptbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.novik.tggptbot.TgGptBot;

@Component
public class BotInitializer {
    private final TgGptBot myTelegramBot;
    @Autowired
    public BotInitializer(TgGptBot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init()throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBotsApi.registerBot(myTelegramBot);
        } catch (TelegramApiException ignored){}
    }
}