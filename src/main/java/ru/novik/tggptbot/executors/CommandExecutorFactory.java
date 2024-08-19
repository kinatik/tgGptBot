package ru.novik.tggptbot.executors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.novik.tggptbot.TgGptBot;

@Component
@AllArgsConstructor
public class CommandExecutorFactory {

    private final ReplyExecutor replyExecutor;
    private final StartExecutor startExecutor;

    public CommandExecutor getCommandExecutor(Update update, TgGptBot tgGptBot) {
        if (update.getMessage().getText().equals("/start")) {
            return startExecutor.setTgGptBot(tgGptBot);
        } else {
            return replyExecutor.setTgGptBot(tgGptBot);
        }
    }
}
