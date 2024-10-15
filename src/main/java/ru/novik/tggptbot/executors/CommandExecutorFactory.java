package ru.novik.tggptbot.executors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.novik.tggptbot.TgGptBot;
import ru.novik.tggptbot.properties.BotProperty;

@Component
@AllArgsConstructor
@Slf4j
public class CommandExecutorFactory {

    private final ReplyExecutor replyExecutor;
    private final ImageExecutor imageExecutor;
    private final StartExecutor startExecutor;
    private final BotProperty botProperty;

    public CommandExecutor getCommandExecutor(Update update, TgGptBot tgGptBot) {
        String text = update.getMessage().getText().toLowerCase();
        log.info("Received message: {}", text);
        if (text.equals("/start")) {
            return startExecutor.setTgGptBot(tgGptBot);
        } else if (botProperty.getGenerateImageKeywords().stream().anyMatch(text::startsWith)) {
            return imageExecutor.setTgGptBot(tgGptBot);
        } else {
            return replyExecutor.setTgGptBot(tgGptBot);
        }
    }
}
