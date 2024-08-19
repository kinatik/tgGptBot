package ru.novik.tggptbot.executors;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.novik.tggptbot.TgGptBot;

public interface CommandExecutor {
    void execute(Update update);
    CommandExecutor setTgGptBot(TgGptBot tgGptBot);
}
